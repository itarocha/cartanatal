package br.itarocha.cartanatal.core.model.interpretacao;

import br.itarocha.cartanatal.core.model.TipoLogico;
import br.itarocha.cartanatal.core.model.TipoPlaneta;
import br.itarocha.cartanatal.core.model.TipoSigno;

public class PlanetaSigno {
	
	private Long id;
	private TipoPlaneta planeta;
	private TipoSigno signo;
	private String texto;
	private TipoLogico conferido;

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

	public TipoSigno getSigno() {
		return signo;
	}

	public void setSigno(TipoSigno signo) {
		this.signo = signo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public TipoLogico getConferido() {
		return conferido;
	}

	public void setConferido(TipoLogico conferido) {
		this.conferido = conferido;
	}
}
