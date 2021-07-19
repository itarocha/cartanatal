package br.itarocha.cartanatal.core.strategy;

import java.text.MessageFormat;

public class Divisao implements Calculo{
    @Override
    public void calcular(int a, int b) {
        System.out.println(MessageFormat.format("A divisão entre {0} e {1} é {2}", a, b, ((double)(a/b))));
    }
}
