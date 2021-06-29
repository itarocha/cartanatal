package br.itarocha.cartanatal.adapter.in.web.controller;

import br.itarocha.cartanatal.core.service.CartaNatalService;
import br.itarocha.cartanatal.core.model.presenter.CartaNatalResponse;
import br.itarocha.cartanatal.core.service.InterpretadorService;
import br.itarocha.cartanatal.core.service.TextFileExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
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

@RestController
@RequestMapping("/api/mapa")
public class MapaController {

    private static final String FILE_NAME_TXT = "mapa.txt";

    @Autowired
    private CartaNatalService service;

    @Autowired
    private InterpretadorService interpretador;

    @Autowired
    private TextFileExporter textFileExporter;

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

        String fileContent = interpretador.buildConteudoArquivoTxt(response, mapa);

        /*
        try {
            interpretador.montarArquivoPdf(response, mapa);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        // Create text file
        Path exportedPath = textFileExporter.export(fileContent, FILE_NAME_TXT);

        // Download file with InputStreamResource
        File exportedFile = exportedPath.toFile();
        FileInputStream fileInputStream = new FileInputStream(exportedFile);
        InputStreamResource inputStreamResource = new InputStreamResource(fileInputStream);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + FILE_NAME_TXT)
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(exportedFile.length())
                .body(inputStreamResource);
    }

    private CartaNatalResponse calcular(DadosPessoais dados) {
        try {
            CartaNatalResponse cartaNatal = service.buildMapa(dados.getNome(), dados.getData(), dados.getHora(), dados.getCidade(), dados.getUf());
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
