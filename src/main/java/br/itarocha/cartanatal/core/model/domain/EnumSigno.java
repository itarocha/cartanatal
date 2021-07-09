package br.itarocha.cartanatal.core.model.domain;

import java.util.Arrays;

public enum EnumSigno {
	//SOL(0, "sol", "A", "Sol"),
	
	AR(0, "ar", "Ar", "a", "Áries"),
	TO(1, "to", "To", "b", "Touro"),
	GE(2, "ge", "Ge","c", "Gêmeos"),
	CA(3, "ca", "Ca","d", "Câncer"),
	LE(4, "le", "Le","e", "Leão"),
	VI(5, "vi", "Vi","f", "Virgem"),
	LI(6, "li", "Li","g", "Libra"),
	ES(7, "es", "Es","h", "Escorpião"),
	SG(8, "sg", "Sg","i", "Sagitário"),
	CP(9, "cp", "Cp","j", "Capricórnio"),
	AQ(10, "aq", "Aq","k", "Aquário"),
	PE(11, "pe", "Pe","l", "Peixes");
	
	private Integer codigo;
	private String sigla;
	private String siglaCapitalized;
	private String letra;
	private String nome;
	
	EnumSigno(Integer codigo, String sigla, String siglaCapitalized, String letra, String nome){
		this.codigo = codigo;
		this.sigla = sigla;
		this.siglaCapitalized = siglaCapitalized;
		this.letra = letra;
		this.nome = nome;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public String getSigla() {
		return sigla;
	}

	public String getSiglaCapitalized() {
		return siglaCapitalized;
	}

	public String getNome() {
		return nome;
	}

	public String getLetra() {
		return letra;
	}
	
	public static EnumSigno getByCodigo(Integer codigo) {
		return Arrays.stream(EnumSigno.values()).filter(x -> x.getCodigo().equals(codigo))
				.findFirst().orElse(EnumSigno.AR);
	}
			
	public static EnumSigno getBySigla(String sigla) {
		return Arrays.stream(EnumSigno.values()).filter(x -> x.getSigla().equalsIgnoreCase(sigla.toLowerCase()))
				.findFirst().orElse(EnumSigno.AR);
	}
			
}
