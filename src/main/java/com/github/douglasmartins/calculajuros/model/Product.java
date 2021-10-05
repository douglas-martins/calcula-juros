package com.github.douglasmartins.calculajuros.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity(name = "produto")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @Column(name = "name", length = 30, nullable = false)
    private String nome;

    @Column(name = "price", nullable = false)
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 9, fraction = 2)
    @Positive
    private BigDecimal valor;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Product product = (Product) o;
        return Objects.equals(codigo, product.codigo);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
