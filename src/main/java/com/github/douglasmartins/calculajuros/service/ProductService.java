package com.github.douglasmartins.calculajuros.service;

import com.github.douglasmartins.calculajuros.exception.NotFoundException;
import com.github.douglasmartins.calculajuros.model.Product;
import com.github.douglasmartins.calculajuros.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product save(Product product) {
        Product createdProduct = productRepository.save(product);
        return createdProduct;
    }

    public Product getById(Long codigo) {
        Optional<Product> result = productRepository.findById(codigo);

        return result.orElseThrow(() -> new NotFoundException("Não existe nenhum produto com o código " + codigo));
    }

    public List<Product> listAll() {
        List<Product> products = productRepository.findAll();
        return products;
    }
}
