package com.github.douglasmartins.calculajuros;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.github.douglasmartins.calculajuros.model"})
@ComponentScan(basePackages = {"com.github.douglasmartins.calculajuros.*"})
@EnableJpaRepositories(basePackages = "com.github.douglasmartins.calculajuros.repository")
@EnableConfigurationProperties
public class CalculaJurosApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalculaJurosApplication.class, args);
    }

}
