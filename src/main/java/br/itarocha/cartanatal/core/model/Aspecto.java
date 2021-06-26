package br.itarocha.cartanatal.core.model;

public class Aspecto {
	
	private Long id;
	private TipoPlaneta planetaOrigem;
	private TipoPlaneta planetaDestino;
	private TipoAspecto aspecto;
	private String texto;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoPlaneta getPlanetaOrigem() {
		return planetaOrigem;
	}

	public void setPlanetaOrigem(TipoPlaneta planetaOrigem) {
		this.planetaOrigem = planetaOrigem;
	}

	public TipoPlaneta getPlanetaDestino() {
		return planetaDestino;
	}

	public void setPlanetaDestino(TipoPlaneta planetaDestino) {
		this.planetaDestino = planetaDestino;
	}

	public TipoAspecto getAspecto() {
		return aspecto;
	}

	public void setAspecto(TipoAspecto aspecto) {
		this.aspecto = aspecto;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}
}
