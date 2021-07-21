package br.itarocha.cartanatal.core.util.horarioverao;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class RegraHorarioVerao {

    private LocalDateTime dataHoraInicial;
    private LocalDateTime dataHoraFinal;

    @Builder.Default
    private List<UfEnum> incluidos = new ArrayList<>();

    @Builder.Default
    private List<UfEnum> excluidos = new ArrayList<>();
}
