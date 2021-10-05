package com.github.douglasmartins.calculajuros.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentInstallment {

    private Integer numeroParcela;

    private Double valor;

    private BigDecimal taxaJurosAoMes;
}
