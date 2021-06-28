package br.itarocha.cartanatal.core.model.presenter;

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
    private String aspecto;

    @JsonProperty(index = 4)
    private Integer x;

    @JsonProperty(index = 5)
    private Integer y;
}
