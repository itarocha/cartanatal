package br.itarocha.cartanatal.adapter.in.web.controller;

import br.itarocha.cartanatal.core.model.presenter.CartaNatal;
import br.itarocha.cartanatal.core.service.CartaNatalService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class HomeController {

    @Autowired
    private CartaNatalService service;

    @GetMapping
    public ResponseEntity<String> getMapa(){
        StringBuilder builder = new StringBuilder();
        builder.append("/api/mapa\n");

        ObjectMapper mapper = new ObjectMapper();

        DadosPessoais dados = new DadosPessoais();
        dados.setNome("Raymond Nonath");
        dados.setData("29/06/1972");
        dados.setHora("05:00");
        dados.setCidade("Caxias");
        dados.setUf("MA");

        try {
            String json = mapper.writeValueAsString(dados);
            builder.append(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(builder.toString());
    }
}
