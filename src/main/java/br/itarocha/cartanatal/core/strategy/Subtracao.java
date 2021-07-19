package br.itarocha.cartanatal.core.strategy;

import java.text.MessageFormat;

public class Subtracao implements Calculo{
    @Override
    public void calcular(int a, int b) {
        System.out.println(MessageFormat.format("A subtração de {0} e {1} é {2}", a, b, a-b));
    }
}
