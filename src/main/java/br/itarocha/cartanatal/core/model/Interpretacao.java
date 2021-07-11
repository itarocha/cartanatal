package br.itarocha.cartanatal.core.model;

import lombok.Builder;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
@Builder
public class Interpretacao {
	private String 	titulo;
	private List<String> paragrafos = new LinkedList<>();
}
