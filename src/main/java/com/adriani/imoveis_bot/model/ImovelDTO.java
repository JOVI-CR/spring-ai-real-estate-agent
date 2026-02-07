package com.adriani.imoveis_bot.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ImovelDTO(
        Long id,
        String link,

        @JsonProperty("title")
        RenderedString tituloObj,

        @JsonProperty("content")
        RenderedString descricaoObj,

        @JsonProperty("property_meta")
        PropertyMeta meta
) {

    public record RenderedString(String rendered) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PropertyMeta(
            @JsonAlias("REAL_HOMES_property_price") String price,
            @JsonAlias("REAL_HOMES_property_bedrooms") String bedrooms,
            @JsonAlias("REAL_HOMES_property_address") String address,
            @JsonAlias("REAL_HOMES_property_id") String code
    ) {}

    public String getTitulo() {
        return tituloObj != null ? tituloObj.rendered() : "";
    }

    public String getEndereco() {
        return meta != null ? meta.address() : "Localização sob consulta";
    }

    public String getQuartos() {
        return meta != null ? meta.bedrooms() : "N/A";
    }

    public String getCodigo() {
        return meta != null ? meta.code() : "";
    }

    public String getPrecoFormatado() {
        if (meta == null || meta.price() == null || meta.price().isBlank() || meta.price().equals("0")) {
            return "Sob Consulta (Varia por Temporada)";
        }
        return "R$ " + meta.price();
    }
}