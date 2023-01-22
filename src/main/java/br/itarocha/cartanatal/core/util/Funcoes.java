package br.itarocha.cartanatal.core.util;

import br.itarocha.cartanatal.core.model.AspectoOrbe;
import br.itarocha.cartanatal.core.model.domain.EnumAspecto;
import br.itarocha.cartanatal.core.model.domain.EnumPlaneta;
import br.itarocha.cartanatal.core.model.domain.ItemAspecto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.util.Objects;
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
		int[] g = grauNaCasaToArray(d);
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

		int g = (int)d;
		double mDouble = (d - g) * 60;
		int m = (int)mDouble;

		double sDouble =  (mDouble - m) * 60;
		int s = (int)Math.round(sDouble);

		retorno[0] = g;
		retorno[1] = m;
		retorno[2] = s;

		return retorno;
	}

	public static int[] grauNaCasaToArray(double d){
		int[] retorno = new int[3];

		int g = (int)d;
		double mDouble = (d - g) * 60;
		int m = (int)mDouble;

		double sDouble =  (mDouble - m) * 60;
		int s = (int)Math.round(sDouble);

		retorno[0] = g % 30;
		retorno[1] = m;
		retorno[2] = s;

		return retorno;
	}

	public static int[] grauNaCasaTruncadoToArray(double d){
		int[] gmc = grauNaCasaToArray(d);
		if (gmc[2] >= 30){
			gmc[2] = 0;
			gmc[1]++;
			if (gmc[1] == 0){
				gmc[0]++;
			}
		}
		if (gmc[1] >= 60){
			gmc[1] = 0;
			gmc[0]++;
		}
		gmc[0] = gmc[0] % 30;
		return gmc;
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

	public static ItemAspecto buildAspect(EnumPlaneta pA, EnumPlaneta pB, double a, double b, int x, int y){
		double minDist = minDistance(a, b);

		String planetaA = pA.getSigla();
		String planetaB = pB.getSigla();

		// Para teste, desmarque
		// _aspectos(planetaA, planetaB, a, b);

		EnumAspecto enumAspecto = EnumAspecto.getByAngulo(minDist);

		if (Objects.nonNull(enumAspecto)){
			double orbe = enumAspecto.getGrau() - minDist;
			int[] gnc = grauNaCasaTruncadoToArray(Math.abs(orbe));

			return ItemAspecto.builder()
					.aspecto(enumAspecto)
					.planetaOrigem(planetaA)
					.planetaDestino(planetaB)
					.planetaOrigemAngulo(a)
					.planetaDestinoAngulo(b)
					.orbe(orbe)
					.orbeGrau(gnc[0])
					.orbeMinuto(gnc[1])
					.orbeDescricao(String.format("%02d°%02d", gnc[0], gnc[1]))
					//.flag()
					.x(x)
					.y(y)
					.build();
		}
		return null;
	}

	private static void _aspectos(String planetaA, String planetaB, double a, double b){
		double minDist = minDistance(a, b);

		AspectoOrbe aspecto = buscarAspecto(minDist);
		if ( !"".equals(aspecto.getAspecto()) ){
			int gnc[] = {0,0,0};
			if (aspecto.getOrbe() != 0.0) {
				gnc = grauNaCasaTruncadoToArray(Math.abs(aspecto.getOrbe()));
			}
			System.out.println(planetaA  + " "+aspecto.getAspecto() + " " + planetaB +
					" " + String.format("%02d°%02d",gnc[0],gnc[1] ) +
					" " + BigDecimal.valueOf(a).setScale(6, RoundingMode.HALF_UP) +
					" " + BigDecimal.valueOf(b).setScale(6, RoundingMode.HALF_UP) +
					" minDist: "+ BigDecimal.valueOf(minDist).setScale(6, RoundingMode.HALF_UP));
		}
	}

	private static AspectoOrbe buscarAspecto(double diff){
		var dif = (int) diff;
		// TODO flag calculando errado
		String flag = "";

		if ( (dif >= 172) && (dif <= 188) ){
			flag = diff < 180 ? "s" : "a";
			return AspectoOrbe.builder()
					.aspecto("OP")
					.orbe(diff - 180)
					.flag(flag)
					.build();
		} else if ( (dif >= 111) && (dif <= 129) ){
			flag = diff > 120 ? "s" : "a";
			return AspectoOrbe.builder()
					.aspecto("TG")
					.orbe(diff - 120)
					.flag(flag)
					.build();
		} else if ( (dif >= 54) && (dif <= 66) ){
			flag = diff < 60 ? "s" : "a";
			return AspectoOrbe.builder()
					.aspecto("SX")
					.orbe(diff - 60)
					.flag(flag)
					.build();
		} else if ( (dif >= 80) && (dif <= 99) ){
			flag = diff < 90 ? "s" : "a";
			return AspectoOrbe.builder()
					.aspecto("QD")
					.orbe(diff - 90)
					.flag(flag)
					.build();
		} else if ( (dif >= -9) && (dif <= 9) ){
			flag = diff > 0 ? "s" : "a";
			return AspectoOrbe.builder()
					.aspecto("CJ")
					.orbe(diff)
					.flag(flag)
					.build();
		}
		return AspectoOrbe.builder().aspecto("").orbe(0).build();
	}

	private static double minDistance(double deg1, double deg2)
	{
		double r;

		r = Math.abs(deg1-deg2);
		return r <= 180.0 ? r : 360.0 - r;
	}

	private static double minDifference(double deg1, double deg2)
	{
		double r;
		r = deg2 - deg1;
		if (Math.abs(r) < 180.0) {
			return r;
		}
		return r >= 0 ? r - 360.0 : r + 360.0;
	}

}
