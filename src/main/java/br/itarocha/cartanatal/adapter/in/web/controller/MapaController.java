package br.itarocha.cartanatal.adapter.in.web.controller;

import br.itarocha.cartanatal.core.service.CartaNatalService;
import br.itarocha.cartanatal.core.model.presenter.CartaNatal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mapa")
public class MapaController {

    @Autowired
    private CartaNatalService service;

    @PostMapping
    public ResponseEntity<CartaNatal> getMapa(){
        return ResponseEntity.ok(calcular());
    }

    private CartaNatal calcular() {
        String nome = "Itamar Rocha Chaves Junior";
        String data = "29/06/1972";
        String hora = "05:00";
        String cidade = "Caxias";
        String uf = "MA";

        try {
            return service.buildMapa(nome, data, hora, cidade, uf);
        } catch (Exception e){
            return CartaNatal.builder().build();
        }
    }
}
