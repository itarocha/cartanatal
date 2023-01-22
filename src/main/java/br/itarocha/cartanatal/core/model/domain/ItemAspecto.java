package br.itarocha.cartanatal.core.model.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ItemAspecto {
	private String planetaOrigem;
	private String planetaDestino;
	private double planetaOrigemAngulo;
	private double planetaDestinoAngulo;
	private EnumAspecto aspecto;
	private double orbe;
	private int orbeGrau;
	private int orbeMinuto;
	private String orbeDescricao;
	private String flag;
	private int x;
	private int y;
}
