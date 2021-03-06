package br.itarocha.cartanatal.core.service;

import br.itarocha.cartanatal.core.model.*;
import br.itarocha.cartanatal.core.model.domain.Cidade;
import br.itarocha.cartanatal.core.model.domain.EnumElemento;
import br.itarocha.cartanatal.core.model.domain.EnumQualidade;
import br.itarocha.cartanatal.core.model.interpretacao.*;
import br.itarocha.cartanatal.core.util.Funcoes;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static br.itarocha.cartanatal.core.service.ArquivosConstantes.*;

@Service
public class BuscadorService {

    private List<Cidade> listaCidades;
    private List<SignoSolar> listaSignosSolares;
    private List<MapaCuspide> listaCuspides;
    private List<MapaPlanetaAspecto> listaAspectos;
    private List<PlanetaCasa> listaPlanetasCasas;
    private List<PlanetaSigno> listaPlanetasSignos;
    private List<MapaElemento> listaMapaElementos;
    private List<MapaQualidade> listaMapaQualidades;

    public BuscadorService(){
        restaurarCidades();
        restaurarSignosSolares();
        restaurarCuspides();
        restaurarAspectos();
        restaurarPlanetasCasas();
        restaurarPlanetasSignos();
        restaurarMapaElementos();
        restaurarMapaQualidades();
    }

    public Cidade findCidade(String cidade, String uf) {
        String key = buildKey(cidade, uf);

        return listaCidades.stream()
                .filter(c -> c.getKey().equals(key))
                .findFirst()
                .orElse(null);
    }

    private String buildKey(String cidade, String uf) {
        String s = Funcoes.removerAcentos(cidade).toUpperCase();
        s = s.replaceAll(" ","");
        return String.format("%s.%s", uf, s);
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

    private void restaurarCidades() {
        ObjectMapper om = new ObjectMapper();
        try {
            URL url = this.getClass().getClassLoader().getResource(ARQUIVO_CIDADES_BRASIL);
            listaCidades = om.readValue(url, new TypeReference<List<Cidade>>(){});
            System.out.println("ARQUIVO DE CIDADES RESTAURADO COM SUCESSO");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void restaurarSignosSolares() {
        ObjectMapper om = new ObjectMapper();
        try {
            URL url = this.getClass().getClassLoader().getResource(ARQUIVO_SIGNOS_SOLARES);
            //File f = new File(url);
            listaSignosSolares = om.readValue(url, new TypeReference<List<SignoSolar>>(){});
            //listaSignosSolares = om.readValue(new File(url.toURI()), new TypeReference<List<SignoSolar>>(){});
            System.out.println("ARQUIVO DE SIGNOS SOLARES RESTAURADO COM SUCESSO");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void restaurarCuspides() {
        ObjectMapper om = new ObjectMapper();
        try {
            URL url = this.getClass().getClassLoader().getResource(ARQUIVO_CUSPIDES);
            //listaCuspides = om.readValue(new File(url.toURI()), new TypeReference<List<MapaCuspide>>(){});
            listaCuspides = om.readValue(url, new TypeReference<List<MapaCuspide>>(){});
            System.out.println("ARQUIVO DE CUSPIDES RESTAURADO COM SUCESSO");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void restaurarAspectos() {
        ObjectMapper om = new ObjectMapper();
        try {
            URL url = this.getClass().getClassLoader().getResource(ARQUIVO_ASPECTOS);
            listaAspectos = om.readValue(url, new TypeReference<List<MapaPlanetaAspecto>>(){});
            System.out.println("ARQUIVO DE ASPECTOS RESTAURADO COM SUCESSO");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void restaurarPlanetasCasas() {
        ObjectMapper om = new ObjectMapper();
        try {
            URL url = this.getClass().getClassLoader().getResource(ARQUIVO_PLANETAS_CASAS);
            listaPlanetasCasas = om.readValue(url, new TypeReference<List<PlanetaCasa>>(){});
            System.out.println("ARQUIVO DE PLANETAS CASAS RESTAURADO COM SUCESSO");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void restaurarPlanetasSignos() {
        ObjectMapper om = new ObjectMapper();
        try {
            URL url = this.getClass().getClassLoader().getResource(ARQUIVO_PLANETAS_SIGNOS);
            listaPlanetasSignos = om.readValue(url, new TypeReference<List<PlanetaSigno>>(){});
            System.out.println("ARQUIVO DE PLANETAS SIGNOS RESTAURADO COM SUCESSO");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void restaurarMapaElementos() {
        this.listaMapaElementos = new ArrayList<>();
        listaMapaElementos.add(buildElementoFogo());
        listaMapaElementos.add(buildElementoTerra());
        listaMapaElementos.add(buildElementoAgua());
        listaMapaElementos.add(buildElementoAr());
        System.out.println("ARQUIVO DE ELEMENTOS RESTAURADO COM SUCESSO");
    }

    private MapaElemento buildElementoFogo() {
        StringBuilder sbEstiloVida = new StringBuilder();
        sbEstiloVida.append("Os signos do fogo s??o ??vidos por experi??ncias e tem uma atitude alegre diante da vida. "+
                        "O fogo ?? o elemento da cria????o, da for??a vital que se manifesta. "+
                        "?? a energia mais ativa e intensa, por isso vai em busca de inspira????o e significado. "+
                        "Tem uma vis??o para novos horizontes, possibilidades  e regenera????o.\n"+
                        "As pessoas nascidas sob o elemento fogo tem processos r??pidos de pensamento e agem, muitas vezes,"+
                        " intuitivamente. S??o mentes capaz de tirar inspira????o \"do nada\". "+
                "Dando grandes saltos de compreens??o ou conclus??es imediatas.\n");

        StringBuilder sbEquilibrio = new StringBuilder();
        sbEquilibrio.append("Quando os planetas est??o situados no mapa de modo que o efeito do fogo fique equilibrado, "+
                "a personalidade ?? otimista, intuitiva, espont??nea afetiva e inspiradora. ");

        StringBuilder sbDesquilibrio = new StringBuilder();
        sbDesquilibrio.append("Em excesso ?? furioso, violento e propenso a acidentes. "+
                "Fogo demais pode levar ao entusiasmo exagerado e a uma s??rie de projetos que nunca s??o conclu??dos. "+
                "J?? a defici??ncia de fogo pode resultar em baixa resist??ncia ?? infec????o, m?? digest??o, frieza, apatia, "+
                "falta de ??mpeto coragem e perseveran??a. Sem energia e criatividade, n??o h?? regenera????o.");

        return MapaElemento.builder()
                .elemento(EnumElemento.FOGO)
                .signos("??ries, Le??o, Sagit??rio")
                .planetas("Sol, Marte, J??piter, Qu??ron (asteroide) ")
                .tipo("Intuitivo")
                .polaridade("Positiva")
                .palavrasChave("Energia, vis??o, criatividade, intui????o, conquista, entusiasmo, motiva????o, aspira????es, "+
                        "esgotamento, vol??til, impaciente")
                .fisiologia("Digest??o, pela, temperatura corporal, febre, inflama????o, cortes, contus??es, cicatrizes")
                .aparencia("Estatura m??dia, verrugas, sardas, espinhas, tend??ncia ?? vermelhid??o")
                .temperamento("Corajoso, esquentado, dominador, espont??neo")
                .associacoes("Criatividade, for??a vital, intui????o, otimismo, conhecimento, intelig??ncia, calor, fruta e sementes.")
                .estiloVida("Vivendo do esp??rito")
                .textoEstiloVida(sbEstiloVida.toString())
                .textoEquilibrio(sbEquilibrio.toString())
                .textoDesequilibrio(sbDesquilibrio.toString())
                .build();
    }

    private MapaElemento buildElementoAr() {
        StringBuilder sbEstiloVida = new StringBuilder();

        sbEstiloVida.append("O ar ?? o mais intelectual e inovador dos elementos. "+
                        "Despreocupado com o lado material da vida, ?? um elemento de conex??o, movido pelo desejo de se "+
                        "comunicar e de compartilhar o pensamento. O ar ?? estimulado pela discuss??o e p??e a "+
                        "concord??ncia mental acima da paix??o.\n");
        sbEstiloVida.append("Um mapa com ??nfase no ar indica uma pessoa inovadora e idealista. Embora n??o seja o mais "+
                        "pr??tico dos elementos - em geral precisa dos outros para concluir as coisas - o ar ?? instigante. "+
                        "A personalidade ?? racional, por??m intuitiva, capaz de reunir uma grande quantidade de dados e "+
                        "ent??o os processar para dar saltos de compreens??o, sem perceber todas as pessoas e conex??es envolvidos.");

        StringBuilder sbEquilibrio = new StringBuilder();
        sbEquilibrio.append("O ar ?? gracioso, objetivo, soci??vel e justo. Quando a influ??ncia do ar ?? ??tima, "+
                "a comunica????o ?? aberta e honesta. A mente ?? vivaz e intuitiva e ainda assim racional.");

        StringBuilder sbDesquilibrio = new StringBuilder();
        sbDesquilibrio.append("O ar em excesso ?? inquieto, nervoso, ansioso, inst??vel e distante. Ar demais resulta numa "+
                "mente borbulhante incapaz de se concentrar. O discurso ?? r??pido e nem sempre l??cido, havendo "+
                "probabilidade de problemas como gagueira e dislexia. A defici??ncia de ar resulta em lassid??o, "+
                "introvers??o, estagna????o e falta de percep????o. Sem ar, a mente tende a ser irracional ou lenta. "+
                "Esse estado ?? tamb??m associado ?? dificuldade de se fazer entender.");

        return MapaElemento.builder()
                .elemento(EnumElemento.AR)
                .signos("G??meos, Libra, Aqu??rio")
                .planetas("Merc??rio, Urano")
                .tipo("Pensador")
                .polaridade("Positiva")
                .palavrasChave("Intelecto, comunica????o, ideias, concord??ncia mental, tecnologia, conceitos, conhecimento, inova????o, planejamento")
                .fisiologia("Sistema nervoso, condutores, canais e cavidades, processos cognitivos, respira????o, coordena????o, movimento, elimina????o")
                .aparencia("Magro, delgado, delicado, cabelo fino")
                .temperamento("Vivo e mercurial, com um jeito r??pido de falar")
                .associacoes("Mente, movimento, comunica????o, livros, gra??a, companheirismo, avers??o ao frio, computadores, televis??o, clima do deserto, flores")
                .estiloVida("Vivendo da mente")
                .textoEstiloVida(sbEstiloVida.toString())
                .textoEquilibrio(sbEquilibrio.toString())
                .textoDesequilibrio(sbDesquilibrio.toString())
                .build();
    }

    private MapaElemento buildElementoAgua() {
        StringBuilder sbEstiloVida = new StringBuilder();
        sbEstiloVida.append("A ??gua ?? o mais sutil e sensitivo dos elementos. Os tr??s introspectivos signos da ??gua fluem com o pr??prio "+
        "ritmo interior, atentos ao chamado de fortes emo????es. A ??gua ?? associada a sentimentos, ciclos e flutua????es. "+
        "A motiva????o vem de dentro, sendo muitas vezes uma rea????o inconsciente a um est??mulo emocional. "+
        "?? o elemento mais irracional e o que menos consci??ncia tem do que lhe ?? exterior. "+
        "Sintonizadas com nuances delicadas, podem se dependentes e vulner??veis, ??s vezes interpretando mal os sinais, "+
        "que enxergam por meio das lentes das pr??prias emo????es. Seu n??vel interior de conhecimento que vai al??m "+
        "dos cinco sentidos. ?? o elemento da percep????o e da precogni????o.");

        StringBuilder sbEquilibrio = new StringBuilder();
        sbEquilibrio.append("A ??gua ?? calma, suave, gentil, sens??vel e emp??tica, com temperamento sereno. "+
                "Nesse elemento, a harmonia se traduz em algu??m que ?? emocional, mas n??o ?? governado pelas emo????es.");

        StringBuilder sbDesquilibrio = new StringBuilder();
        sbDesquilibrio.append("Em excesso, a ??gua ?? muito emocional, apreensiva, atenta demais ?? seguran??a, auto-indulgente, "+
                "sonhadora e sensual, podendo tender a sobrepeso. A defici??ncia de ??gua leva ?? rigidez e ?? falta de "+
                "ritmo, de empatia e de liga????es emocionais. Sem ??gua suficiente, as emo????es caem na indiferen??a, "+
                "uma tend??ncia que se disfar??a de objetividade imparcial.");

        return MapaElemento.builder()
                .elemento(EnumElemento.AGUA)
                .signos("C??ncer, Escorpi??o, Peixes")
                .planetas("Lua, Netuno")
                .tipo("Sens??vel")
                .polaridade("Negativa")
                .palavrasChave("Sentimentos, fluidez, ciclos, passividade, sensitividade, empatia, reflex??o, compaix??o, "+
                        "imagina????o, intui????o, ilus??o")
                .fisiologia("Sistema de lubrifica????o e resfriamento; linfa, sangue, plasma secre????es")
                .aparencia("Roli??a, carnuda, com olhos grandes e suaves e pele brilhante")
                .temperamento("Tranquilo, suave, com fala mon??tona")
                .associacoes("Emo????es, limpeza, empatia, suavidade, umidade, nutri????o, tronco das ??rvores ou caule das flores")
                .estiloVida("Vivendo das emo????es")
                .textoEstiloVida(sbEstiloVida.toString())
                .textoEquilibrio(sbEquilibrio.toString())
                .textoDesequilibrio(sbDesquilibrio.toString())
                .build();
    }

    private MapaElemento buildElementoTerra() {
        StringBuilder sbEstiloVida = new StringBuilder();
        sbEstiloVida.append("A terra ?? o elemento que prioriza a seguran??a e ?? o mais pr??tico dos elementos. "+
                "?? o elemento que extrai a forma do caos e molda a mat??ria. As pessoas nascidas sob a terra podem ter "+
                "caracter??sticas s??lidas e duradouras. F??rtil e tang??vel, esse elemento ?? relacionado ao corpo f??sico, "+
                "sentidos e a sensualidade. As pessoas com ??nfase no elemento terra usam os cinco sentidos para "+
                "interagir com o mundo a sua volta e compreend??-lo, concentrando-se nas necessidades b??sicas, "+
                "como sobreviv??ncia, seguran??a, alimenta????o e calor. "+
                "Geralmente s??o determinados e comprometidos em \"levar as coisas at?? o fim\".");

        StringBuilder sbEquilibrio = new StringBuilder();
        sbEquilibrio.append("Se configura como tolerante, paciente, constante e realista, com s??lido sistema de valores. "+
                "Com a terra funcionando em n??veis ??timos, as tarefas s??o realizadas com efici??ncia e conclu??das.");

        StringBuilder sbDesquilibrio = new StringBuilder();
        sbEquilibrio.append("O excesso de terra ?? inflex??vel e lento para pensar e compreender. Indica uma tend??ncia a "+
                        "dormir demais e uma forte resist??ncia ?? mudan??a. O corpo e as emo????es se tornam t??xicos, "+
                        "exigindo uma limpeza constante.\n");
        sbDesquilibrio.append("A defici??ncia de terra resulta em toxicidade em todos os n??veis, especialmente no corpo. "+
                "Essa personalidade tende a ser inst??vel, com energia dispersa e a vis??o tolhida. "+
                "A falta de terra favorece o comportamento irracional de algu??m que vive totalmente dentro da cabe??a, ou imerso em emo????es.");

        return MapaElemento.builder()
                .elemento(EnumElemento.TERRA)
                .signos("Touro, Virgem, Capric??rnio")
                .planetas("Saturno")
                .tipo("Sensorial")
                .polaridade("Negativa")
                .palavrasChave("Forma, produtividade, praticidade, fertilidade, sensualidade, necessidades b??sicas, seguran??a, o corpo, prud??ncia")
                .fisiologia("Processos anab??licos, olfato, ossos, dentes, unhas, estrutura")
                .aparencia("Corpo firme e s??lido, com um f??sico bem desenvolvido")
                .temperamento("Digno e conservador, com um jeito lento e ponderado de falar")
                .associacoes("O corpo f??sico e os sentidos, fertilidade, coragem, perman??ncia, praticidade, fincar ra??zes")
                .estiloVida("Vivendo da mat??ria")
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
                .signos("??ries, C??ncer, Libra, Capric??rnio")
                .casas("Primeira, Quarta, S??tima, D??cima")
                .palavrasChave("A????o, iniciativa, desafio, asser????o, inicia????o, mudan??a, lideran??a, impetuosidade.")
                .personalidade("Iniciam mudan??as e p??es as coisas em movimento. S??o mais empreendedores, "+
                        "vigorosos e pr??-ativos. Valente, assertivo e ??s vezes agressivo pode levar outras pessoas ?? a????o. "+
                        "L??deres naturais, t??m dificuldades de aceitar ordens.")
                .build();
    }

    private MapaQualidade buildQualidadeFixo() {
        return MapaQualidade.builder()
                .qualidade(EnumQualidade.FIXO)
                .signos("Touro, Le??o, Escorpi??o, Aqu??rio")
                .casas("Segunda, Quinta, Oitava, D??cima primeira")
                .palavrasChave("Consist??ncia, obstinado, r??gido, resistente, est??vel, firme, leal, arraigado, previs??vel, fixa????o.")
                .personalidade("S??o resistentes ?? mudan??as, preferindo uma rotina r??gida e um estilo de vida est??vel. "+
                        "Prefere que tudo continue igual - sempre. Essa qualidade se reflete na for??a e na consist??ncia, "+
                        "tornando - o mais confi??vel entre os outros. ?? uma personalidade leal, que leva tudo at?? o fim, "+
                        "mesmo quando o mais sensato seria desistir.")
                .build();
    }

    private MapaQualidade buildQualidadeMutavel() {
        return MapaQualidade.builder()
                .qualidade(EnumQualidade.MUTAVEL)
                .signos("G??meos, Virgem, Sagit??rio, Peixes")
                .casas("Terceira, Sexta, Nona, D??cima segunda")
                .palavrasChave("Flex??vel, adapt??vel, vers??til, mut??vel, imprevis??vel, inst??vel, superficial")
                .personalidade("G??meos, Sagit??rio e Peixes s??o mut??veis duais, ou seja, tem por natureza dois lados "+
                        "distintos, movendo-se facilmente entre os dois ou assumindo duas perspectivas diferentes. "+
                        "Virgem ?? a exce????o, pois prefere um ??nico ponto de vista. "+
                        "Essa qualidade se adapta e reage ??s necessidade do ambiente, criando personalidades vers??teis. "+
                        "Os signos mut??veis se divertem com explora????o e mudan??a. "+
                        "Tem dificuldade de manter uma rotina porque logo se entediam. "+
                        "N??o gostam de seguir ordens ou protocolos, principalmente quando lhes parece sem sentido. "+
                        "S??o excelentes numa equipe porque trabalham para o bem de todos.")
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
