package com.github.douglasmartins.calculajuros.service;

import com.github.douglasmartins.calculajuros.model.Negotiation;
import com.github.douglasmartins.calculajuros.model.PaymentCondition;
import com.github.douglasmartins.calculajuros.model.PaymentInstallment;
import com.github.douglasmartins.calculajuros.model.SelicTax;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class SelicService {

    private final RestTemplate restTemplate;

    /**
     * Construtor padrão da classe.
     *
     * @param restTemplate ‘interface’ para execução de uma chamada HTTP.
     */
    public SelicService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Executa o cálculo do valor das parcelas da negociação.
     *
     * @param negotiation objeto de dados que contêm informações da negociação que será realizada.
     * @return uma lista com todas as parcelas com valor e taxa de juros.
     */
    public List<PaymentInstallment> getNegotiationValue(Negotiation negotiation) {
        PaymentCondition paymentCondition = negotiation.getCondicaoPagamento();
        BigDecimal newTotalValue = negotiation
                .getProduto()
                .getValor()
                .subtract(paymentCondition.getValorEntrada());
        Integer installments = paymentCondition.getQtdeParcelas();

        return installments > 6 ?
                this.getIncreasedNegotiationValue(negotiation, newTotalValue) :
                this.getFixedNegotiationValue(negotiation, newTotalValue);
    }

    /**
     * Constrói uma lista com os valores das parcelas sem juros.
     *
     * @param negotiation objeto de dados que contêm informações da negociação que será realizada.
     * @param newTotalValue valor total do produto, já com a subtração do valor de entrada.
     * @return uma lista com todas as parcelas com valor e taxa de juros.
     */
    private List<PaymentInstallment> getFixedNegotiationValue(Negotiation negotiation, BigDecimal newTotalValue) {
        List<PaymentInstallment> result = new ArrayList<>();
        Integer installments = negotiation.getCondicaoPagamento().getQtdeParcelas();
        Double installmentValue = newTotalValue
                .divide(BigDecimal.valueOf(installments), RoundingMode.UNNECESSARY)
                .doubleValue();

        for (int i = 0; i < installments; i++) {
            Integer installment = (i + 1);

            PaymentInstallment paymentInstallment = new PaymentInstallment(
                    installment,
                    installmentValue,
                    new BigDecimal("0")
            );

            result.add(paymentInstallment);
        }

        return result;
    }

    /**
     * Constrói uma lista com os valores das parcelas com os juros já calculado e o seu valor.
     *
     * @param negotiation objeto de dados que contêm informações da negociação que será realizada.
     * @param newTotalValue valor total do produto, já com a subtração do valor de entrada.
     * @return uma lista com todas as parcelas com valor e taxa de juros.
     */
    private List<PaymentInstallment> getIncreasedNegotiationValue(Negotiation negotiation, BigDecimal newTotalValue) {
        List<PaymentInstallment> result = new ArrayList<>();
        Integer installments = negotiation.getCondicaoPagamento().getQtdeParcelas();
        List<SelicTax> taxs = this.getTax(installments);
        BigDecimal finalValue = this.calculateSelicTax(taxs, newTotalValue);
        BigDecimal installmentValue = finalValue
                .divide(BigDecimal.valueOf(installments), RoundingMode.HALF_UP);
        System.out.println(installmentValue);

        for (int i = 0; i < installments; i++) {
            Integer installment = (i + 1);
            SelicTax tax = i >= taxs.size() ? taxs.get(0) : taxs.get(i);
            BigDecimal taxValue = tax.getValor();

            PaymentInstallment paymentInstallment = new PaymentInstallment(
                    installment,
                    installmentValue.doubleValue(),
                    taxValue
            );

            result.add(paymentInstallment);
        }

        return result;
    }

    /**
     * Faz uma busca na API do governo pela taxa Selic de hoje, até o final da última parcela da negociação.
     *
     * @param installments número de parcelas que foi pedido pela negociação.
     * @return uma lista com todas as taxas Selic disponíveis.
     */
    private List<SelicTax> getTax(Integer installments) {
        String startData = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String endData = LocalDate.now().plusMonths(installments).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String url = "https://api.bcb.gov.br/dados/serie/bcdata.sgs.11/dados?formato=json"
                        + "&dataInicial=" + startData
                        + "&dataFinal=" + endData;

        try {
            SelicTax[] taxs = restTemplate.getForObject(url, SelicTax[].class);

            assert taxs != null;
            return List.of(taxs);
        } catch (Exception ex) {
            return this.getFixedTax();
        }
    }

    /**
     * Constrói uma lista de valores da taxa Selic, utilizada caso a requisição para API do governo não tenha sucesso.
     *
     * @return uma lista com todas as taxas Selic disponíveis.
     */
    private List<SelicTax> getFixedTax() {
        List<SelicTax> fixedTaxs = new ArrayList<>();

        String data = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        BigDecimal valor = new BigDecimal("0.023687");

        fixedTaxs.add(new SelicTax(data, valor));

        return fixedTaxs;
    }

    /**
     * Realizar o cálculo de correção do valor total do produto, aplicando os juros.
     *
     * @param taxs lista com o valor da taxa Selic.
     * @param totalValue valor total do produto antes do cálculo de juros.
     * @return valor total corrigido da negociação, com os juros calculados.
     */
    private BigDecimal calculateSelicTax(List<SelicTax> taxs, BigDecimal totalValue) {
        BigDecimal oneHundred = new BigDecimal("100");

        for (int i = 0; i < 1; i++) {
            SelicTax tax = i >= taxs.size() ? taxs.get(0) : taxs.get(i);
            BigDecimal taxValue = tax.getValor();
            int monthDays = tax.getData().lengthOfMonth();

            for (int j = 0; j < monthDays; j++) {
                totalValue = totalValue.add(totalValue.multiply(taxValue).divide(oneHundred, RoundingMode.HALF_UP));
            }
        }

        return totalValue;
    }
}
