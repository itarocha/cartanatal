package br.itarocha.cartanatal.core.model.presenter;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DadoPessoal {
    private String nome;
    private String data;
    private String hora;
    private String deltaT; // double
    private String julDay;
    private String lat;
    private String lon;
}
