package br.itarocha.cartanatal.core.model.presenter;

import br.itarocha.cartanatal.core.model.Pair;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Paragrafo {
    private EstiloParagrafo estilo;
    private String texto;
    private List<Pair> tabela;
    private ParagrafoImagem imagem;
}
