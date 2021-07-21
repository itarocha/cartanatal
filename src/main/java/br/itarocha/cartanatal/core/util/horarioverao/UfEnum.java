package br.itarocha.cartanatal.core.util.horarioverao;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum UfEnum {

    AC(RegiaoEnum.NORTE),
    AL(RegiaoEnum.NORDESTE),
    AP(RegiaoEnum.NORTE),
    AM(RegiaoEnum.NORTE),
    BA(RegiaoEnum.NORDESTE),
    CE(RegiaoEnum.NORDESTE),
    DF(RegiaoEnum.CENTRO_OESTE),
    ES(RegiaoEnum.SUDESTE),
    GO(RegiaoEnum.CENTRO_OESTE),
    MA(RegiaoEnum.NORDESTE),
    MT(RegiaoEnum.CENTRO_OESTE),
    MS(RegiaoEnum.CENTRO_OESTE),
    MG(RegiaoEnum.SUDESTE),
    PR(RegiaoEnum.SUL),
    PB(RegiaoEnum.NORDESTE),
    PA(RegiaoEnum.NORTE),
    PE(RegiaoEnum.NORDESTE),
    PI(RegiaoEnum.NORDESTE),
    RN(RegiaoEnum.NORDESTE),
    RS(RegiaoEnum.SUL),
    RJ(RegiaoEnum.SUDESTE),
    RO(RegiaoEnum.NORTE),
    RR(RegiaoEnum.NORTE),
    SC(RegiaoEnum.SUL),
    SE(RegiaoEnum.NORDESTE),
    SP(RegiaoEnum.SUDESTE),
    TO(RegiaoEnum.NORTE);

    private RegiaoEnum regiao;

    UfEnum(RegiaoEnum regiao){
        this.regiao = regiao;
    }

    public RegiaoEnum getRegiao(){
        return this.regiao;
    }

    public static List<UfEnum> getUfs(RegiaoEnum r){
        return Arrays.asList(UfEnum.values())
                .stream()
                .filter(uf -> uf.getRegiao().equals(r))
                .collect(Collectors.toList());
    }

    public static List<UfEnum> getAll(){
        return Arrays.asList(UfEnum.values())
                .stream()
                .collect(Collectors.toList());
    }
}
