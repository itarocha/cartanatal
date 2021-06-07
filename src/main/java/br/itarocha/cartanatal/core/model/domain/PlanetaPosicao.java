package br.itarocha.cartanatal.core.model.domain;

import br.itarocha.cartanatal.core.util.Funcoes;

public class PlanetaPosicao {
	private EnumPlaneta enumPlaneta;
	private EnumSigno enumSigno;
	private String grau;
	private String grauNaCasa;
	private double posicao;
	private boolean retrogrado;
	private double latitude;
	private double distancia;
	private double direcao;
	private String gnc;
	private String g;
	private String m;
	private String s;
	private double casaDouble;
	private Integer casa;

	public PlanetaPosicao(EnumPlaneta enumPlaneta, double posicao) {
		this.enumPlaneta = enumPlaneta;
		this.posicao = posicao;
		int signo = (int)(posicao / 30);
		this.enumSigno = EnumSigno.getByCodigo(signo);
		this.setGrau( Funcoes.grau(posicao) );
		this.setGrauNaCasa( Funcoes.grauNaCasa(posicao) );
	}
	
	public EnumPlaneta getEnumPlaneta() {
		return enumPlaneta;
	}

	public EnumSigno getEnumSigno() {
		return enumSigno;
	}

	public void setEnumPlaneta(EnumPlaneta enumPlaneta) {
		this.enumPlaneta = enumPlaneta;
	}

	public void setEnumSigno(EnumSigno enumSigno) {
		this.enumSigno = enumSigno;
	}

	public String getGrau() {
		return grau;
	}
	
	private void setGrau(String grau) {
		this.grau = grau;
		String tmp = grau;
		tmp = tmp.replace('.', '-');
		String[] gms = tmp.split("-");
		g = gms[0];
		m = gms[1];
		s = gms[2];
	}

	public String getGrauNaCasa() {
		return grauNaCasa;
	}
	
	private void setGrauNaCasa(String grauNaCasa) {
		this.grauNaCasa = grauNaCasa;
		String tmp = grauNaCasa;
		tmp = tmp.replace('.', '-');
		String[] gms = tmp.split("-");
		gnc = gms[0];
		m = gms[1];
		s = gms[2];
	}
	
	public double getPosicao() {
		return posicao;
	}
	
	public void setPosicao(double posicao) {
		this.posicao = posicao;
	}
	
	public boolean isRetrogrado() {
		return retrogrado;
	}
	
	public void setRetrogrado(boolean retrogrado) {
		this.retrogrado = retrogrado;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public double getDistancia() {
		return distancia;
	}
	
	public void setDistancia(double distancia) {
		this.distancia = distancia;
	}
	
	public double getDirecao() {
		return direcao;
	}
	
	public void setDirecao(double direcao) {
		this.direcao = direcao;
	}
	
	public String getStatusRetrogrado(){
		return this.isRetrogrado() ? "R" : "D";
	}
	
	public double getCasaDouble() {
		return this.casaDouble;
	}
	
	public void setCasaDouble(double casaDouble) {
		this.casaDouble = casaDouble;
		this.casa = (int)casaDouble;
	}
	
	public Integer getCasa() {
		return this.casa;
	}	
	
	public String getGnc() {
		return gnc;
	}
	
	public String getG() {
		return g;
	}
	
	public String getM() {
		return m;
	}
	
	public String getS() {
		return s;
	}

}
