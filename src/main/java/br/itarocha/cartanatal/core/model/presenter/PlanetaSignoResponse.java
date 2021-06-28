package br.itarocha.cartanatal.core.model.presenter;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanetaSignoResponse {
    private String planeta;
    private String signo;
    private Integer casa;
    private String grau;
    private Integer gg;
    private Integer mm;
    private Integer ss;
}
