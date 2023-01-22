package br.itarocha.cartanatal.core.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Aspecto {
	private Long id;
	private TipoPlaneta planetaOrigem;
	private TipoPlaneta planetaDestino;
	private TipoAspecto aspecto;
	private String texto;
}
