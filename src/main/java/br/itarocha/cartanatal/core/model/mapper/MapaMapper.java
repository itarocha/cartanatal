package br.itarocha.cartanatal.core.model.mapper;

import br.itarocha.cartanatal.core.model.domain.Mapa;
import br.itarocha.cartanatal.core.model.domain.CuspideCasa;
import br.itarocha.cartanatal.core.model.domain.ItemAspecto;
import br.itarocha.cartanatal.core.model.domain.PlanetaPosicao;
import br.itarocha.cartanatal.core.model.presenter.*;
import br.itarocha.cartanatal.core.util.Funcoes;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MapaMapper {

    public CartaNatalResponse toCartaNatal(Mapa mapa){
        return CartaNatalResponse.builder()
                .dadosPessoais(buildDadosPessoais(mapa))
                .planetasSignos(buildPlanetasSignos(mapa))
                .cuspides(buildCuspides(mapa))
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

    private PlanetaSignoResponse planetaPosicaoToPlanetaSigno(PlanetaPosicao pp){
        return PlanetaSignoResponse.builder()
                .planeta(pp.getEnumPlaneta().getSigla())
                .signo(pp.getEnumSigno().getSigla())
                .casa(pp.getCasa())
                .grau(pp.getGrau())
                .g360(Integer.parseInt(pp.getG()))
                .gg(Integer.parseInt(pp.getGnc()))
                .mm(Integer.parseInt(pp.getM()))
                .ss(Integer.parseInt(pp.getS()))
                .direcao(pp.getStatusRetrogrado())
                .descricao(String.format("%s%s%s%s", pp.getGnc(), pp.getEnumSigno().getSiglaCapitalized() ,
                                                     pp.getM(), "R".equals(pp.getStatusRetrogrado()) ? "R" : " " ))
                .build();
    }

    private List<CuspideResponse> buildCuspides(Mapa mapa) {
        return mapa.getListaCuspides().stream()
                .filter(c -> c.getNumero() <= 12)
                .map(this::cuspideCasaToCuspide)
                .collect(Collectors.toList());
    }

    private CuspideResponse cuspideCasaToCuspide(CuspideCasa c){
        String g = c.getGrau();
        String gnc = c.getGrauNaCasa();
        gnc = gnc.replace('.', '-');
        String[] gms = gnc.split("-");

        return CuspideResponse.builder()
                .casa(c.getNumero())
                .signo(c.getEnumSigno().getSigla())
                .grau(g)
                .gg(Integer.parseInt(gms[0]))
                .mm(Integer.parseInt(gms[1]))
                .ss(Integer.parseInt(gms[2]))
                .descricao(String.format("%s%s%s", c.getGnc(), c.getEnumSigno().getSiglaCapitalized() , c.getM()))
                .build();
    }

    private List<AspectoResponse> buildAspectos(Mapa mapa) {
        return mapa.getListaAspectos().stream()
                .map(this::itemAspectoToAspecto)
                .collect(Collectors.toList());
    }

    private AspectoResponse itemAspectoToAspecto(ItemAspecto a){
        return AspectoResponse.builder()
                .planetaOrigem(a.getPlanetaA().getEnumPlaneta().getSigla())
                .planetaDestino(a.getPlanetaB().getEnumPlaneta().getSigla())
                .aspecto(a.getAspecto().getSigla())
                .x(a.getPlanetaA().getCoordenada())
                .y(a.getPlanetaB().getCoordenada())
                .build();
    }
}
