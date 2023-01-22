package br.itarocha.cartanatal.adapter.in.web.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AspectoResponse {
    @JsonProperty(value = "planeta_origem", index = 1)
    private String planetaOrigem;

    @JsonProperty(value = "planeta_destino", index = 2)
    private String planetaDestino;

    @JsonProperty(index = 3)
    private double planetaOrigemAngulo;

    @JsonProperty(index = 4)
    private double planetaDestinoAngulo;

    @JsonProperty(index = 5)
    private String aspecto;

    @JsonProperty(index = 6)
    private double orbe;

    @JsonProperty(index = 7)
    private int orbeGrau;

    @JsonProperty(index = 8)
    private int orbeMinuto;

    @JsonProperty(index = 9)
    private String orbeDescricao;

    @JsonProperty(index = 10)
    private Integer x;

    @JsonProperty(index = 11)
    private Integer y;

}
