package br.itarocha.cartanatal.core.model.domain;

import java.util.Arrays;

public enum EnumAspecto {

	CJ("cj","m","Conjunção", 0, 10),
	OP("op","n","Oposição", 180, 10 ),
	QD("qd","o","Quadratura", 90, 9),
	TG("tg","p","Trígono", 120, 8),
	SX("sx","q","Sextil", 60, 5);
	
	private String sigla;
	private String letra;
	private String nome;
	private int grau;
	private int orbita;
	private int rangeIni;
	private int rangeFim;
	
	EnumAspecto(String sigla, String letra, String nome, int grau, int orbita){
		this.sigla = sigla;
		this.letra = letra;
		this.nome = nome;
		this.grau = grau;
		this.orbita = orbita;
		this.rangeIni = grau-orbita;
		this.rangeFim = grau+orbita;
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

	public int getGrau() { return this.grau; }

	public int getOrbita() { return this.orbita; }

	public static EnumAspecto getBySigla(String sigla) {
		return Arrays.stream(EnumAspecto.values()).filter(x -> x.getSigla().equalsIgnoreCase(sigla.toLowerCase()))
				.findFirst()
				.orElse(EnumAspecto.CJ);
	}

	public static EnumAspecto getByAngulo(double angulo) {
		int intAngulo = ((int) angulo);
		return Arrays.stream(EnumAspecto.values())
				.filter(x -> (intAngulo >= x.rangeIni) && (intAngulo <= x.rangeFim))
				.findFirst()
				.orElse(null);
	}

}
