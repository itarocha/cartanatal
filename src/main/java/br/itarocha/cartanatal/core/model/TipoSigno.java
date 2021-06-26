package br.itarocha.cartanatal.core.model;

import java.util.Arrays;

public enum TipoSigno {

    XX(-1, "xx", "x", "*** Descrição ***"),
	AR(0, "ar", "a", "Áries"),
	TO(1, "to", "b", "Touro"),
	GE(2, "ge", "c", "Gêmeos"),
	CA(3, "ca", "d", "Câncer"),
	LE(4, "le", "e", "Leão"),
	VI(5, "vi", "f", "Virgem"),
	LI(6, "li", "g", "Libra"),
	ES(7, "es", "h", "Escorpião"),
	SG(8, "sg", "i", "Sagitário"),
	CP(9, "cp", "j", "Capricórnio"),
	AQ(10, "aq", "k", "Aquário"),
	PE(11, "pe", "l", "Peixes");

/*
	AR(0, "ar", "a", "Áries"),
	TO(1, "to", "b", "Touro"),
	GE(2, "ge", "c", "Gêmeos"),
	CA(3, "ca", "d", "Câncer"),
	LE(4, "le", "e", "Leão"),
	VI(5, "vi", "f", "Virgem"),
	LI(6, "li", "g", "Libra"),
	ES(7, "es", "h", "Escorpião"),
	SG(8, "sg", "i", "Sagitário"),
	CP(9, "cp", "j", "Capricórnio"),
	AQ(10, "aq", "k", "Aquário"),
	PE(11, "pe", "l", "Peixes");

*/
/*
	private String descricao;
	
	TipoSigno(String descricao){
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return this.descricao;
	}


	public static String getByString(String texto) {
		for (TipoSigno t: TipoSigno.values()) {
			if (t.toString().equalsIgnoreCase(texto)) {
				return t.getDescricao();
			}
		}
		return texto;
	}
*/

	private Integer codigo;
	private String sigla;
	private String letra;
	private String descricao;

	TipoSigno(Integer codigo, String sigla, String letra, String descricao){
		this.codigo = codigo;
		this.sigla = sigla;
		this.letra = letra;
		this.descricao = descricao;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public String getSigla() {
		return sigla;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getLetra() {
		return letra;
	}

	public static TipoSigno getByCodigo(Integer codigo) {
		return Arrays.stream(TipoSigno.values()).filter(x -> x.getCodigo().equals(codigo))
				.findFirst().orElse(TipoSigno.AR);
	}

	public static TipoSigno getBySigla(String sigla) {
		return Arrays.stream(TipoSigno.values()).filter(x -> x.getSigla().equalsIgnoreCase(sigla.toLowerCase()))
				.findFirst().orElse(TipoSigno.AR);
	}

	public static String getByString(String texto) {
		for (TipoSigno t: TipoSigno.values()) {
			if (t.toString().equalsIgnoreCase(texto)) {
				return t.getDescricao();
			}
		}
		return texto;
	}
}
