package br.itarocha.cartanatal.core.model.interpretacao;

import br.itarocha.cartanatal.core.model.domain.EnumQualidade;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MapaQualidade {
    private EnumQualidade qualidade;
    private String signos;
    private String casas;
    private String palavrasChave;
    private String personalidade;
}
