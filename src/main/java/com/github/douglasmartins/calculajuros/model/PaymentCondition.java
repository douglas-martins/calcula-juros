package com.github.douglasmartins.calculajuros.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentCondition {

    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 9, fraction = 2)
    @Positive
    private BigDecimal valorEntrada;

    @Positive
    private Integer qtdeParcelas;
}
