package br.itarocha.cartanatal.core.model.domain;

import java.util.Arrays;

// Símbolos
// https://en.wikipedia.org/wiki/Astrological_symbols
public enum EnumAspecto {

	CJ("cj","m","Conjunção", 0, 9, "\u260C"),
	OP("op","n","Oposição", 180, 8, "\u260D" ),
	QD("qd","o","Quadratura", 90, 10, "\u25A1"),
	TG("tg","p","Trígono", 120, 9, "\u25B3"),
	SX("sx","q","Sextil", 60, 6, "\u26B9");
	
	private String sigla;
	private String letra;
	private String nome;
	private String unicode;
	private int grau;
	private int orbita;
	private int rangeIni;
	private int rangeFim;
	
	EnumAspecto(String sigla, String letra, String nome, int grau, int orbita, String unicode){
		this.sigla = sigla;
		this.letra = letra;
		this.nome = nome;
		this.grau = grau;
		this.orbita = orbita;
		this.unicode = unicode;
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

	public String getUnicode() { return this.unicode; }

	public static EnumAspecto getBySigla(String sigla) {
		return Arrays.stream(EnumAspecto.values())
				.filter(x -> x.getSigla().equalsIgnoreCase(sigla.toLowerCase()))
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
