package com.github.douglasmartins.calculajuros.model;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ToString
public class SelicTax {
    private LocalDate date;
    private BigDecimal valor;
}
