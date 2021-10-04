package com.github.douglasmartins.calculajuros.controller.exception;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiError {
    private Integer code;
    private String message;
    private LocalDateTime date;
}
