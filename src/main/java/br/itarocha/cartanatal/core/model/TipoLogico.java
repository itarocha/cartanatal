package br.itarocha.cartanatal.core.model;

public enum TipoLogico {

    S("Sim"),
    N("Não");
	
	private String descricao;
	
	TipoLogico(String descricao){
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
	public static String getByString(String texto) {
		for (TipoLogico t: TipoLogico.values()) {
			if (t.toString().equalsIgnoreCase(texto)) {
				return t.getDescricao();
			}
		}
		return texto;
	}
	
}
