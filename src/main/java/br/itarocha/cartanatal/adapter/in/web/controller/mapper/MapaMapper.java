package br.itarocha.cartanatal.adapter.in.web.controller.mapper;

import br.itarocha.cartanatal.adapter.in.web.controller.dto.*;
import br.itarocha.cartanatal.core.model.domain.*;
import br.itarocha.cartanatal.core.util.Funcoes;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MapaMapper {

    public CartaNatalResponse toCartaNatal(Mapa mapa){
        return CartaNatalResponse.builder()
                .dadosPessoais(buildDadosPessoais(mapa))
                .planetasSignos(buildPlanetasSignos(mapa))
                .cuspides(buildCuspides(mapa))
                .elementos(buildElementos(mapa))
                .qualidades(buildQualidades(mapa))
                .polaridades(buildPolaridades(mapa))
                .aspectos(buildAspectos(mapa))
                .build();
    }

    private DadoPessoalResponse buildDadosPessoais(Mapa mapa){
        double latitude = mapa.getLatitude().Coordenada2Graus();
        double longitude = mapa.getLongitude().Coordenada2Graus();
        String lon = (longitude > 0 ? "E" : "W");
        String lat = (latitude > 0 ? "N" : "S");

        return DadoPessoalResponse.builder()
                .nome(mapa.getNome())
                .cidade(mapa.getNomeCidade())
                .data(mapa.getData())
                .hora(mapa.getHora())
                .deltaT( new DecimalFormat("#.####").format(mapa.getDeltaTSec()) )
                .julDay( new DecimalFormat("#.######").format(mapa.getJulDay()) )
                .lat(Funcoes.grau(latitude)+lat)
                .lon(Funcoes.grau(longitude)+lon)
                .grausDefasagemAscendente(mapa.getGrausDefasagemAscendente())
                .build();
    }

    private List<PlanetaSignoResponse> buildPlanetasSignos(Mapa mapa) {
        return mapa.getPosicoesPlanetas()
                .stream()
                .map(this::planetaPosicaoToPlanetaSigno)
                .collect(Collectors.toList());
    }

    private Map<String, Integer> buildQualidades(Mapa mapa) {
        Map<String, Integer> mapaQualidades = new HashMap<>();

        Long totCardinais = mapa.getPosicoesPlanetas()
                .stream()
                .filter(pp -> EnumQualidade.CARDINAL.equals(pp.getEnumSigno().getQualidade()) )
                .count();
        Long totFixos = mapa.getPosicoesPlanetas()
                .stream()
                .filter(pp -> EnumQualidade.FIXO.equals(pp.getEnumSigno().getQualidade()) )
                .count();
        Long totMutaveis = mapa.getPosicoesPlanetas()
                .stream()
                .filter(pp -> EnumQualidade.MUTAVEL.equals(pp.getEnumSigno().getQualidade()) )
                .count();

        mapaQualidades.put("Cardinais", totCardinais.intValue());
        mapaQualidades.put("Fixos", totFixos.intValue());
        mapaQualidades.put("Mutáveis", totMutaveis.intValue());

        return mapaQualidades;
    }
    private Map<String, Long> buildPolaridades(Mapa mapa) {
        Map<String, Long> mapaPolaridades = new HashMap<>();

        Long totPositivas = mapa.getPosicoesPlanetas()
                .stream()
                .filter(pp -> EnumPolaridade.POSITIVA.equals(pp.getEnumSigno().getPolaridade()) )
                .count();
        Long totNegativas = mapa.getPosicoesPlanetas()
                .stream()
                .filter(pp -> EnumPolaridade.NEGATIVA.equals(pp.getEnumSigno().getPolaridade()) )
                .count();

        mapaPolaridades.put("Positivas", totPositivas);
        mapaPolaridades.put("Negativas", totNegativas);

        return mapaPolaridades;
    }

    private Map<String, Integer> buildElementos(Mapa mapa) {
        Map<String, Integer> mapaElementos = new HashMap<>();

        Long totFogo = mapa.getPosicoesPlanetas()
                .stream()
                .filter(pp -> EnumElemento.FOGO.equals(pp.getEnumSigno().getElemento()) )
                .count();

        Long totTerra = mapa.getPosicoesPlanetas()
                .stream()
                .filter(pp -> EnumElemento.TERRA.equals(pp.getEnumSigno().getElemento()) )
                .count();

        Long totAgua = mapa.getPosicoesPlanetas()
                .stream()
                .filter(pp -> EnumElemento.AGUA.equals(pp.getEnumSigno().getElemento()) )
                .count();

        Long totAr = mapa.getPosicoesPlanetas()
                .stream()
                .filter(pp -> EnumElemento.AR.equals(pp.getEnumSigno().getElemento()) )
                .count();

        mapaElementos.put("Fogo", totFogo.intValue());
        mapaElementos.put("Terra", totTerra.intValue());
        mapaElementos.put("Água", totAgua.intValue());
        mapaElementos.put("Ar", totAr.intValue());

        return mapaElementos;
    }

    private PlanetaSignoResponse planetaPosicaoToPlanetaSigno(PlanetaPosicao pp){
        return PlanetaSignoResponse.builder()
                .planeta(pp.getEnumPlaneta().getSigla())
                .signo(pp.getEnumSigno().getSigla())
                .casa(pp.getCasa())
                .angulo(pp.getLocalizacao().getAngulo())
                .grau(pp.getLocalizacao().getGrau())
                .g360(Integer.parseInt(pp.getLocalizacao().getG()))
                .gg(Integer.parseInt(pp.getLocalizacao().getGnc()))
                .mm(Integer.parseInt(pp.getLocalizacao().getM()))
                .ss(Integer.parseInt(pp.getLocalizacao().getS()))
                .direcao(pp.getStatusRetrogrado())
                .descricao(String.format("%s%s%s%s", pp.getLocalizacao().getGnc(), pp.getEnumSigno().getSiglaCapitalized() ,
                                                     pp.getLocalizacao().getM(), "R".equals(pp.getStatusRetrogrado()) ? "R" : " " ))
                .build();
    }

    private List<CuspideResponse> buildCuspides(Mapa mapa) {
        return mapa.getListaCuspides().stream()
                .filter(c -> c.getNumero() <= 12)
                .map(this::cuspideCasaToCuspide)
                .collect(Collectors.toList());
    }

    private CuspideResponse cuspideCasaToCuspide(CuspideCasa c){
        return CuspideResponse.builder()
                .casa(c.getNumero())
                .signo(c.getEnumSigno().getSigla())
                .angulo(c.getLocalizacao().getAngulo())
                .grau(c.getLocalizacao().getGrau())
                .gg(Integer.parseInt(c.getLocalizacao().getGnc()))
                .mm(Integer.parseInt(c.getLocalizacao().getM()))
                .ss(Integer.parseInt(c.getLocalizacao().getS()))
                .descricao(String.format("%s%s%s", c.getLocalizacao().getGnc(),
                        c.getEnumSigno().getSiglaCapitalized(),
                        c.getLocalizacao().getM()))
                .build();
    }

    private List<AspectoResponse> buildAspectos(Mapa mapa) {
        return mapa.getListaAspectos().stream()
                .map(this::itemAspectoToAspecto)
                .collect(Collectors.toList());
    }

    private AspectoResponse itemAspectoToAspecto(ItemAspecto a){
        return AspectoResponse.builder()
                .planetaOrigem(a.getPlanetaOrigem())
                .planetaDestino(a.getPlanetaDestino())
                .planetaOrigemAngulo(a.getPlanetaOrigemAngulo())
                .planetaDestinoAngulo(a.getPlanetaDestinoAngulo())
                .aspecto(a.getAspecto().getSigla())
                .x(a.getX())
                .y(a.getY())
                .orbe(a.getOrbe())
                .orbeGrau(a.getOrbeGrau())
                .orbeMinuto(a.getOrbeMinuto())
                .orbeDescricao(a.getOrbeDescricao())
                .build();
    }
}

/*
paragrafo:
    titulo
    comum
    tabela
*/
