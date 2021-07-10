package br.itarocha.cartanatal.core.model.domain;

import java.util.Arrays;

public enum EnumQualidade {
	CARDINAL( "cd", "Cardinal"),
	FIXO("fx", "Fixo"),
	MUTAVEL("mt", "Mutavel");

	private String sigla;
	private String nome;

	EnumQualidade(String sigla, String nome){
		this.sigla = sigla;
		this.nome = nome;
	}

	public String getSigla() {
		return sigla;
	}

	public String getNome() {
		return nome;
	}

	public static EnumQualidade getBySigla(String sigla) {
		return Arrays.stream(EnumQualidade.values()).filter(x -> x.getSigla().equalsIgnoreCase(sigla.toLowerCase()))
				.findFirst().orElse(EnumQualidade.CARDINAL);
	}
			
}
