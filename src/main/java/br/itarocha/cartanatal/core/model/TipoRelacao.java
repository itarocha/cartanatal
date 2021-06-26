package br.itarocha.cartanatal.core.model;

public enum TipoRelacao {

    MESTRE("Principal"),
    DETALHE("Dependente");
	
	private String descricao;
	
	TipoRelacao(String descricao){
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
	static public boolean isMember(String texto) {
		TipoRelacao[] valores = TipoRelacao.values();
		for (TipoRelacao valor : valores)
			if (valor.name().equals(texto))
				return true;
		return false;
	}
	
}
