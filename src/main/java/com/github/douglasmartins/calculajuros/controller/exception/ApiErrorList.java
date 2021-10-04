package com.github.douglasmartins.calculajuros.controller.exception;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApiErrorList extends ApiError {

    private List<String> errors;

    public ApiErrorList(Integer code, String message, LocalDateTime date, List<String> errors) {
        super(code, message, date);
        this.errors = errors;
    }
}
