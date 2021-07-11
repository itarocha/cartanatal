package br.itarocha.cartanatal.core.service;

import br.itarocha.cartanatal.core.model.*;
import br.itarocha.cartanatal.core.model.domain.EnumElemento;
import br.itarocha.cartanatal.core.model.domain.EnumQualidade;
import br.itarocha.cartanatal.core.model.interpretacao.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static br.itarocha.cartanatal.core.service.ArquivosConstantes.*;

@Service
public class BuscadorService {

    private List<SignoSolar> listaSignosSolares;
    private List<MapaCuspide> listaCuspides;
    private List<MapaPlanetaAspecto> listaAspectos;
    private List<PlanetaCasa> listaPlanetasCasas;
    private List<PlanetaSigno> listaPlanetasSignos;
    private List<MapaElemento> listaMapaElementos;
    private List<MapaQualidade> listaMapaQualidades;

    public BuscadorService(){
        restaurarSignosSolares();
        restaurarCuspides();
        restaurarAspectos();
        restaurarPlanetasCasas();
        restaurarPlanetasSignos();
        restaurarMapaElementos();
        restaurarMapaQualidades();
    }

    public SignoSolar findSignoSolar(String signo) {
        TipoSigno ts = getTipoSigno(signo);
        return listaSignosSolares.stream()
                .filter(s -> ts.equals(s.getSigno()))
                .findFirst()
                .orElse(null);
    }

    public MapaElemento findElemento(String elemento) {
        EnumElemento el = EnumElemento.getBySigla(elemento);
        return listaMapaElementos.stream()
                .filter(me -> me.getElemento().equals(el))
                .findFirst()
                .orElse(null);
    }

    public MapaQualidade findQualidade(String qualidade) {
        EnumQualidade q = EnumQualidade.getBySigla(qualidade);
        return listaMapaQualidades.stream()
                .filter(mq -> mq.getQualidade().equals(q))
                .findFirst()
                .orElse(null);
    }

    public MapaCuspide findCuspide(String signo, Integer casa) {
        TipoSigno ts = getTipoSigno(signo);
        return listaCuspides.stream().filter(c -> ts.equals(c.getSigno()) && c.getCasa().equals(casa))
                .findFirst()
                .orElse(null);
    }

    public PlanetaSigno findPlanetaSigno(String planeta, String signo) {
        TipoPlaneta tp = getTipoPlaneta(planeta);
        TipoSigno ts = getTipoSigno(signo);

        return listaPlanetasSignos.stream().filter(ps -> ts.equals(ps.getSigno()) && tp.equals(ps.getPlaneta()))
                .findFirst()
                .orElse(null);
    }

    public PlanetaCasa findPlanetaCasa(String planeta, Integer casa) {
        TipoPlaneta tp = getTipoPlaneta(planeta);

        return listaPlanetasCasas.stream()
                .filter(pc -> tp.equals(pc.getPlaneta()) && pc.getCasa().equals(casa))
                .findFirst()
                .orElse(null);
    }

    public MapaPlanetaAspecto findAspecto(String planetaOrigem, String planetaDestino, String aspecto) {
        TipoPlaneta tpo = getTipoPlaneta(planetaOrigem);
        TipoPlaneta tpd = getTipoPlaneta(planetaDestino);
        TipoAspecto ta = getTipoAspecto(aspecto);

        return listaAspectos.stream()
                .filter(a -> tpo.equals(a.getPlanetaOrigem()) && tpd.equals(a.getPlanetaDestino()) && ta.equals(a.getAspecto()))
                .findFirst()
                .orElse(null);
    }

    private void restaurarSignosSolares() {
        ObjectMapper om = new ObjectMapper();
        try {
            URL url = this.getClass().getClassLoader().getResource(ARQUIVO_SIGNOS_SOLARES);
            listaSignosSolares = om.readValue(new File(url.toURI()), new TypeReference<List<SignoSolar>>(){});
            System.out.println("SIGNOS SOLARES RESTAURADO COM SUCESSO");
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void restaurarCuspides() {
        ObjectMapper om = new ObjectMapper();
        try {
            URL url = this.getClass().getClassLoader().getResource(ARQUIVO_CUSPIDES);
            listaCuspides = om.readValue(new File(url.toURI()), new TypeReference<List<MapaCuspide>>(){});
            System.out.println("CUSPIDES RESTAURADO COM SUCESSO");
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void restaurarAspectos() {
        ObjectMapper om = new ObjectMapper();
        try {
            URL url = this.getClass().getClassLoader().getResource(ARQUIVO_ASPECTOS);
            listaAspectos = om.readValue(new File(url.toURI()), new TypeReference<List<MapaPlanetaAspecto>>(){});
            System.out.println("ASPECTOS RESTAURADO COM SUCESSO");
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void restaurarPlanetasCasas() {
        ObjectMapper om = new ObjectMapper();
        try {
            URL url = this.getClass().getClassLoader().getResource(ARQUIVO_PLANETAS_CASAS);
            listaPlanetasCasas = om.readValue(new File(url.toURI()), new TypeReference<List<PlanetaCasa>>(){});
            System.out.println("PLANETAS CASAS RESTAURADO COM SUCESSO");
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void restaurarPlanetasSignos() {
        ObjectMapper om = new ObjectMapper();
        try {
            URL url = this.getClass().getClassLoader().getResource(ARQUIVO_PLANETAS_SIGNOS);
            listaPlanetasSignos = om.readValue(new File(url.toURI()), new TypeReference<List<PlanetaSigno>>(){});
            System.out.println("PLANETAS SIGNOS RESTAURADO COM SUCESSO");
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void restaurarMapaElementos() {
        this.listaMapaElementos = new ArrayList<>();
        listaMapaElementos.add(buildElementoFogo());
        listaMapaElementos.add(buildElementoTerra());
        listaMapaElementos.add(buildElementoAgua());
        listaMapaElementos.add(buildElementoAr());
        System.out.println("ELEMENTOS RESTAURADO COM SUCESSO");
    }

    private MapaElemento buildElementoFogo() {
        StringBuilder sbEstiloVida = new StringBuilder();
        sbEstiloVida.append("Os signos do fogo são ávidos por experiências e tem uma atitude alegre diante da vida. "+
                        "O fogo é o elemento da criação, da força vital que se manifesta. "+
                        "É a energia mais ativa e intensa, por isso vai em busca de inspiração e significado. "+
                        "Tem uma visão para novos horizontes, possibilidades  e regeneração.\n"+
                        "As pessoas nascidas sob o elemento fogo tem processos rápidos de pensamento e agem, muitas vezes,"+
                        " intuitivamente. São mentes capaz de tirar inspiração \"do nada\". "+
                "Dando grandes saltos de compreensão ou conclusões imediatas.\n");

        StringBuilder sbEquilibrio = new StringBuilder();
        sbEquilibrio.append("Quando os planetas estão situados no mapa de modo que o efeito do fogo fique equilibrado, "+
                "a personalidade é otimista, intuitiva, espontânea afetiva e inspiradora. ");

        StringBuilder sbDesquilibrio = new StringBuilder();
        sbDesquilibrio.append("Em excesso é furioso, violento e propenso a acidentes. "+
                "Fogo demais pode levar ao entusiasmo exagerado e a uma série de projetos que nunca são concluídos. "+
                "Já a deficiência de fogo pode resultar em baixa resistência à infecção, má digestão, frieza, apatia, "+
                "falta de ímpeto coragem e perseverança. Sem energia e criatividade, não há regeneração.");

        return MapaElemento.builder()
                .elemento(EnumElemento.FOGO)
                .signos("Áries, Leão, Sagitário")
                .planetas("Sol, Marte, Júpiter, Quíron (asteroide) ")
                .tipo("Intuitivo")
                .polaridade("Positiva")
                .palavrasChave("Energia, visão, criatividade, intuição, conquista, entusiasmo, motivação, aspirações, "+
                        "esgotamento, volátil, impaciente")
                .fisiologia("Digestão, pela, temperatura corporal, febre, inflamação, cortes, contusões, cicatrizes")
                .aparencia("Estatura média, verrugas, sardas, espinhas, tendência à vermelhidão")
                .temperamento("Corajoso, esquentado, dominador, espontâneo")
                .associacoes("Criatividade, força vital, intuição, otimismo, conhecimento, inteligência, calor, fruta e sementes.")
                .estiloVida("Vivendo do espírito")
                .textoEstiloVida(sbEstiloVida.toString())
                .textoEquilibrio(sbEquilibrio.toString())
                .textoDesequilibrio(sbDesquilibrio.toString())
                .build();
    }

    private MapaElemento buildElementoAr() {
        StringBuilder sbEstiloVida = new StringBuilder();

        sbEstiloVida.append("O ar é o mais intelectual e inovador dos elementos. "+
                        "Despreocupado com o lado material da vida, é um elemento de conexão, movido pelo desejo de se "+
                        "comunicar e de compartilhar o pensamento. O ar é estimulado pela discussão e põe a "+
                        "concordância mental acima da paixão.\n");
        sbEstiloVida.append("Um mapa com ênfase no ar indica uma pessoa inovadora e idealista. Embora não seja o mais "+
                        "prático dos elementos - em geral precisa dos outros para concluir as coisas - o ar é instigante. "+
                        "A personalidade é racional, porém intuitiva, capaz de reunir uma grande quantidade de dados e "+
                        "então os processar para dar saltos de compreensão, sem perceber todas as pessoas e conexões envolvidos.");

        StringBuilder sbEquilibrio = new StringBuilder();
        sbEquilibrio.append("O ar é gracioso, objetivo, sociável e justo. Quando a influência do ar é ótima, "+
                "a comunicação é aberta e honesta. A mente é vivaz e intuitiva e ainda assim racional.");

        StringBuilder sbDesquilibrio = new StringBuilder();
        sbDesquilibrio.append("O ar em excesso é inquieto, nervoso, ansioso, instável e distante. Ar demais resulta numa "+
                "mente borbulhante incapaz de se concentrar. O discurso é rápido e nem sempre lúcido, havendo "+
                "probabilidade de problemas como gagueira e dislexia. A deficiência de ar resulta em lassidão, "+
                "introversão, estagnação e falta de percepção. Sem ar, a mente tende a ser irracional ou lenta. "+
                "Esse estado é também associado à dificuldade de se fazer entender.");

        return MapaElemento.builder()
                .elemento(EnumElemento.AR)
                .signos("Gêmeos, Libra, Aquário")
                .planetas("Mercúrio, Urano")
                .tipo("Pensador")
                .polaridade("Positiva")
                .palavrasChave("Intelecto, comunicação, ideias, concordância mental, tecnologia, conceitos, conhecimento, inovação, planejamento")
                .fisiologia("Sistema nervoso, condutores, canais e cavidades, processos cognitivos, respiração, coordenação, movimento, eliminação")
                .aparencia("Magro, delgado, delicado, cabelo fino")
                .temperamento("Vivo e mercurial, com um jeito rápido de falar")
                .associacoes("Mente, movimento, comunicação, livros, graça, companheirismo, aversão ao frio, computadores, televisão, clima do deserto, flores")
                .estiloVida("Vivendo da mente")
                .textoEstiloVida(sbEstiloVida.toString())
                .textoEquilibrio(sbEquilibrio.toString())
                .textoDesequilibrio(sbDesquilibrio.toString())
                .build();
    }

    private MapaElemento buildElementoAgua() {
        StringBuilder sbEstiloVida = new StringBuilder();
        sbEstiloVida.append("A água é o mais sutil e sensitivo dos elementos. Os três introspectivos signos da água fluem com o próprio "+
        "ritmo interior, atentos ao chamado de fortes emoções. A água é associada a sentimentos, ciclos e flutuações. "+
        "A motivação vem de dentro, sendo muitas vezes uma reação inconsciente a um estímulo emocional. "+
        "É o elemento mais irracional e o que menos consciência tem do que lhe é exterior. "+
        "Sintonizadas com nuances delicadas, podem se dependentes e vulneráveis, às vezes interpretando mal os sinais, "+
        "que enxergam por meio das lentes das próprias emoções. Seu nível interior de conhecimento que vai além "+
        "dos cinco sentidos. É o elemento da percepção e da precognição.");

        StringBuilder sbEquilibrio = new StringBuilder();
        sbEquilibrio.append("A água é calma, suave, gentil, sensível e empática, com temperamento sereno. "+
                "Nesse elemento, a harmonia se traduz em alguém que é emocional, mas não é governado pelas emoções.");

        StringBuilder sbDesquilibrio = new StringBuilder();
        sbDesquilibrio.append("Em excesso, a água é muito emocional, apreensiva, atenta demais à segurança, auto-indulgente, "+
                "sonhadora e sensual, podendo tender a sobrepeso. A deficiência de água leva à rigidez e à falta de "+
                "ritmo, de empatia e de ligações emocionais. Sem água suficiente, as emoções caem na indiferença, "+
                "uma tendência que se disfarça de objetividade imparcial.");

        return MapaElemento.builder()
                .elemento(EnumElemento.AGUA)
                .signos("Câncer, Escorpião, Peixes")
                .planetas("Lua, Netuno")
                .tipo("Sensível")
                .polaridade("Negativa")
                .palavrasChave("Sentimentos, fluidez, ciclos, passividade, sensitividade, empatia, reflexão, compaixão, "+
                        "imaginação, intuição, ilusão")
                .fisiologia("Sistema de lubrificação e resfriamento; linfa, sangue, plasma secreções")
                .aparencia("Roliça, carnuda, com olhos grandes e suaves e pele brilhante")
                .temperamento("Tranquilo, suave, com fala monótona")
                .associacoes("Emoções, limpeza, empatia, suavidade, umidade, nutrição, tronco das árvores ou caule das flores")
                .estiloVida("Vivendo das emoções")
                .textoEstiloVida(sbEstiloVida.toString())
                .textoEquilibrio(sbEquilibrio.toString())
                .textoDesequilibrio(sbDesquilibrio.toString())
                .build();
    }

    private MapaElemento buildElementoTerra() {
        StringBuilder sbEstiloVida = new StringBuilder();
        sbEstiloVida.append("A terra é o elemento que prioriza a segurança e é o mais prático dos elementos. "+
                "É o elemento que extrai a forma do caos e molda a matéria. As pessoas nascidas sob a terra podem ter "+
                "características sólidas e duradouras. Fértil e tangível, esse elemento é relacionado ao corpo físico, "+
                "sentidos e a sensualidade. As pessoas com ênfase no elemento terra usam os cinco sentidos para "+
                "interagir com o mundo a sua volta e compreendê-lo, concentrando-se nas necessidades básicas, "+
                "como sobrevivência, segurança, alimentação e calor. "+
                "Geralmente são determinados e comprometidos em \"levar as coisas até o fim\".");

        StringBuilder sbEquilibrio = new StringBuilder();
        sbEquilibrio.append("Se configura como tolerante, paciente, constante e realista, com sólido sistema de valores. "+
                "Com a terra funcionando em níveis ótimos, as tarefas são realizadas com eficiência e concluídas.");

        StringBuilder sbDesquilibrio = new StringBuilder();
        sbEquilibrio.append("O excesso de terra é inflexível e lento para pensar e compreender. Indica uma tendência a "+
                        "dormir demais e uma forte resistência à mudança. O corpo e as emoções se tornam tóxicos, "+
                        "exigindo uma limpeza constante.\n");
        sbDesquilibrio.append("A deficiência de terra resulta em toxicidade em todos os níveis, especialmente no corpo. "+
                "Essa personalidade tende a ser instável, com energia dispersa e a visão tolhida. "+
                "A falta de terra favorece o comportamento irracional de alguém que vive totalmente dentro da cabeça, ou imerso em emoções.");

        return MapaElemento.builder()
                .elemento(EnumElemento.TERRA)
                .signos("Touro, Virgem, Capricórnio")
                .planetas("Saturno")
                .tipo("Sensorial")
                .polaridade("Negativa")
                .palavrasChave("Forma, produtividade, praticidade, fertilidade, sensualidade, necessidades básicas, segurança, o corpo, prudência")
                .fisiologia("Processos anabólicos, olfato, ossos, dentes, unhas, estrutura")
                .aparencia("Corpo firme e sólido, com um físico bem desenvolvido")
                .temperamento("Digno e conservador, com um jeito lento e ponderado de falar")
                .associacoes("O corpo físico e os sentidos, fertilidade, coragem, permanência, praticidade, fincar raízes")
                .estiloVida("Vivendo da matéria")
                .textoEstiloVida(sbEstiloVida.toString())
                .textoEquilibrio(sbEquilibrio.toString())
                .textoDesequilibrio(sbDesquilibrio.toString())
                .build();
    }

    private void restaurarMapaQualidades() {
        this.listaMapaQualidades = new ArrayList<>();
        listaMapaQualidades.add(buildQualidadeCardinal());
        listaMapaQualidades.add(buildQualidadeFixo());
        listaMapaQualidades.add(buildQualidadeMutavel());
        System.out.println("QUALIDADES RESTAURADO COM SUCESSO");
    }

    private MapaQualidade buildQualidadeCardinal() {
        return MapaQualidade.builder()
                .qualidade(EnumQualidade.CARDINAL)
                .signos("Áries, Câncer, Libra, Capricórnio")
                .casas("Primeira, Quarta, Sétima, Décima")
                .palavrasChave("Ação, iniciativa, desafio, asserção, iniciação, mudança, liderança, impetuosidade.")
                .personalidade("Iniciam mudanças e pões as coisas em movimento. São mais empreendedores, "+
                        "vigorosos e pró-ativos. Valente, assertivo e às vezes agressivo pode levar outras pessoas à ação. "+
                        "Líderes naturais, têm dificuldades de aceitar ordens.")
                .build();
    }

    private MapaQualidade buildQualidadeFixo() {
        return MapaQualidade.builder()
                .qualidade(EnumQualidade.FIXO)
                .signos("Touro, Leão, Escorpião, Aquário")
                .casas("Segunda, Quinta, Oitava, Décima primeira")
                .palavrasChave("Consistência, obstinado, rígido, resistente, estável, firme, leal, arraigado, previsível, fixação.")
                .personalidade("São resistentes à mudanças, preferindo uma rotina rígida e um estilo de vida estável. "+
                        "Prefere que tudo continue igual - sempre. Essa qualidade se reflete na força e na consistência, "+
                        "tornando - o mais confiável entre os outros. É uma personalidade leal, que leva tudo até o fim, "+
                        "mesmo quando o mais sensato seria desistir.")
                .build();
    }

    private MapaQualidade buildQualidadeMutavel() {
        return MapaQualidade.builder()
                .qualidade(EnumQualidade.MUTAVEL)
                .signos("Gêmeos, Virgem, Sagitário, Peixes")
                .casas("Terceira, Sexta, Nona, Décima segunda")
                .palavrasChave("Flexível, adaptável, versátil, mutável, imprevisível, instável, superficial")
                .personalidade("Gêmeos, Sagitário e Peixes são mutáveis duais, ou seja, tem por natureza dois lados "+
                        "distintos, movendo-se facilmente entre os dois ou assumindo duas perspectivas diferentes. "+
                        "Virgem é a exceção, pois prefere um único ponto de vista. "+
                        "Essa qualidade se adapta e reage às necessidade do ambiente, criando personalidades versáteis. "+
                        "Os signos mutáveis se divertem com exploração e mudança. "+
                        "Tem dificuldade de manter uma rotina porque logo se entediam. "+
                        "Não gostam de seguir ordens ou protocolos, principalmente quando lhes parece sem sentido. "+
                        "São excelentes numa equipe porque trabalham para o bem de todos.")
                .build();
    }

    private TipoAspecto getTipoAspecto(String aspecto) {
        TipoAspecto ta = null;
        try {
            ta = TipoAspecto.valueOf(aspecto.toUpperCase());
        } catch(Exception e) {
            ta = TipoAspecto.XX;
        }
        return ta;
    }

    private TipoSigno getTipoSigno(String signo) {
        TipoSigno ts = null;
        try {
            ts = TipoSigno.valueOf(signo.toUpperCase());
        } catch(Exception e) {
            ts = TipoSigno.XX;
        }
        return ts;
    }

    private TipoPlaneta getTipoPlaneta(String planeta) {
        TipoPlaneta tp = null;
        try {
            tp = TipoPlaneta.valueOf(planeta.toUpperCase());
        } catch(Exception e) {
            tp = TipoPlaneta.XXX;
        }
        return tp;
    }

}
