package br.itarocha.cartanatal.core.model;

public enum TipoAspecto {

    XX("*** Descrição ***"),
    CJ("Conjunção"),
    TG("Trígono"),
    SX("Sextil"),
    QD("Quadratura"),
    OP("Oposição");
	
	private String descricao;
	
	TipoAspecto(String descricao){
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
	public static String getByString(String texto) {
		for (TipoAspecto t: TipoAspecto.values()) {
			if (t.toString().equalsIgnoreCase(texto)) {
				return t.getDescricao();
			}
		}
		return texto;
	}
	
	static public boolean isMember(String texto) {
		TipoRelacao[] valores = TipoRelacao.values();
		for (TipoRelacao valor : valores)
			if (valor.name().equals(texto))
				return true;
		return false;
	}
	
}
