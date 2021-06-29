package br.itarocha.cartanatal.core.service;

import java.nio.file.Path;

public interface FileExporter {
    Path export(String fileContent, String fileName);
}
