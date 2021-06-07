package br.itarocha.cartanatal.core.model.domain;

public class PlanetaAspecto {
	private EnumPlaneta enumPlaneta;
	private int coordenada;
	private double posicao;
	private String grau;
	
	public EnumPlaneta getEnumPlaneta() {
		return enumPlaneta;
	}

	public void setEnumPlaneta(EnumPlaneta enumPlaneta) {
		this.enumPlaneta = enumPlaneta;
	}

	public int getCoordenada() {
		return coordenada;
	}
	
	public void setCoordenada(int coordenada) {
		this.coordenada = coordenada;
	}
	
	public double getPosicao() {
		return posicao;
	}
	
	public void setPosicao(double posicao) {
		this.posicao = posicao;
	}
	
	public String getGrau() {
		return grau;
	}
	
	public void setGrau(String grau) {
		this.grau = grau;
	}
}
