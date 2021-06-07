package br.itarocha.cartanatal.core.model.domain;

import br.itarocha.cartanatal.core.util.Funcoes;

public class CuspideCasa {
	private int numero;
	private double posicao;
	private EnumSigno enumSigno;
	private String grau;
	private String grauNaCasa;
	private String gnc;
	private String g;
	private String m;
	private String s;
	
	public CuspideCasa(int numero, double posicao) {
		
		this.numero = numero;
		this.posicao = posicao;
		this.setGrau(Funcoes.grau(posicao));
		this.setGrauNaCasa(Funcoes.grauNaCasa(posicao));
		int signo = (int)(posicao / 30); 
		this.enumSigno = EnumSigno.getByCodigo(signo);
	}

	public int getNumero() {
		return numero;
	}
	
	public double getPosicao() {
		return posicao;
	}
	
	public EnumSigno getEnumSigno() {
		return enumSigno;
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
