package com.adriani.imoveis_bot.service;

import com.adriani.imoveis_bot.model.ImovelDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@Service
public class ImobiliariaService {

    private final RestClient restClient;

    public ImobiliariaService(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://adrianiimoveispero.com.br/wp-json/wp/v2")
                .build();
    }

    public List<ImovelDTO> buscarImoveis() {
        ImovelDTO[] response = restClient.get()
                .uri("/imovel?per_page=100")
                .retrieve()
                .body(ImovelDTO[].class);

        if (response == null) {
            return List.of();
        }

        return Arrays.asList(response);
    }

    public void debugJson() {
        String jsonPuro = restClient.get()
                .uri("/imovel?per_page=1")
                .retrieve()
                .body(String.class);

        System.out.println("🔎 JSON BRUTO QUE O JAVA VÊ:");
        System.out.println(jsonPuro);
    }
}
