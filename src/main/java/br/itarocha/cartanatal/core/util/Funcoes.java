package br.itarocha.cartanatal.core.util;

import br.itarocha.cartanatal.core.model.domain.EnumAspecto;

import java.text.Normalizer;
import java.util.TimeZone;

public class Funcoes {

	// Retorna o TimeZone de horas (-3) retorna "GMT-3:00"
	public static TimeZone timeZone(int fuso) {
		String tmz = "";
		if (fuso > 0) {
			tmz = String.format("GMT+%d:00", fuso);
		} else {
			tmz = String.format("GMT%d:00", fuso);
		}
		return TimeZone.getTimeZone(tmz);
	}
	
	public static String removerAcentos(String acentuada) {
		CharSequence cs = new StringBuilder(acentuada);
		return Normalizer.normalize(cs, Normalizer.Form.NFKD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}
	
	public static String grauNaCasa(double d){
		//return grau(d % 30);

		int[] g = grauToArray(d % 30);
		//return String.format("%3d� %02d' %02.0f\"", g[0], g[1], g[2]);
		return String.format("%02d.%02d.%02d", g[0], g[1], g[2]);
	}
	
	public static String grau(double d) {
		int[] g = grauToArray(d);
		//return String.format("%3d� %02d' %02.0f\"", g[0], g[1], g[2]);
		return String.format("%03d.%02d.%02d", g[0], g[1], g[2]);
	}

	public static int[] grauToArray(double d){
		int[] retorno = new int[3];

		d = Math.abs(d);
		d += 0.5/3600./10000.;	// round to 1/1000 of a second
		int deg = (int) d;
		d = (d - deg) * 60;
		int min = (int) d;
		d = (d - min) * 60;
		int sec = (int)d;

		retorno[0] = deg;
		retorno[1] = min;
		retorno[2] = sec;
		
		return retorno;
	}
	
    public static String signGlyphFromIndex(int i) {
        String c;
        switch (i) {
            case 0: c = "AR"; break;
            case 1: c = "l"; break;
            case 2: c = "m"; break;
            case 3: c = "n"; break;
            case 4: c = "o"; break;
            case 5: c = "p"; break;
            case 6: c = "q"; break;
            case 7: c = "r"; break;
            case 8: c = "s"; break;
            case 9: c = "t"; break;
            case 10: c = "u"; break;
            case 11: c = "v"; break;
            default: c = "-"; break;
        }
        return c;
    }

	public static EnumAspecto buildAspect(String planetaA, String planetaB, double a, double b){
		if (Math.abs(b-a) > 190) {
			if (b < a) b += 360;
		}
		double resultado = Math.abs(b-a);

		EnumAspecto aspecto = EnumAspecto.getByAngulo(resultado);
		/*
		System.out.println(String.format("%s - %s [%s] %s %s => %s",planetaA, planetaB,
				aspecto == null ? "--" : aspecto.getSigla(),
				new DecimalFormat("000.0000").format(a),
				new DecimalFormat("000.0000").format(b),
				new DecimalFormat("000.0000").format(resultado)
		));
		 */
		return aspecto;
	}
    
}
