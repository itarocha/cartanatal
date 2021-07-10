package br.itarocha.cartanatal.core.model.domain;

import java.util.Arrays;

public enum EnumSigno {
	AR(0, "ar", "Ar", "a", "Áries", EnumElemento.FOGO, EnumQualidade.CARDINAL, EnumPolaridade.POSITIVA),
	TO(1, "to", "To", "b", "Touro", EnumElemento.TERRA, EnumQualidade.FIXO, EnumPolaridade.NEGATIVA),
	GE(2, "ge", "Ge","c", "Gêmeos", EnumElemento.AR, EnumQualidade.MUTAVEL, EnumPolaridade.POSITIVA),
	CA(3, "ca", "Ca","d", "Câncer", EnumElemento.AGUA, EnumQualidade.CARDINAL, EnumPolaridade.NEGATIVA),

	LE(4, "le", "Le","e", "Leão", EnumElemento.FOGO, EnumQualidade.FIXO, EnumPolaridade.POSITIVA),
	VI(5, "vi", "Vi","f", "Virgem", EnumElemento.TERRA, EnumQualidade.MUTAVEL, EnumPolaridade.NEGATIVA),
	LI(6, "li", "Li","g", "Libra", EnumElemento.AR, EnumQualidade.CARDINAL, EnumPolaridade.POSITIVA),
	ES(7, "es", "Es","h", "Escorpião", EnumElemento.AGUA, EnumQualidade.FIXO, EnumPolaridade.NEGATIVA),

	SG(8, "sg", "Sg","i", "Sagitário", EnumElemento.FOGO, EnumQualidade.MUTAVEL, EnumPolaridade.POSITIVA),
	CP(9, "cp", "Cp","j", "Capricórnio", EnumElemento.TERRA, EnumQualidade.CARDINAL, EnumPolaridade.NEGATIVA),
	AQ(10, "aq", "Aq","k", "Aquário", EnumElemento.AR, EnumQualidade.FIXO, EnumPolaridade.POSITIVA),
	PE(11, "pe", "Pe","l", "Peixes", EnumElemento.AGUA, EnumQualidade.MUTAVEL, EnumPolaridade.NEGATIVA);
	
	private Integer codigo;
	private String sigla;
	private String siglaCapitalized;
	private String letra;
	private String nome;
	private EnumElemento elemento;
	private EnumQualidade qualidade;
	private EnumPolaridade polaridade;
	
	EnumSigno(Integer codigo,
			  String sigla,
			  String siglaCapitalized,
			  String letra,
			  String nome,
			  EnumElemento elemento,
			  EnumQualidade qualidade,
			  EnumPolaridade polaridade)
	{
		this.codigo = codigo;
		this.sigla = sigla;
		this.siglaCapitalized = siglaCapitalized;
		this.letra = letra;
		this.nome = nome;
		this.elemento = elemento;
		this.qualidade = qualidade;
		this.polaridade = polaridade;
	}

	public Integer getCodigo() {
		return this.codigo;
	}

	public String getSigla() {
		return this.sigla;
	}

	public String getSiglaCapitalized() {
		return this.siglaCapitalized;
	}

	public String getNome() {
		return this.nome;
	}

	public String getLetra() {
		return this.letra;
	}

	public EnumElemento getElemento(){
		return this.elemento;
	}

	public EnumQualidade getQualidade(){
		return this.qualidade;
	}

	public EnumPolaridade getPolaridade(){
		return this.polaridade;
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
