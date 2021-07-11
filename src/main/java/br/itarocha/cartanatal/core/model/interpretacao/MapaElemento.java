package br.itarocha.cartanatal.core.model.interpretacao;

import br.itarocha.cartanatal.core.model.domain.EnumElemento;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MapaElemento {
    private EnumElemento elemento;
    private String signos;
    private String planetas;
    private String tipo;
    private String polaridade;
    private String palavrasChave;
    private String fisiologia;
    private String aparencia;
    private String temperamento;
    private String associacoes;
    private String estiloVida;
    private String textoEstiloVida;
    private String textoEquilibrio;
    private String textoDesequilibrio;
}
