package br.itarocha.cartanatal.core.model.domain;

import java.util.Arrays;

public enum EnumAspecto {

	CJ("cj","m","Conjunção"),
	OP("op","n","Oposição" ),
	QD("qd","o","Quadratura"),
	TG("tg","p","Trígono"),
	SX("sx","q","Sextil");
	
	private String sigla;
	private String letra;
	private String nome;
	
	EnumAspecto(String sigla, String letra, String nome){
		this.sigla = sigla;
		this.letra = letra;
		this.nome = nome;
	}

	public String getSigla() {
		return sigla;
	}

	public String getNome() {
		return nome;
	}

	public String getLetra() {
		return letra;
	}
	
	public static EnumAspecto getBySigla(String sigla) {
		return Arrays.stream(EnumAspecto.values()).filter(x -> x.getSigla().equalsIgnoreCase(sigla.toLowerCase()))
				.findFirst()
				.orElse(EnumAspecto.CJ);
	}
			
}
