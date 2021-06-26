package br.itarocha.cartanatal.core.model;

public class Casa {
	private static final String[] casas = {"Primeira", "Segunda", "Terceira", "Quarta", "Quinta", "Sexta", "Sétima", "Oitava", "Nona", "Décima", "Décima Primeira", "Décima Segunda"};
	
	public static String getByNumero(int casa) {
		if (casa >= 1 && casa <= 12) {
			return casas[casa-1];
		}
		return Integer.toString(casa);
	}
}
