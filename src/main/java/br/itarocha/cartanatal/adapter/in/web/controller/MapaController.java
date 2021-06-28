package br.itarocha.cartanatal.adapter.in.web.controller;

import br.itarocha.cartanatal.core.model.Interpretacao;
import br.itarocha.cartanatal.core.service.CartaNatalService;
import br.itarocha.cartanatal.core.model.presenter.CartaNatalResponse;
import br.itarocha.cartanatal.core.service.GeradorPdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mapa")
public class MapaController {

    @Autowired
    private CartaNatalService service;

    @Autowired
    private GeradorPdfService geradorPdfService;

    @PostMapping
    public ResponseEntity<CartaNatalResponse> getMapa(@RequestBody DadosPessoais model){
        return ResponseEntity.ok(calcular(model));
    }

    private CartaNatalResponse calcular(DadosPessoais dados) {
        try {
            CartaNatalResponse cartaNatal = service.buildMapa(dados.getNome(), dados.getData(), dados.getHora(), dados.getCidade(), dados.getUf());

            List<Interpretacao> interpretacoes = geradorPdfService.createArquivo(true, cartaNatal);
            interpretacoes.stream().forEach(i -> {
                ///System.out.println(i.getTitulo());
                i.getParagrafos().stream().forEach(t -> {
                    System.out.println(t);
                });

            });
            return cartaNatal;
        } catch (Exception e){
            return CartaNatalResponse.builder().build();
        }
    }
}
