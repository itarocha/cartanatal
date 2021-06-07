package br.itarocha.cartanatal.core.model.mapper;

import br.itarocha.cartanatal.core.Mapa;
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

    public CartaNatal toCartaNatal(Mapa mapa){
        return CartaNatal.builder()
                .dadosPessoais(buildDadosPessoais(mapa))
                .planetasSignos(buildPlanetasSignos(mapa))
                .cuspides(buildCuspides(mapa))
                .aspectos(buildAspectos(mapa))
                .build();
    }

    private DadoPessoal buildDadosPessoais(Mapa mapa){
        double latitude = mapa.getLatitude().Coordenada2Graus();
        double longitude = mapa.getLongitude().Coordenada2Graus();
        String lon = (longitude > 0 ? "E" : "W");
        String lat = (latitude > 0 ? "N" : "S");

        return DadoPessoal.builder()
                .nome(mapa.getNome())
                .data(mapa.getData())
                .hora(mapa.getHora())
                .deltaT( new DecimalFormat("#.####").format(mapa.getDeltaTSec()) )
                .julDay( new DecimalFormat("#.######").format(mapa.getJulDay()) )
                .lat(Funcoes.grau(latitude)+lat)
                .lon(Funcoes.grau(longitude)+lon)
                .build();
    }

    private List<PlanetaSigno> buildPlanetasSignos(Mapa mapa) {
        return mapa.getPosicoesPlanetas()
                .stream()
                .map(this::planetaPosicaoToPlanetaSigno)
                .collect(Collectors.toList());
    }

    private PlanetaSigno planetaPosicaoToPlanetaSigno(PlanetaPosicao pp){
        return PlanetaSigno.builder()
                .planeta(pp.getEnumPlaneta().getSigla())
                .signo(pp.getEnumSigno().getSigla())
                .casa(pp.getCasa())
                .grau(pp.getGrau())
                .gg(Integer.parseInt(pp.getGnc()))
                .mm(Integer.parseInt(pp.getM()))
                .ss(Integer.parseInt(pp.getS()))
                .build();
    }

    private List<Cuspide> buildCuspides(Mapa mapa) {
        return mapa.getListaCuspides().stream()
                .filter(c -> c.getNumero() <= 12)
                .map(this::cuspideCasaToCuspide)
                .collect(Collectors.toList());
    }

    private Cuspide cuspideCasaToCuspide(CuspideCasa c){
        String g = c.getGrau();
        String gnc = c.getGrauNaCasa();
        gnc = gnc.replace('.', '-');
        String[] gms = gnc.split("-");

        return Cuspide.builder()
                .casa(c.getNumero())
                .signo(c.getEnumSigno().getSigla())
                .grau(g)
                .gg(Integer.parseInt(gms[0]))
                .mm(Integer.parseInt(gms[1]))
                .ss(Integer.parseInt(gms[2]))
                .build();
    }

    private List<Aspecto> buildAspectos(Mapa mapa) {
        return mapa.getListaAspectos().stream()
                .map(this::itemAspectoToAspecto)
                .collect(Collectors.toList());
    }

    private Aspecto itemAspectoToAspecto(ItemAspecto a){
        return Aspecto.builder()
                .planetaOrigem(a.getPlanetaA().getEnumPlaneta().getSigla())
                .planetaDestino(a.getPlanetaB().getEnumPlaneta().getSigla())
                .aspecto(a.getAspecto().getSigla())
                .x(a.getPlanetaA().getCoordenada())
                .y(a.getPlanetaB().getCoordenada())
                .build();
    }
}
