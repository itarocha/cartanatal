package br.itarocha.cartanatal.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
/*
import com.itarocha.stardata.model.*;
import com.itarocha.stardata.model.entities.*;
import com.itarocha.stardata.repository.*;
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

//import static com.itarocha.stardata.service.ArquivosConstantes.*;

@Service
public class BuscadorDatabaseService {

    /*
    @Autowired
    private SignoSolarRepository signoSolarRepository;

    @Autowired
    private MapaCuspideRepository mapaCuspideRepository;

    @Autowired
    private MapaPlanetaAspectoRepository mapaPlanetaAspectoRepository;

    @Autowired
    private PlanetaCasaRepository planetaCasaRepository;

    @Autowired
    private PlanetaSignoRepository planetaSignoRepository;
    */

    /*
    public void gravarTodos(){
        gravarSignosSolares();
        gravarCuspides();
        gravarAspectos();
        gravarPlanetasCasas();
        gravarPlanetasSignos();
    }

    private void gravarSignosSolares(){
        List<SignoSolarEntity> signos = signoSolarRepository.findAll();
        List<SignoSolar> lista = signos.stream().map(this::getSignoSolar).collect(Collectors.toList());
        ObjectMapper om = new ObjectMapper();
        try {
            om.writeValue(new File(ARQUIVO_SIGNOS_SOLARES), lista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SignoSolar getSignoSolar(SignoSolarEntity obj) {
        /*
        SignoSolar retorno = new SignoSolar();
        retorno.setId(obj.getId());
        retorno.setSigno(obj.getSigno());
        retorno.setDescricao(obj.getDescricao());
        retorno.setConferido(obj.getConferido());
        retorno.setTexto(obj.getTexto());
        return retorno;
    }

    private void gravarCuspides(){
        List<MapaCuspideEntity> cuspides = mapaCuspideRepository.findAll();
        List<MapaCuspide> lista = cuspides.stream().map(this::getMapaCuspide).collect(Collectors.toList());
        ObjectMapper om = new ObjectMapper();
        try {
            om.writeValue(new File(ARQUIVO_CUSPIDES), lista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MapaCuspide getMapaCuspide(MapaCuspideEntity obj) {
        MapaCuspide retorno = new MapaCuspide();
        retorno.setId(obj.getId());
        retorno.setCasa(obj.getCasa());
        retorno.setSigno(obj.getSigno());
        retorno.setTexto(obj.getTexto());
        return retorno;
    }

    private void gravarAspectos(){
        List<MapaPlanetaAspectoEntity> aspectos = mapaPlanetaAspectoRepository.findAll();
        List<MapaPlanetaAspecto> lista = aspectos.stream().map(this::getMapaPlanetaAspecto).collect(Collectors.toList());
        ObjectMapper om = new ObjectMapper();
        try {
            om.writeValue(new File(ARQUIVO_ASPECTOS), lista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MapaPlanetaAspecto getMapaPlanetaAspecto(MapaPlanetaAspectoEntity obj) {
        Long idAspectoMestre = obj.getAspectoMestre() == null ? null : obj.getAspectoMestre().getId();

        MapaPlanetaAspecto retorno = new MapaPlanetaAspecto();
        retorno.setId(obj.getId());
        retorno.setPlanetaOrigem(obj.getPlanetaOrigem());
        retorno.setPlanetaDestino(obj.getPlanetaDestino());
        retorno.setAspecto(obj.getAspecto());
        retorno.setTipoRelacao(obj.getTipoRelacao());
        retorno.setConferido(obj.getConferido());
        retorno.setAspectoMestre( idAspectoMestre );
        retorno.setTexto(obj.getTexto());
        return retorno;
    }

    private void gravarPlanetasCasas(){
        List<PlanetaCasaEntity> planetasCasas = planetaCasaRepository.findAll();
        List<PlanetaCasa> lista = planetasCasas.stream().map(this::getPlanetaCasa).collect(Collectors.toList());

        ObjectMapper om = new ObjectMapper();
        try {
            om.writeValue(new File(ARQUIVO_PLANETAS_CASAS), lista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PlanetaCasa getPlanetaCasa(PlanetaCasaEntity obj) {
        PlanetaCasa retorno = new PlanetaCasa();
        retorno.setId(obj.getId());
        retorno.setPlaneta(obj.getPlaneta());
        retorno.setCasa(obj.getCasa());
        retorno.setConferido(obj.getConferido());
        retorno.setTexto(obj.getTexto());
        return retorno;
    }

    private void gravarPlanetasSignos(){
        List<PlanetaSignoEntity> planetasSignos = planetaSignoRepository.findAll();
        List<PlanetaSigno> lista = planetasSignos.stream().map(this::getPlanetaSigno).collect(Collectors.toList());

        ObjectMapper om = new ObjectMapper();
        try {
            om.writeValue(new File(ARQUIVO_PLANETAS_SIGNOS), lista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PlanetaSigno getPlanetaSigno(PlanetaSignoEntity obj) {
        PlanetaSigno retorno = new PlanetaSigno();
        retorno.setId(obj.getId());
        retorno.setPlaneta(obj.getPlaneta());
        retorno.setSigno(obj.getSigno());
        retorno.setConferido(obj.getConferido());
        retorno.setTexto(obj.getTexto());
        return retorno;
    }
    */
}
