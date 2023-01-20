package br.itarocha.cartanatal.core.service;

import de.thmac.swisseph.SweDate;
import de.thmac.swisseph.SwissEph;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@Component
public class SWService {

    @Value("${parametros.diretorioEphe}")
    private String DIRETORIO_EPHE;

    private static final Logger log = Logger.getAnonymousLogger();

    private SwissEph sw;

    @PostConstruct
    public void loadDependencies(){
        log.info("CARREGANDO PATH: "+DIRETORIO_EPHE);
        try {
            this.sw = new SwissEph(DIRETORIO_EPHE);
        } catch (Exception e) {
            System.out.println("NÃO FOI POSSÍVEL CARREGAR ARQUIVOS DO PATH "+DIRETORIO_EPHE);
            throw e;
        }
    }

    public SwissEph getSw(){
        return this.sw;
    }

}
