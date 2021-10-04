package com.github.douglasmartins.calculajuros.controller;

import com.github.douglasmartins.calculajuros.model.SelicTax;
import com.github.douglasmartins.calculajuros.service.SelicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("negociacao")
public class SelicController {
    private final SelicService selicService;

    public SelicController(SelicService service) {
        this.selicService = service;
    }

    // TODO: fazer try catch
    @GetMapping
    public ResponseEntity<List<SelicTax>> calculateNegotiation() {
        List<SelicTax> taxs = selicService.getTax();
        return ResponseEntity.status(200).body(taxs);
    }
}
