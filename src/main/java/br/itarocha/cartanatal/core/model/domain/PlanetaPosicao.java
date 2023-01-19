package br.itarocha.cartanatal.core.model.domain;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class PlanetaPosicao {
	private EnumPlaneta enumPlaneta;
	private EnumSigno enumSigno;
	private BigDecimal angulo;
	private String grau;
	private String grauNaCasa;
	private double posicao;
	private boolean retrogrado;
	private String statusRetrogrado;
	private double latitude;
	private double distancia;
	private double direcao;
	private String gnc;
	private String g;
	private String m;
	private String s;
	private double casaDouble;
	private Integer casa;
}
