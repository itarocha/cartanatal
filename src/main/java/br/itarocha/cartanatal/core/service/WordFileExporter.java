package br.itarocha.cartanatal.core.service;

import br.itarocha.cartanatal.core.model.presenter.Paragrafo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class WordFileExporter implements NewFileExporter {

    private static final String EXPORT_DIRECTORY = "/usr/local/";

    @Override
    public Path export(List<Paragrafo> paragrafos, String fileName) {
        Path filePath = Paths.get(EXPORT_DIRECTORY, fileName);
        try {
            MapaWord.gerarWord(filePath.toString(), paragrafos);
            return filePath;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
