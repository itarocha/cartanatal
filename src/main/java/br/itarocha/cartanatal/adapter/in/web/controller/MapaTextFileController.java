package br.itarocha.cartanatal.adapter.in.web.controller;

import br.itarocha.cartanatal.adapter.in.web.controller.dto.CartaNatalResponse;
import br.itarocha.cartanatal.adapter.in.web.controller.mapper.MapaMapper;
import br.itarocha.cartanatal.core.decorator.ChartDraw;
import br.itarocha.cartanatal.core.model.DadosPessoais;
import br.itarocha.cartanatal.core.model.domain.Cidade;
import br.itarocha.cartanatal.core.model.domain.Mapa;
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
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/mapa")
public class MapaTextFileController {

    private static final String FILE_NAME_TXT = "mapa.txt";
    private static final String FILE_NAME_DOC = "mapa.docx";

    private final MapaService mapaService;
    private final BuscadorService buscadorService;
    private final MapaMapper mapper;
    private final InterpretadorService interpretador;
    private final TextFileExporter textFileExporter;
    private final ChartDraw chartDraw;

    @PostMapping
    @RequestMapping("/textfile")
    public ResponseEntity<?> getMapaText(@RequestBody DadosPessoais dadosPessoais) throws FileNotFoundException {
        Cidade cidade = buscadorService.findCidade(dadosPessoais.getCidade(), dadosPessoais.getUf());
        if (Objects.isNull(cidade)){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Cidade inválida");
        }
        Mapa _mapa = mapaService.build(dadosPessoais, cidade);
        CartaNatalResponse response = mapper.toCartaNatal(_mapa);

        Map<String, String> mapa = interpretador.gerarInterpretacoes(response);
        String fileContent = interpretador.buildConteudoArquivoTxt(mapa);

        // Create text file
        Path exportedPath = textFileExporter.export(fileContent, FILE_NAME_TXT);
        System.out.println("Gerando arquivo de saída : "+exportedPath.toString());

        // Download file with InputStreamResource
        File exportedFile = exportedPath.toFile();
        FileInputStream fileInputStream = new FileInputStream(exportedFile);
        InputStreamResource inputStreamResource = new InputStreamResource(fileInputStream);

        chartDraw.drawMapa(response);
        chartDraw.drawAspectos(response);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + FILE_NAME_TXT)
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(exportedFile.length())
                .body(inputStreamResource);
    }

}

