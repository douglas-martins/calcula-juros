package com.github.douglasmartins.calculajuros.service;

import com.github.douglasmartins.calculajuros.model.Negotiation;
import com.github.douglasmartins.calculajuros.model.PaymentInstallment;
import com.github.douglasmartins.calculajuros.model.SelicTax;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class SelicService {

    private final RestTemplate restTemplate;

    public SelicService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<SelicTax> getTax(Integer installments) {
        String startData = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String endData = LocalDate.now().plusMonths(installments).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String url = "https://api.bcb.gov.br/dados/serie/bcdata.sgs.11/dados?formato=json"
                        + "&dataInicial=" + startData
                        + "&dataFinal=" + endData;

        try {
            var taxs = restTemplate.getForObject(url, SelicTax[].class);

            assert taxs != null;
            return List.of(taxs);
        } catch (Exception ex) {
            return this.getFixedTax();
        }
    }

    public List<PaymentInstallment> getNegotiationValue(Negotiation negotiation, List<SelicTax> taxs) {
        List<PaymentInstallment> result = new ArrayList<>();
        BigDecimal newTotalValue = negotiation.getProduto().getValor().subtract(negotiation.getCondicaoPagamento().getValorEntrada());

        for (int i = 0; i < negotiation.getCondicaoPagamento().getQtdeParcelas(); i++) {
            Integer installment = (i + 1);
            BigDecimal taxValue = i >= taxs.size() ? taxs.get(0).getValor() : taxs.get(i).getValor();
            BigDecimal installmentValue = this.calculateSelicTax(taxValue, newTotalValue);

            PaymentInstallment paymentInstallment = new PaymentInstallment(
                    installment,
                    installmentValue,
                    taxValue
            );

            result.add(paymentInstallment);
        }

        return result;
    }

    private List<SelicTax> getFixedTax() {
        List<SelicTax> fixedTaxs = new ArrayList<>();

        String data = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        BigDecimal valor = new BigDecimal("0.023687");

        fixedTaxs.add(new SelicTax(data, valor));

        return fixedTaxs;
    }

    private BigDecimal calculateSelicTax(BigDecimal tax, BigDecimal totalValue) {


        return new BigDecimal("0");
    }
}
