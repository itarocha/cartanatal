package br.itarocha.cartanatal.core.model.domain;

import java.util.Arrays;

public enum EnumPolaridade {
	POSITIVA( "pos", "Positiva"),
	NEGATIVA("neg", "Negativa");

	private String sigla;
	private String nome;

	EnumPolaridade(String sigla, String nome){
		this.sigla = sigla;
		this.nome = nome;
	}

	public String getSigla() {
		return sigla;
	}

	public String getNome() {
		return nome;
	}

	public static EnumPolaridade getBySigla(String sigla) {
		return Arrays.stream(EnumPolaridade.values()).filter(x -> x.getSigla().equalsIgnoreCase(sigla.toLowerCase()))
				.findFirst().orElse(EnumPolaridade.POSITIVA);
	}
			
}
