package com.github.douglasmartins.calculajuros.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.github.douglasmartins.calculajuros.repository")
public class RepositoryConfiguration {
}
