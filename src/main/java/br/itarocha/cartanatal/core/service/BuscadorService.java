package br.itarocha.cartanatal.core.service;

import br.itarocha.cartanatal.core.model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static br.itarocha.cartanatal.core.service.ArquivosConstantes.*;

@Service
public class BuscadorService {

    private List<SignoSolar> listaSignosSolares;
    private List<MapaCuspide> listaCuspides;
    private List<MapaPlanetaAspecto> listaAspectos;
    private List<PlanetaCasa> listaPlanetasCasas;
    private List<PlanetaSigno> listaPlanetasSignos;

    public BuscadorService(){
        restaurarSignosSolares();
        restaurarCuspides();
        restaurarAspectos();
        restaurarPlanetasCasas();
        restaurarPlanetasSignos();
    }

    public SignoSolar findSignoSolar(String signo) {
        TipoSigno ts = getTipoSigno(signo);
        return listaSignosSolares.stream()
                .filter(s -> ts.equals(s.getSigno()))
                .findFirst()
                .orElse(null);
    }

    public MapaCuspide findCuspide(String signo, Integer casa) {
        TipoSigno ts = getTipoSigno(signo);
        return listaCuspides.stream().filter(c -> ts.equals(c.getSigno()) && c.getCasa().equals(casa))
                .findFirst()
                .orElse(null);
    }

    public PlanetaSigno findPlanetaSigno(String planeta, String signo) {
        TipoPlaneta tp = getTipoPlaneta(planeta);
        TipoSigno ts = getTipoSigno(signo);

        return listaPlanetasSignos.stream().filter(ps -> ts.equals(ps.getSigno()) && tp.equals(ps.getPlaneta()))
                .findFirst()
                .orElse(null);
    }

    public PlanetaCasa findPlanetaCasa(String planeta, Integer casa) {
        TipoPlaneta tp = getTipoPlaneta(planeta);

        return listaPlanetasCasas.stream()
                .filter(pc -> tp.equals(pc.getPlaneta()) && pc.getCasa().equals(casa))
                .findFirst()
                .orElse(null);
    }

    public MapaPlanetaAspecto findAspecto(String planetaOrigem, String planetaDestino, String aspecto) {
        TipoPlaneta tpo = getTipoPlaneta(planetaOrigem);
        TipoPlaneta tpd = getTipoPlaneta(planetaDestino);
        TipoAspecto ta = getTipoAspecto(aspecto);

        return listaAspectos.stream()
                .filter(a -> tpo.equals(a.getPlanetaOrigem()) && tpd.equals(a.getPlanetaDestino()) && ta.equals(a.getAspecto()))
                .findFirst()
                .orElse(null);
    }

    private void restaurarSignosSolares() {
        ObjectMapper om = new ObjectMapper();
        try {
            URL url = this.getClass().getClassLoader().getResource(ARQUIVO_SIGNOS_SOLARES);
            listaSignosSolares = om.readValue(new File(url.toURI()), new TypeReference<List<SignoSolar>>(){});
            System.out.println("SIGNOS SOLARES RESTAURADO COM SUCESSO");
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void restaurarCuspides() {
        ObjectMapper om = new ObjectMapper();
        try {
            URL url = this.getClass().getClassLoader().getResource(ARQUIVO_CUSPIDES);
            listaCuspides = om.readValue(new File(url.toURI()), new TypeReference<List<MapaCuspide>>(){});
            System.out.println("CUSPIDES RESTAURADO COM SUCESSO");
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void restaurarAspectos() {
        ObjectMapper om = new ObjectMapper();
        try {
            URL url = this.getClass().getClassLoader().getResource(ARQUIVO_ASPECTOS);
            listaAspectos = om.readValue(new File(url.toURI()), new TypeReference<List<MapaPlanetaAspecto>>(){});
            System.out.println("ASPECTOS RESTAURADO COM SUCESSO");
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void restaurarPlanetasCasas() {
        ObjectMapper om = new ObjectMapper();
        try {
            URL url = this.getClass().getClassLoader().getResource(ARQUIVO_PLANETAS_CASAS);
            listaPlanetasCasas = om.readValue(new File(url.toURI()), new TypeReference<List<PlanetaCasa>>(){});
            System.out.println("PLANETAS CASAS RESTAURADO COM SUCESSO");
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void restaurarPlanetasSignos() {
        ObjectMapper om = new ObjectMapper();
        try {
            URL url = this.getClass().getClassLoader().getResource(ARQUIVO_PLANETAS_SIGNOS);
            listaPlanetasSignos = om.readValue(new File(url.toURI()), new TypeReference<List<PlanetaSigno>>(){});
            System.out.println("PLANETAS SIGNOS RESTAURADO COM SUCESSO");
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private TipoAspecto getTipoAspecto(String aspecto) {
        TipoAspecto ta = null;
        try {
            ta = TipoAspecto.valueOf(aspecto.toUpperCase());
        } catch(Exception e) {
            ta = TipoAspecto.XX;
        }
        return ta;
    }

    private TipoSigno getTipoSigno(String signo) {
        TipoSigno ts = null;
        try {
            ts = TipoSigno.valueOf(signo.toUpperCase());
        } catch(Exception e) {
            ts = TipoSigno.XX;
        }
        return ts;
    }

    private TipoPlaneta getTipoPlaneta(String planeta) {
        TipoPlaneta tp = null;
        try {
            tp = TipoPlaneta.valueOf(planeta.toUpperCase());
        } catch(Exception e) {
            tp = TipoPlaneta.XXX;
        }
        return tp;
    }

}
