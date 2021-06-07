package br.itarocha.cartanatal.core.model.domain;

import java.util.Arrays;

public enum EnumPlaneta {
	SOL(0, "sol", "A", "Sol"),
	LUA(1, "lua", "B", "Lua"),
	MER(2, "mer", "C", "Mercúrio"),
	VEN(3, "ven", "D", "Vênus"),
	MAR(4, "mar", "E", "Marte"),
	JUP(5, "jup", "F", "Júpiter"),
	SAT(6, "sat", "G", "Saturno"),
	URA(7, "ura", "H", "Urano"),
	NET(8, "net", "I", "Netuno"),
	PLU(9, "plu", "J", "Plutão"),
	ASC(10, "asc", "P", "Ascendente"),
	MCE(11, "mce", "Q", "Meio do Céu");

	//TND(10, "tnd", "L", "Nodo Norte"),
	//SND("snd", "M", "Nodo Sul");
	
	private Integer codigo;
	private String sigla;
	private String letra;
	private String nome;
	
	EnumPlaneta(Integer codigo, String sigla, String letra, String nome){
		this.codigo = codigo;
		this.sigla = sigla;
		this.letra = letra;
		this.nome = nome;
	}

	public Integer getCodigo() {
		return this.codigo;
	}

	public String getSigla() {
		return sigla;
	}

	public String getNome() {
		return nome;
	}

	public String getLetra() {
		return letra;
	}

	public static EnumPlaneta getBySigla(String sigla) {
		return Arrays.stream(EnumPlaneta.values())
				.filter(x -> x.getSigla().equalsIgnoreCase(sigla.toLowerCase()))
				.findFirst()
				.orElse(EnumPlaneta.SOL);
	}

	public static EnumPlaneta getByCodigo(Integer codigo) {
		return Arrays.stream(EnumPlaneta.values())
				.filter(x -> x.getCodigo().equals(codigo))
				.findFirst()
				.orElse(EnumPlaneta.SOL);
	}
}
