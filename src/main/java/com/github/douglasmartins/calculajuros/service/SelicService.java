package com.github.douglasmartins.calculajuros.service;

import com.github.douglasmartins.calculajuros.model.SelicTax;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class SelicService {

    private final RestTemplate restTemplate;

    public SelicService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<SelicTax> getTax() {
        // https://api.bcb.gov.br/dados/serie/bcdata.sgs.11/dados?formato=json&dataInicial=01/09/2021&dataFinal=31/12/2021
        var url = "https://api.bcb.gov.br/dados/serie/bcdata.sgs.11/dados?formato=json&dataInicial=01/09/2021&dataFinal=31/12/2021";
        var taxs = restTemplate.getForObject(url, SelicTax[].class);
        assert taxs != null;
        return List.of(taxs);
    }
}
