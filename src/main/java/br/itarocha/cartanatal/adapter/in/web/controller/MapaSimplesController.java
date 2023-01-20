package br.itarocha.cartanatal.adapter.in.web.controller;

import br.itarocha.cartanatal.adapter.in.web.controller.mapper.MapaMapper;
import br.itarocha.cartanatal.core.model.DadosPessoais;
import br.itarocha.cartanatal.core.model.domain.Cidade;
import br.itarocha.cartanatal.core.model.domain.Mapa;
import br.itarocha.cartanatal.core.service.*;
import br.itarocha.cartanatal.adapter.in.web.controller.dto.CartaNatalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/mapa")
public class MapaSimplesController {

    private static final String FILE_NAME_TXT = "mapa.txt";
    private static final String FILE_NAME_DOC = "mapa.docx";

    private final MapaService mapaService;
    private final BuscadorService buscadorService;
    private final MapaMapper mapper;

    @PostMapping
    @RequestMapping("/single")
    public ResponseEntity<?> getMapa(@RequestBody DadosPessoais dadosPessoais){
        Cidade cidade = buscadorService.findCidade(dadosPessoais.getCidade(), dadosPessoais.getUf());
        if (Objects.isNull(cidade)){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Cidade inválida");
        }
        Mapa mapa = mapaService.build(dadosPessoais, cidade);
        CartaNatalResponse response = mapper.toCartaNatal(mapa);
        return ResponseEntity.ok(response);
    }

}

