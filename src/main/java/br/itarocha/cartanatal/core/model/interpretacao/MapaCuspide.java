package br.itarocha.cartanatal.core.model.interpretacao;

import br.itarocha.cartanatal.core.model.TipoSigno;

public class MapaCuspide {
	
	private Long id;
	private TipoSigno signo;
	private Integer casa;
	private String texto;

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

	public Integer getCasa() {
		return casa;
	}

	public void setCasa(Integer casa) {
		this.casa = casa;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}
}
