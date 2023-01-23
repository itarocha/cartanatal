package br.itarocha.cartanatal.core.model.domain;

public class Coordenada {
	private int grau;
	private int minuto;
	private int segundo;
	private String sinal;
	private boolean positive;
	
	public Coordenada(int g, int m, int s){
		this.grau = Math.abs(g);
		this.minuto = m;
		this.segundo = s;
		this.sinal = (g < 0) ? "-" : "";
		this.positive = g > 0;
	}

	/*
	public Coordenada(int g, int m, int s, String sinal){
		this.grau = g;
		this.minuto = m;
		this.segundo = s;
		this.sinal = sinal;
	}
	*/

	public int getGrau() {
		return grau;
	}

	public int getMinuto() {
		return minuto;
	}

	public int getSegundo() {
		return segundo;
	}

	public boolean isPositive(){
		return positive;
	}

	public String getSinal() {
		return sinal;
	}

    /// <summary>
    /// Converts a string in one of the formats ddd.mm.ss, dd.mm.ss to a double value,
    /// the value of direction ('+' or '-') defines the sign of he result.
    /// </summary>
    /// <param name="intext">The string to convert</param>
    /// <param name="direction">The direction, positive for north or east, negative for south or west</param>
    /// <returns>Converted value in degrees. If an error occurred: -999</returns>
    public double Coordenada2Graus() {
        double x;
        try {
            double d = this.grau;
            double m = this.minuto;
            double s = this.segundo;
            x = (double)d + m / 60 + s / 3600;
            if (this.sinal.equals("-")) x = -x;
        }
        catch(Exception e) {
            x = -999;  // TODO better error handling
        }
        return x;
    }
	
}