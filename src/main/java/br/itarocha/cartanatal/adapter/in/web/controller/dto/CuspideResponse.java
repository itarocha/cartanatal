package br.itarocha.cartanatal.adapter.in.web.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CuspideResponse {
    private Integer casa;
    private String signo;
    private String grau;
    private BigDecimal angulo;
    private Integer gg;
    private Integer mm;
    private Integer ss;
    private String descricao;
}
