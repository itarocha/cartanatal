package br.itarocha.cartanatal.core.model.presenter;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ParagrafoImagem {
    private String fileName;
    private int width;
    private int height;
}
