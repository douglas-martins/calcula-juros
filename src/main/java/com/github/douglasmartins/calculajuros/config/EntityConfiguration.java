package com.github.douglasmartins.calculajuros.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackages = {"com.github.douglasmartins.calculajuros.model"})
public class EntityConfiguration {
}
