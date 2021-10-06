package com.github.douglasmartins.calculajuros;


import com.github.douglasmartins.calculajuros.controller.SelicController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SmokeTest {

    private final SelicController controller;

    @Autowired
    public SmokeTest(SelicController controller) {
        this.controller = controller;
    }

    @Test
    public void contextLoads() throws Exception {
        assertThat(this.controller).isNotNull();
    }
}
