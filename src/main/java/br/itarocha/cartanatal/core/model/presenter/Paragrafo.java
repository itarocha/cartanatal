package br.itarocha.cartanatal.core.model.presenter;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Paragrafo {
    private EstiloParagrafo estilo;
    private String texto;
}
