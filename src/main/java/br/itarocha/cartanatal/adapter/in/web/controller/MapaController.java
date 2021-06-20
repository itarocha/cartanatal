package br.itarocha.cartanatal.adapter.in.web.controller;

import br.itarocha.cartanatal.core.service.CartaNatalService;
import br.itarocha.cartanatal.core.model.presenter.CartaNatal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mapa")
public class MapaController {

    @Autowired
    private CartaNatalService service;

    @PostMapping
    public ResponseEntity<CartaNatal> getMapa(@RequestBody DadosPessoais model){
        return ResponseEntity.ok(calcular(model));
    }

    private CartaNatal calcular(DadosPessoais dados) {
        try {
            return service.buildMapa(dados.getNome(), dados.getData(), dados.getHora(), dados.getCidade(), dados.getUf());
        } catch (Exception e){
            return CartaNatal.builder().build();
        }
    }
}
