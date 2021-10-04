package com.github.douglasmartins.calculajuros.repository;

import com.github.douglasmartins.calculajuros.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
