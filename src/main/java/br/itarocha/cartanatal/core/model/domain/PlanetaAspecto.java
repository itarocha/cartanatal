package br.itarocha.cartanatal.core.model.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PlanetaAspecto {
	private EnumPlaneta enumPlaneta;
	private int coordenada;
	private double posicao;
	private String grau;
}
