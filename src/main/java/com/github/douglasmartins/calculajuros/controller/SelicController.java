package com.github.douglasmartins.calculajuros.controller;

import com.github.douglasmartins.calculajuros.exception.InvalidNegotiationValues;
import com.github.douglasmartins.calculajuros.model.Negotiation;
import com.github.douglasmartins.calculajuros.model.PaymentInstallment;
import com.github.douglasmartins.calculajuros.service.SelicService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("negociacao")
public class SelicController {
    private final SelicService selicService;

    public SelicController(SelicService service) {
        this.selicService = service;
    }

    @ApiOperation(value = "Executa o cálculo de uma negociação de produto")
    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<List<PaymentInstallment>> calculateNegotiation(@RequestBody @Valid Negotiation negotiation) {
        BigDecimal productValue = negotiation.getProduto().getValor();
        BigDecimal inCashValue = negotiation.getCondicaoPagamento().getValorEntrada();
        if (productValue.subtract(inCashValue).intValue() <= 0) {
            String message = "Valor de entrada não pode ser maior ou igual do valor do produto";
            throw new InvalidNegotiationValues(message);
        }

        List<PaymentInstallment> result = selicService.getNegotiationValue(negotiation);

        return ResponseEntity.ok().body(result);
    }
}
