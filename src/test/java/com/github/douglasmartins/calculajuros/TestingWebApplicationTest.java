package com.github.douglasmartins.calculajuros;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.douglasmartins.calculajuros.controller.SelicController;
import com.github.douglasmartins.calculajuros.model.Negotiation;
import com.github.douglasmartins.calculajuros.model.PaymentCondition;
import com.github.douglasmartins.calculajuros.model.PaymentInstallment;
import com.github.douglasmartins.calculajuros.model.Product;
import com.github.douglasmartins.calculajuros.service.SelicService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SelicController.class)
@Import({SelicService.class, RestTemplate.class})
public class TestingWebApplicationTest {
    private final MockMvc mockMvc;

    private SelicService selicService;

    @Autowired
    public TestingWebApplicationTest(MockMvc mockMvc, SelicService selicService) {
        this.mockMvc = mockMvc;
        this.selicService = selicService;
    }

    @Test
    public void shouldReturnInvalidValue() throws Exception {
        Product produto = new Product(1L, "Colar de Ouro", new BigDecimal("299.99"));
        PaymentCondition paymentCondition = new PaymentCondition(new BigDecimal("300"), 10);
        Negotiation negotiation = new Negotiation(produto, paymentCondition, true);

        this.mockMvc.perform(
                post("/negociacao")
                        .content(this.asJsonString(negotiation))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest()
        );
    }

    @Test
    public void shouldReturnValidValue() throws Exception {
        Product produto = new Product(1L, "Colar de Diamante", new BigDecimal("3999.31"));
        PaymentCondition paymentCondition = new PaymentCondition(new BigDecimal("852.52"), 10);
        Negotiation negotiation = new Negotiation(produto, paymentCondition, true);

        MvcResult result = this.mockMvc.perform(
                post("/negociacao")
                        .content(this.asJsonString(negotiation))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        PaymentInstallment[] response = new ObjectMapper().readValue(json, PaymentInstallment[].class);

        assertThat(response.length).isEqualTo(10);

        for (int i = 0; i < paymentCondition.getQtdeParcelas(); i++) {
            PaymentInstallment paymentInstallment = response[i];
            assertThat(paymentInstallment.getNumeroParcela()).isEqualTo((i + 1));
            assertThat(paymentInstallment.getValor()).isEqualTo(317.0d);
            assertThat(paymentInstallment.getTaxaJurosAoMes()).isEqualTo(new BigDecimal("0.023687"));
        }
    }

    @Test
    public void shouldCalculateValidIncreasedNegotiationValue() {
        Product produto = new Product(1L, "Colar de Diamante", new BigDecimal("2000.00"));
        PaymentCondition paymentCondition = new PaymentCondition(new BigDecimal("500.00"), 12);
        Negotiation negotiation = new Negotiation(produto, paymentCondition, true);

        List<PaymentInstallment> result = this.selicService.getNegotiationValue(negotiation);

        assertThat(result.size()).isEqualTo(12);

        for (int i = 0; i < paymentCondition.getQtdeParcelas(); i++) {
            PaymentInstallment paymentInstallment = result.get(i);
            assertThat(paymentInstallment.getNumeroParcela()).isEqualTo((i + 1));
            assertThat(paymentInstallment.getValor()).isEqualTo(125.92d);
            assertThat(paymentInstallment.getTaxaJurosAoMes()).isEqualTo(new BigDecimal("0.023687"));
        }
    }

    @Test
    public void shouldCalculateValidNegotiationValue() {
        Product produto = new Product(1L, "Colar de Ouro", new BigDecimal("1000.00"));
        PaymentCondition paymentCondition = new PaymentCondition(new BigDecimal("200.00"), 5);
        Negotiation negotiation = new Negotiation(produto, paymentCondition, true);

        List<PaymentInstallment> result = this.selicService.getNegotiationValue(negotiation);

        assertThat(result.size()).isEqualTo(5);

        for (int i = 0; i < paymentCondition.getQtdeParcelas(); i++) {
            PaymentInstallment paymentInstallment = result.get(i);
            assertThat(paymentInstallment.getNumeroParcela()).isEqualTo((i + 1));
            assertThat(paymentInstallment.getValor()).isEqualTo(160.0d);
            assertThat(paymentInstallment.getTaxaJurosAoMes()).isEqualTo(new BigDecimal("0"));
        }
    }

    private String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
