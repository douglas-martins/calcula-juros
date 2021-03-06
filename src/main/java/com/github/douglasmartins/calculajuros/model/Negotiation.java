package com.github.douglasmartins.calculajuros.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Negotiation {
    @NotNull(message = "Produto é obrigatório")
    private Product produto;

    @NotNull(message = "Condição de pagamento é obrigatório")
    private PaymentCondition condicaoPagamento;

    private Boolean useFixedValue = false;
}
