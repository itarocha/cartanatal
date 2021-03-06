package br.itarocha.cartanatal.core.model.presenter;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CuspideResponse {
    private Integer casa;
    private String signo;
    private String grau;
    private Integer gg;
    private Integer mm;
    private Integer ss;
    private String descricao;
}
