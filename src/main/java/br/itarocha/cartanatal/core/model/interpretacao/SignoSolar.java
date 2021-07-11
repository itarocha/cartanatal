package br.itarocha.cartanatal.core.model.interpretacao;

import br.itarocha.cartanatal.core.model.TipoLogico;
import br.itarocha.cartanatal.core.model.TipoSigno;

public class SignoSolar {
	
	private Long id;
	private TipoSigno signo;
	private String descricao;
	private String texto;
	private TipoLogico conferido;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoSigno getSigno() {
		return signo;
	}

	public void setSigno(TipoSigno signo) {
		this.signo = signo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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
