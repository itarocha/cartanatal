package br.itarocha.cartanatal.core.decorator;

public class ItemDesenhoMapa implements Comparable<ItemDesenhoMapa>{
	
	private String texto;
	private Integer grau360;
	private Integer grauNaCasa;
	private Integer minuto;
	
	public ItemDesenhoMapa(String texto, Integer grau360, Integer grauNaCasa, Integer mm) {
		this.texto = texto;
		this.grau360 = grau360;
		this.grauNaCasa = grauNaCasa;
		this.minuto = mm;
	}

	public String getTexto() {
		return texto;
	}

	public Integer getGrau360() {
		return grau360;
	}

	public Integer getGrauNaCasa() {
		return grauNaCasa;
	}

	public Integer getMinuto() {
		return minuto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public void setGrau360(Integer grau360) {
		this.grau360 = grau360;
	}

	public void setGrauNaCasa(Integer grauNaCasa) {
		this.grauNaCasa = grauNaCasa;
	}

	public void setMinuto(Integer minuto) {
		this.minuto = minuto;
	}
	
	public int compareTo(ItemDesenhoMapa outro) {
		String esseGrau = String.format("%03d%02d", this.grau360, this.minuto);
		String outroGrau = String.format("%03d%02d", outro.grau360, outro.minuto);
		return esseGrau.compareTo(outroGrau);
    }
	
	public String toString() {
		return String.format("%03d.%02d - %s", this.grau360, this.minuto, this.texto);
	}

}
