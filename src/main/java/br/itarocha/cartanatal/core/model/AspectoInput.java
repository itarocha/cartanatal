package br.itarocha.cartanatal.core.model;

import javax.validation.constraints.NotNull;

public class AspectoInput {
	
	@NotNull(message="Planeta de Origem é obrigatório")
	private String planetaOrigem;

	@NotNull(message="Planeta Destino é obrigatório")
	private String planetaDestino;
	
	@NotNull(message="Tipo de Aspecto é obrigatório")
	private String aspecto;

	@NotNull(message="Texto é obrigatório")
	private String texto;
	
	private String aspectoMestre;

	
	public String getPlanetaOrigem() {
		return planetaOrigem;
	}

	public void setPlanetaOrigem(String planetaOrigem) {
		this.planetaOrigem = planetaOrigem;
	}

	public String getPlanetaDestino() {
		return planetaDestino;
	}

	public void setPlanetaDestino(String planetaDestino) {
		this.planetaDestino = planetaDestino;
	}

	public String getAspecto() {
		return aspecto;
	}

	public void setAspecto(String aspecto) {
		this.aspecto = aspecto;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getAspectoMestre() {
		return aspectoMestre;
	}

	public void setAspectoMestre(String aspectoMestre) {
		this.aspectoMestre = aspectoMestre;
	}
	
}
