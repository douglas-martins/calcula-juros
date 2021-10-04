package com.github.douglasmartins.calculajuros.dto;

import com.github.douglasmartins.calculajuros.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductSaveDto {

    @NotBlank(message = "Nome do produto invalido.")
    @NotNull(message = "Nome do produto não informado")
    private String nome;


    @NotNull(message = "Preço do produto não informado")
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 9, fraction = 2)
    @Positive
    private BigDecimal valor;

    public Product transformToProduct() {
        Product product = new Product(null, this.nome, this.valor);
        return product;
    }
}
