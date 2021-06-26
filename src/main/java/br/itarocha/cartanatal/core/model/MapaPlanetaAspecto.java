package br.itarocha.cartanatal.core.model;

public class MapaPlanetaAspecto {

	private Long id;
	private TipoPlaneta planetaOrigem;
	private TipoPlaneta planetaDestino;
	private TipoAspecto aspecto;
	private TipoRelacao tipoRelacao;
	private TipoLogico conferido;
	private Long aspectoMestre;
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

	public TipoRelacao getTipoRelacao() {
		return tipoRelacao;
	}

	public void setTipoRelacao(TipoRelacao tipoRelacao) {
		this.tipoRelacao = tipoRelacao;
	}

	public TipoLogico getConferido() {
		return conferido;
	}

	public void setConferido(TipoLogico conferido) {
		this.conferido = conferido;
	}

	public Long getAspectoMestre() {
		return aspectoMestre;
	}

	public void setAspectoMestre(Long aspectoMestre) {
		this.aspectoMestre = aspectoMestre;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}
}
