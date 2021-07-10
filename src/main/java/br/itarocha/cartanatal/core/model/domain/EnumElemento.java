package br.itarocha.cartanatal.core.model.domain;

import java.util.Arrays;

public enum EnumElemento {
	FOGO( "fg", "Fogo"),
	TERRA("te", "Terra"),
	AGUA("ag", "Água"),
	AR( "ar", "Ar");

	private String sigla;
	private String nome;

	EnumElemento(String sigla, String nome){
		this.sigla = sigla;
		this.nome = nome;
	}

	public String getSigla() {
		return sigla;
	}

	public String getNome() {
		return nome;
	}

	public static EnumElemento getBySigla(String sigla) {
		return Arrays.stream(EnumElemento.values()).filter(x -> x.getSigla().equalsIgnoreCase(sigla.toLowerCase()))
				.findFirst().orElse(EnumElemento.FOGO);
	}
			
}
