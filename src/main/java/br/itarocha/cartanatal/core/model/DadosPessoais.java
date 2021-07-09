package br.itarocha.cartanatal.core.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@Builder
public class DadosPessoais {

    @NotEmpty
    private String nome;

    @NotEmpty
    private LocalDateTime dataHoraNascimento;

    @NotEmpty
    private String cidade;

    @NotEmpty
    private String uf;

}
