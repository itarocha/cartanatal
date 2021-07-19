package br.itarocha.cartanatal.core.strategy;

import org.springframework.stereotype.Component;

import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;

@Component
public class CalculoFactory {
    List<Calculo> lista = new ArrayList<>();

    public CalculoFactory(){
        lista.add(new Soma());
        lista.add(new Subtracao());
        lista.add(new Multiplicacao());
        lista.add(new Divisao());
    }

    public Calculo getStrategy(Class<? extends Calculo> classe){
        return lista.stream().filter(s -> classe.equals(s.getClass())).findFirst().orElse(null);
    }


    public static void main(String[] args) {
        CalculoFactory cf = new CalculoFactory();
        Calculo c = cf.getStrategy(Divisao.class);

        if (c != null){
            c.calcular(21, 7);
        }
    }
}
