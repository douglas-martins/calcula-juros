package com.github.douglasmartins.calculajuros;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.douglasmartins.calculajuros.controller.SelicController;
import com.github.douglasmartins.calculajuros.model.Negotiation;
import com.github.douglasmartins.calculajuros.model.PaymentCondition;
import com.github.douglasmartins.calculajuros.model.Product;
import com.github.douglasmartins.calculajuros.service.SelicService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(SelicController.class)
public class TestingWebApplicationTest {
    private final MockMvc mockMvc;

    @MockBean
    private SelicService service;

    @Autowired
    public TestingWebApplicationTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void shouldReturnInvalidValue() throws Exception {
        Product produto = new Product(1L, "Colar de Ouro", new BigDecimal("299.99"));
        PaymentCondition paymentCondition = new PaymentCondition(new BigDecimal("300"), 10);
        Negotiation negotiation = new Negotiation(produto, paymentCondition);

        this.mockMvc.perform(
                post("/negociacao")
                        .content(this.asJsonString(negotiation))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()
        );
    }

    @Test
    public void shouldReturnValidValue() throws Exception {
        Product produto = new Product(1L, "Colar de Diamante", new BigDecimal("2000.00"));
        PaymentCondition paymentCondition = new PaymentCondition(new BigDecimal("500.00"), 10);
        Negotiation negotiation = new Negotiation(produto, paymentCondition);

        MvcResult result = this.mockMvc.perform(
                post("/negociacao")
                        .content(this.asJsonString(negotiation))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
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
