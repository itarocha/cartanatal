package br.itarocha.cartanatal.core.model.domain;

import lombok.Data;

@Data
public class Cidade {
	private int codigo;
	private String nomeOriginal;
	private String nomeSemAcento;
	private String uf;
	private String latitude;
	private String longitude;
	private Integer fuso;
	private String key;

	@Override
	public String toString() {
		return this.getNomeOriginal() + " - " + this.getUf()+" Fuso = "+this.getFuso();
	}
}
