package br.itarocha.cartanatal.core.util.horarioverao;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//https://pt.wikipedia.org/wiki/Lista_de_per%C3%ADodos_em_que_vigorou_o_hor%C3%A1rio_de_ver%C3%A3o_no_Brasil
@Component
public class HorarioVerao {
    private List<RegraHorarioVerao> periodos = new ArrayList<>();

    public HorarioVerao(){
        // 1986 / 1987
        periodos.add(RegraHorarioVerao.builder()
                .dataHoraInicial(LocalDateTime.of(1986,10,25,0,0,0))
                .dataHoraFinal(LocalDateTime.of(1987,2,14,0,0,0))
                .incluidos(UfEnum.getAll())
                .build());

        // 1987 / 1988
        periodos.add(RegraHorarioVerao.builder()
                .dataHoraInicial(LocalDateTime.of(1987,10,25,0,0,0))
                .dataHoraFinal(LocalDateTime.of(1988,2,7,0,0,0))
                .incluidos(UfEnum.getAll())
                .build());

        //1988/1989	0h de 16 de outubro de 1988, à 0h de 29 de janeiro de 1989	Regiões Sul, Sudeste, Centro-Oeste e Nordeste
        RegraHorarioVerao regras1988a1989 = RegraHorarioVerao.builder()
                .dataHoraInicial(LocalDateTime.of(1988,10,16,0,0,0))
                .dataHoraFinal(LocalDateTime.of(1989,1,29,0,0,0))
                .build();
        regras1988a1989.getIncluidos().addAll(UfEnum.getUfs(RegiaoEnum.SUL));
        regras1988a1989.getIncluidos().addAll(UfEnum.getUfs(RegiaoEnum.SUDESTE));
        regras1988a1989.getIncluidos().addAll(UfEnum.getUfs(RegiaoEnum.CENTRO_OESTE));
        regras1988a1989.getIncluidos().addAll(UfEnum.getUfs(RegiaoEnum.NORDESTE));
        periodos.add(regras1988a1989);

        // 1989/1990	0h de 15 de outubro de 1989, à 0h de 11 de fevereiro de 1990	Regiões Sul, Sudeste, Centro-Oeste, Nordeste e Ilhas Oceânicas
        RegraHorarioVerao regras1989a1990 = RegraHorarioVerao.builder()
                .dataHoraInicial(LocalDateTime.of(1989,10,15,0,0,0))
                .dataHoraFinal(LocalDateTime.of(1990,2,11,0,0,0))
                .build();
        regras1989a1990.getIncluidos().addAll(UfEnum.getUfs(RegiaoEnum.SUL));
        regras1989a1990.getIncluidos().addAll(UfEnum.getUfs(RegiaoEnum.SUDESTE));
        regras1989a1990.getIncluidos().addAll(UfEnum.getUfs(RegiaoEnum.CENTRO_OESTE));
        regras1989a1990.getIncluidos().addAll(UfEnum.getUfs(RegiaoEnum.NORDESTE));
        periodos.add(regras1989a1990);

    }

    public boolean isHorarioVerao(String uf, LocalDateTime dataHora){
        UfEnum ufEnum = UfEnum.valueOf(uf);

        Optional<RegraHorarioVerao> opt = periodos.stream()
                .filter(regra -> {
                            boolean naData = (dataHora.isAfter(regra.getDataHoraInicial()) && dataHora.isBefore(regra.getDataHoraFinal()))
                                              || dataHora.isEqual(regra.getDataHoraInicial()) || dataHora.isEqual(regra.getDataHoraFinal());

                            boolean contido = regra.getIncluidos().contains(ufEnum);
                            boolean naoContido = !regra.getExcluidos().contains(ufEnum);

                            return naData && contido && naoContido;
                        }
                )
                .findFirst();

        return opt.isPresent();
    }

}
