package br.itarocha.cartanatal.adapter.in.web.controller;

import br.itarocha.cartanatal.core.model.Interpretacao;
import br.itarocha.cartanatal.core.service.CartaNatalService;
import br.itarocha.cartanatal.core.model.presenter.CartaNatalResponse;
import br.itarocha.cartanatal.core.service.GeradorPdfService;
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

    @Autowired
    private CartaNatalService service;

    @Autowired
    private GeradorPdfService geradorPdfService;

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
        Map<String, String> mapa = geradorPdfService.createArquivo(response);
        //return ResponseEntity.ok(mapa);

        String fileName = "arquivo.txt";
        String fileContent = geradorPdfService.buildConteudoArquivoTxt(response, mapa);


        // Create text file
        Path exportedPath = geradorPdfService.export(fileContent, fileName);

        // Download file with InputStreamResource
        File exportedFile = exportedPath.toFile();
        FileInputStream fileInputStream = new FileInputStream(exportedFile);
        InputStreamResource inputStreamResource = new InputStreamResource(fileInputStream);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(exportedFile.length())
                .body(inputStreamResource);
    }

    private CartaNatalResponse calcular(DadosPessoais dados) {
        try {
            CartaNatalResponse cartaNatal = service.buildMapa(dados.getNome(), dados.getData(), dados.getHora(), dados.getCidade(), dados.getUf());

            Map<String, String> mapa = geradorPdfService.createArquivo(cartaNatal);
            /*
            interpretacoes.stream().forEach(i -> {
                ///System.out.println(i.getTitulo());
                i.getParagrafos().stream().forEach(t -> {
                    System.out.println(t);
                });
            });
             */
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
