package br.itarocha.cartanatal.adapter.in.web.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PlanetaSignoResponse {
    private String planeta;
    private String signo;
    private Integer casa;
    private BigDecimal angulo;
    private String grau;
    private Integer g360;
    private Integer gg;
    private Integer mm;
    private Integer ss;
    private String direcao;
    private String descricao;
}
