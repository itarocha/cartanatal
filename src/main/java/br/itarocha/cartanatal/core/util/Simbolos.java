package br.itarocha.cartanatal.core.util;

import java.util.HashMap;
import java.util.Map;

public class Simbolos {
    private static Map<String, Character> charSignos = new HashMap<>();
    private static Map<String, Character> charPlanetas = new HashMap<>();
    private static Map<String, Character> charAspectos = new HashMap<>();

    static {
        charSignos.put("ar", '\u2648');
        charSignos.put("to", '\u2649');
        charSignos.put("ge", '\u264A');
        charSignos.put("ca", '\u264B');
        charSignos.put("le", '\u264C');
        charSignos.put("vi", '\u264D');
        charSignos.put("li", '\u264E');
        charSignos.put("es", '\u264F');
        charSignos.put("sg", '\u2650');
        charSignos.put("cp", '\u2651');
        charSignos.put("aq", '\u2652');
        charSignos.put("pe", '\u2653');

        charPlanetas.put("sol", '\u2609');
        charPlanetas.put("lua", '\u263D');
        charPlanetas.put("mer", '\u263F');
        charPlanetas.put("ven", '\u2640');
        charPlanetas.put("ter", '\u2295');
        charPlanetas.put("mar", '\u2642');
        charPlanetas.put("jup", '\u2643');
        charPlanetas.put("sat", '\u2644');
        charPlanetas.put("ura", '\u2645');
        charPlanetas.put("net", '\u2646');
        charPlanetas.put("plu", '\u2647');
        charPlanetas.put("asc", 'A');
        charPlanetas.put("mce", 'M');

        charAspectos.put("cj", '\u260C');
        charAspectos.put("sx", '\u26B9');
        charAspectos.put("qd", '\u25A1');
        charAspectos.put("tg", '\u25B3');
        charAspectos.put("op", '\u260D');

        //wget -A pdf,jpg -m -p -E -k -K -np https://unicode.org/charts/PDF/
        //wget --accept pdf,jpg --mirror --page-requisites --adjust-extension --convert-links --backup-converted --no-parent https://unicode.org/charts/PDF/
        // https://unicode.org/charts/PDF/ diversas tabelas pdf
    }

    public static Character getSimboloSigno(String key){
        return charSignos.get(key);
    }

    public static Character getSimboloPlaneta(String key){
        return charPlanetas.get(key);
    }

    public static Character getSimboloAspecto(String key){
        return charAspectos.get(key);
    }

    public static void main(String[] args) {
        charSignos.forEach((k, v) -> {
            System.out.println(k + " => "+ v);
        });

        charPlanetas.forEach((k, v) -> {
            System.out.println(k + " => "+ v);
        });

        charAspectos.forEach((k, v) -> {
            System.out.println(k + " => "+ v);
        });
    }
}
