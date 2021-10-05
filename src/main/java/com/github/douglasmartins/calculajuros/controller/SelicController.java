package com.github.douglasmartins.calculajuros.controller;

import com.github.douglasmartins.calculajuros.controller.exception.ApiError;
import com.github.douglasmartins.calculajuros.controller.exception.ApiErrorList;
import com.github.douglasmartins.calculajuros.model.Negotiation;
import com.github.douglasmartins.calculajuros.model.PaymentInstallment;
import com.github.douglasmartins.calculajuros.model.SelicTax;
import com.github.douglasmartins.calculajuros.service.SelicService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("negociacao")
public class SelicController {
    private final SelicService selicService;

    public SelicController(SelicService service) {
        this.selicService = service;
    }

    @PostMapping
    public ResponseEntity<Object> calculateNegotiation(@RequestBody @Valid Negotiation negotiation) {
        BigDecimal productValue = negotiation.getProduto().getValor();
        BigDecimal inCashValue = negotiation.getCondicaoPagamento().getValorEntrada();
        if (productValue.subtract(inCashValue).intValue() <= 0) {
            ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(), "Valor de entrada não pode ser maior ou igual do valor do produto", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        List<SelicTax> taxs = selicService.getTax(negotiation.getCondicaoPagamento().getQtdeParcelas());
        List<PaymentInstallment> result = selicService.getNegotiationValue(negotiation, taxs);

        return ResponseEntity.ok().body(result);
    }
}
