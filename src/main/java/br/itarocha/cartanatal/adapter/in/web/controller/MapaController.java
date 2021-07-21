package br.itarocha.cartanatal.adapter.in.web.controller;

import br.itarocha.cartanatal.core.decorator.ChartDraw;
import br.itarocha.cartanatal.core.model.DadosPessoais;
import br.itarocha.cartanatal.core.model.presenter.Paragrafo;
import br.itarocha.cartanatal.core.service.*;
import br.itarocha.cartanatal.core.model.presenter.CartaNatalResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mapa")
public class MapaController {

    private static final String FILE_NAME_TXT = "mapa.txt";
    private static final String FILE_NAME_DOC = "mapa.docx";

    @Autowired
    private CartaNatalService service;

    @Autowired
    private InterpretadorService interpretador;

    @Autowired
    private InterpretadorServiceNew interpretadorNew;

    @Autowired
    private TextFileExporter textFileExporter;

    @Autowired
    private WordFileExporter wordFileExporter;

    @Autowired
    private ChartDraw chartDraw;

    @GetMapping
    public ResponseEntity<String> getMapa(){
        StringBuilder builder = new StringBuilder();
        builder.append("/api/mapa\n");

        ObjectMapper mapper = new ObjectMapper();

        DadosPessoais dados = DadosPessoais.builder().build();
        dados.setNome("Raymond Nonath");
        dados.setDataHoraNascimento(LocalDateTime.of(1972, 6, 29, 5, 0, 0));
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

    @PostMapping
    @RequestMapping("/single")
    public ResponseEntity<CartaNatalResponse> getMapa(@RequestBody DadosPessoais model){
        CartaNatalResponse response = calcular(model);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @RequestMapping("/textfile")
    public ResponseEntity<InputStreamResource> getMapaText(@RequestBody DadosPessoais model) throws FileNotFoundException {
        CartaNatalResponse response = calcular(model);

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

    @PostMapping
    @RequestMapping("/wordfile")
    public ResponseEntity<Resource> getMapaDocFile(@RequestBody DadosPessoais model) throws FileNotFoundException {
        CartaNatalResponse response = calcular(model);

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

    @PostMapping
    @RequestMapping("/atualizar")
    public ResponseEntity<String> atualizar(){
        return ResponseEntity.ok("Atualizado com sucesso");
    }

    private CartaNatalResponse calcular(DadosPessoais dados) {
        try {
            CartaNatalResponse cartaNatal = service.buildMapa(dados);
            Map<String, String> mapa = interpretador.gerarInterpretacoes(cartaNatal);
            return cartaNatal;
        } catch (Exception e){
            return CartaNatalResponse.builder().build();
        }
    }

    /*
    @GetMapping(
            value = "/get-file",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public @ResponseBody byte[] getFile() throws IOException {
        InputStream in = getClass()
                .getResourceAsStream("/com/baeldung/produceimage/data.txt");
        return IOUtils.toByteArray(in);
    }
    */

}

