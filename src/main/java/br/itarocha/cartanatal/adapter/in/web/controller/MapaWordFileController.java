package br.itarocha.cartanatal.adapter.in.web.controller;

import br.itarocha.cartanatal.adapter.in.web.controller.dto.CartaNatalResponse;
import br.itarocha.cartanatal.adapter.in.web.controller.mapper.MapaMapper;
import br.itarocha.cartanatal.core.decorator.ChartDraw;
import br.itarocha.cartanatal.core.model.DadosPessoais;
import br.itarocha.cartanatal.core.model.domain.Cidade;
import br.itarocha.cartanatal.core.model.domain.Mapa;
import br.itarocha.cartanatal.core.model.presenter.Paragrafo;
import br.itarocha.cartanatal.core.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/mapa")
public class MapaWordFileController {

    private static final String FILE_NAME_TXT = "mapa.txt";
    private static final String FILE_NAME_DOC = "mapa.docx";

    private final MapaService mapaService;
    private final BuscadorService buscadorService;
    private final MapaMapper mapper;
    private final ChartDraw chartDraw;

    private final InterpretadorServiceNew interpretadorNew;
    private final WordFileExporter wordFileExporter;
    @PostMapping
    @RequestMapping("/wordfile")
    public ResponseEntity<?> getMapaDocFile(@RequestBody DadosPessoais dadosPessoais) throws FileNotFoundException {
        Cidade cidade = buscadorService.findCidade(dadosPessoais.getCidade(), dadosPessoais.getUf());
        if (Objects.isNull(cidade)){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Cidade inválida");
        }
        Mapa _mapa = mapaService.build(dadosPessoais, cidade);
        CartaNatalResponse response = mapper.toCartaNatal(_mapa);


        chartDraw.drawMapa(response);
        chartDraw.drawAspectos(response);

        List<Paragrafo> lista = interpretadorNew.gerarInterpretacoes(response);
        Path exportedPath = wordFileExporter.export(lista, FILE_NAME_DOC);
        File file = exportedPath.toFile();

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + FILE_NAME_DOC)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}

