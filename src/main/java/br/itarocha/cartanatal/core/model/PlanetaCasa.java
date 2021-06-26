package br.itarocha.cartanatal.core.model;

public class PlanetaCasa {
	
	private Long id;
	private TipoPlaneta planeta;
	private Integer casa;
	private TipoLogico conferido;
	private String texto;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoPlaneta getPlaneta() {
		return planeta;
	}

	public void setPlaneta(TipoPlaneta planeta) {
		this.planeta = planeta;
	}

	public Integer getCasa() {
		return casa;
	}

	public void setCasa(Integer casa) {
		this.casa = casa;
	}

	public TipoLogico getConferido() {
		return conferido;
	}

	public void setConferido(TipoLogico conferido) {
		this.conferido = conferido;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}
}
