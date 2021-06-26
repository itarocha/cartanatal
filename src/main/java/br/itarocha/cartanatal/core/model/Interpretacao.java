package br.itarocha.cartanatal.core.model;

import java.util.LinkedList;
import java.util.List;

public class Interpretacao {

	private String 	titulo;
	
	private List<String> textos = new LinkedList<String>();
	
	public Interpretacao(String titulo) {
		this.titulo = titulo;
	}
	
	public Interpretacao(String titulo, List<String> textos) {
		this(titulo);
		this.textos = textos;
	}

	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public List<String> getTextos() {
		return textos;
	}

	public void addTextos(String texto) {
		this.textos.add(texto);
	}
}
