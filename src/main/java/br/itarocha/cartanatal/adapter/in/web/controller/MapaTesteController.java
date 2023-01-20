package br.itarocha.cartanatal.adapter.in.web.controller;

import br.itarocha.cartanatal.adapter.in.web.controller.dto.CartaNatalResponse;
import br.itarocha.cartanatal.adapter.in.web.controller.mapper.MapaMapper;
import br.itarocha.cartanatal.core.model.DadosPessoais;
import br.itarocha.cartanatal.core.model.domain.Cidade;
import br.itarocha.cartanatal.core.model.domain.Mapa;
import br.itarocha.cartanatal.core.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/mapa")
public class MapaTesteController {

    private static final String FILE_NAME_TXT = "mapa.txt";
    private static final String FILE_NAME_DOC = "mapa.docx";

    private final MapaService mapaService;
    private final BuscadorService buscadorService;
    private final MapaMapper mapper;

    @PostMapping
    public ResponseEntity<?> getMapa(){
        StringBuilder builder = new StringBuilder();
        builder.append("/api/mapa\n");

        DadosPessoais dados = DadosPessoais.builder().build();
        dados.setNome("Raymond Nonath");
        dados.setDataHoraNascimento(LocalDateTime.of(1972, 6, 29, 5, 0, 0));
        dados.setCidade("Caxias");
        dados.setUf("MA");

        Cidade cidade = buscadorService.findCidade(dados.getCidade(), dados.getUf());
        if (Objects.isNull(cidade)){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Cidade inválida");
        }

        Mapa mapa = mapaService.build(dados, cidade);
        CartaNatalResponse response = mapper.toCartaNatal(mapa);
        return ResponseEntity.ok(response);
    }
}

