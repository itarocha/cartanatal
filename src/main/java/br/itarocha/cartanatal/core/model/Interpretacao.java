package br.itarocha.cartanatal.core.model;

import java.util.LinkedList;
import java.util.List;

public class Interpretacao {

	private String 	titulo;
	
	private List<String> paragrafos = new LinkedList<>();
	
	public Interpretacao(String titulo) {
		this.titulo = titulo;
	}
	
	public Interpretacao(String titulo, List<String> paragrafos) {
		this(titulo);
		this.paragrafos = paragrafos;
	}

	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public List<String> getParagrafos() {
		return paragrafos;
	}

	public void addParagrafos(String texto) {
		this.paragrafos.add(texto);
	}
}
