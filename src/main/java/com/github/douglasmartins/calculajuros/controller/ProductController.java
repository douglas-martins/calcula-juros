package com.github.douglasmartins.calculajuros.controller;

import com.github.douglasmartins.calculajuros.dto.ProductSaveDto;
import com.github.douglasmartins.calculajuros.model.Product;
import com.github.douglasmartins.calculajuros.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "produtos")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> save(@RequestBody @Valid ProductSaveDto productDto) {
        Product createdProduct = productService.save(productDto.transformToProduct());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Product> getById(@PathVariable(name = "codigo") Long codigo) {
        Product product = productService.getById(codigo);
        return ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity<List<Product>> listAll() {
        List<Product> products = productService.listAll();
        return ResponseEntity.ok(products);
    }
}
