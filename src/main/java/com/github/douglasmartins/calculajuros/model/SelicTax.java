package com.github.douglasmartins.calculajuros.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class SelicTax {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate data;

    private BigDecimal valor;

    public SelicTax(String data, BigDecimal valor) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        this.data = LocalDate.parse(data, formatter);
        this.valor = valor;
    }
}
