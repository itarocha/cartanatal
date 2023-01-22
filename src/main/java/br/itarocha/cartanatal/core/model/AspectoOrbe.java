package br.itarocha.cartanatal.core.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AspectoOrbe {
    private String aspecto;
    private double orbe;
    private String flag;
}
