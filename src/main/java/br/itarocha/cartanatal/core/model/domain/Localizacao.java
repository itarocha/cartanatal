package br.itarocha.cartanatal.core.model.domain;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class Localizacao {
    private double posicao;
    private BigDecimal angulo;
    private String grau;
    private String grauNaCasa;
    private String gnc;
    private String g;
    private String m;
    private String s;
}