package com.github.douglasmartins.calculajuros.dto;

import com.github.douglasmartins.calculajuros.model.PaymentCondition;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NegotiationDto {

    @NotNull(message = "Produto é obriatório.")
    private ProductSaveDto produto;

    @NotNull(message = "Condição de pagamento é obriatório.")
    private PaymentCondition condicaoPagamento;
}
