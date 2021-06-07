package br.itarocha.cartanatal.core.decorator;

public class Ponto {
	public int angulo;
	public float defasagem;
	public String nome;
	
	public Ponto(int angulo) {
		defasagem = 90;
		this.angulo = angulo;
	}
	
	public Ponto(int angulo, String nome, float defasagem) {
		this.angulo = angulo;
		this.nome = nome;
		this.defasagem = defasagem;
	}
	
	public void setAngulo(int angulo) {
		this.angulo = angulo;
	}
	
	public double getX() {
    	return Math.sin(angulo * Math.PI / 180 - defasagem);
	}
	
	public double getY() {
		return Math.cos(angulo * Math.PI / 180 - defasagem);
	}
	
	
	
}
