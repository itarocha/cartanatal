package br.itarocha.cartanatal.core.model.domain;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class CuspideCasa {
	private int numero;
	private EnumSigno enumSigno;
	private Localizacao localizacao;
	/*
	private double posicao;
	private BigDecimal angulo;
	private String grau;
	private String grauNaCasa;
	private String gnc;
	private String g;
	private String m;
	private String s;
	 */
}
