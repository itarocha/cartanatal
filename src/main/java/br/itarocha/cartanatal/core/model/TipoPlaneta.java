package br.itarocha.cartanatal.core.model;

public enum TipoPlaneta {

    XXX(".Descrição."),
    SOL("Sol"),
    LUA("Lua"),
    MER("Mercúrio"),
    VEN("Vênus"),
    MAR("Marte"),
    JUP("Júpiter"),
    SAT("Saturno"),
    URA("Urano"),
    NET("Netuno"),
    PLU("Plutão"),
    ASC("Ascendente"),
    DES("Descencente"),
    MCE("Meio do Céu"),
    NAD("Nadir"),
    NOR("Nódulo Norte"),
    SUL("Nódulo Sul");
	
	private String descricao;
	
	TipoPlaneta(String descricao){
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
	public static String getByString(String texto) {
		for (TipoPlaneta t: TipoPlaneta.values()) {
			if (t.toString().equalsIgnoreCase(texto)) {
				return t.getDescricao();
			}
		}
		return texto;
	}

	static public boolean isMember(String texto) {
		TipoPlaneta[] valores = TipoPlaneta.values();
		for (TipoPlaneta valor : valores)
			if (valor.name().equals(texto))
				return true;
		return false;
	}
	
}
