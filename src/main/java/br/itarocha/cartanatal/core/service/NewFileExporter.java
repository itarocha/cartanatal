package br.itarocha.cartanatal.core.service;

import br.itarocha.cartanatal.core.model.presenter.Paragrafo;

import java.nio.file.Path;
import java.util.List;

public interface NewFileExporter {
    Path export(List<Paragrafo> paragrafos, String fileName);
}
