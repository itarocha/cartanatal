package br.itarocha.cartanatal.core.model.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PlanetaPosicao {
	private EnumPlaneta enumPlaneta;
	private EnumSigno enumSigno;
	private boolean retrogrado;
	private String statusRetrogrado;
	private double latitude;
	private double distancia;
	private double direcao;
	private double casaDouble;
	private Integer casa;
	private Localizacao localizacao;
}
