package br.itarocha.cartanatal.core.service;

import br.itarocha.cartanatal.core.model.Casa;
import br.itarocha.cartanatal.core.model.Pair;
import br.itarocha.cartanatal.core.model.domain.*;
import br.itarocha.cartanatal.core.model.interpretacao.*;
import br.itarocha.cartanatal.core.model.presenter.*;
import br.itarocha.cartanatal.core.util.Simbolos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

import static br.itarocha.cartanatal.core.model.presenter.EstiloParagrafo.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class InterpretadorServiceNew {

	private static final String NOT_FOUND = "<NOT_FOUND>";
	private static final String LF = "\r\n\r\n";

	@Value("${parametros.arquivoImagemMapa}")
	private String arquivoImagemMapa;

	@Value("${parametros.arquivoImagemAspectos}")
	private String arquivoImagemAspectos;

	@Autowired
	private BuscadorService buscador;

    public List<Paragrafo> gerarInterpretacoes(CartaNatalResponse cartaNatal) {

    	List<Paragrafo> retorno = new ArrayList<>();

		retorno.add(buildParagrafoImagem(arquivoImagemMapa, 400, 400));
		retorno.add(buildParagrafoImagem(arquivoImagemAspectos, 450, 150));

		retorno.addAll(buildCabecalho(cartaNatal.getDadosPessoais()));
		retorno.addAll(buildIntroducao());
		retorno.addAll(interpretarElemento(cartaNatal.getPlanetasSignos()));
		retorno.addAll(interpretarQualidade(cartaNatal.getPlanetasSignos()));
		retorno.addAll(interpretarSignoSolar(cartaNatal.getPlanetasSignos()));
		retorno.addAll(interpretarPlanetasSignos(cartaNatal.getPlanetasSignos()));
		retorno.addAll(interpretarCuspidesTituloGeral());
		retorno.addAll(interpretarCuspides(cartaNatal.getCuspides()));
		retorno.addAll(interpretarPlanetasCasas(cartaNatal.getPlanetasSignos()));
		retorno.addAll(interpretarAspectos(cartaNatal.getAspectos()));

		return retorno;
	 }

	private List<Paragrafo> buildCabecalho(DadoPessoalResponse dadosPessoais) {
		List<Paragrafo> lista = new ArrayList<>();
    	lista.add(buildParagrafo(TITULO_PRINCIPAL,dadosPessoais.getNome()));
		lista.add(buildParagrafo(TITULO_PARAGRAFO,dadosPessoais.getData() + " "  + dadosPessoais.getHora()));
		lista.add(buildParagrafo(TITULO_PARAGRAFO,dadosPessoais.getCidade()));
		lista.add(buildParagrafo(TITULO_SIMPLES,String.format("Latitude: %s", dadosPessoais.getLat())));
		lista.add(buildParagrafo(TITULO_SIMPLES,String.format("Longitude: %s", dadosPessoais.getLon())));
		lista.add(buildParagrafo(TITULO_SIMPLES,String.format("Julian Day: %s", dadosPessoais.getJulDay())));
		lista.add(buildParagrafo(TITULO_SIMPLES,String.format("Delta T: %s sec", dadosPessoais.getDeltaT())));
		return lista;
	}

	private Paragrafo buildParagrafoImagem(String fileName, int width, int height) {
    	return Paragrafo.builder()
				.estilo(IMAGEM)
				.imagem(ParagrafoImagem.builder()
						.fileName(fileName)
						.width(width)
						.height(height)
						.build())
				.build();
	}

	private List<Paragrafo> buildIntroducao() {
		List<Paragrafo> lista = new ArrayList<>();

		StringBuilder sbAstrologia = new StringBuilder();
		sbAstrologia.append("A astrologia (do grego astron, \"astros\", \"estrelas\" ou \"corpo celeste\"; e logos, \"palavra\", \"estudo\") ?? uma ferramenta matem??tica, segundo a qual as posi????es relativas das estrelas poderiam prover informa????o sobre a personalidade, as rela????es humanas, e outros assuntos relacionados ?? vida do ser humano.");
		sbAstrologia.append("Os astr??logos estudam, h?? milhares de anos, os efeitos da atividade planet??ria e as suas correspond??ncias com o comportamento humano. Durante s??culos, a astrologia se baseou na observa????o de objetos celestes e no registro de seus movimentos. Mais recentemente, os astr??logos t??m usado dados coletados pelos astr??nomos e organizados em tabelas chamadas efem??rides, que mostram as posi????es dos corpos celestes.");

		lista.add(buildParagrafo(TITULO_PARAGRAFO,"Astrologia"));
		lista.add(buildParagrafo(PARAGRAFO_NORMAL,sbAstrologia.toString()));


		StringBuilder sbMapaNatal = new StringBuilder();
		sbMapaNatal.append("O Mapa Natal mostra a posi????o correta dos astros em rela????o ?? Terra no momento de nascimento "+
				"de uma determinada pessoa. Ele captura o momento no tempo e o congela. Da sua perspectiva na Terra, os "+
				"planetas parecem se mover ?? sua volta, de modo que voc?? est?? no centro do seu mapa astral. "+
				"As configura????es de um Mapa Natal se repetem apenas a cada 26.000 anos, portanto ele ?? quase como uma "+
				"impress??o digital - n??o existe um igual ao outro.");

		lista.add(buildParagrafo(TITULO_PARAGRAFO,"O Mapa Natal"));
		lista.add(buildParagrafo(PARAGRAFO_NORMAL,sbMapaNatal.toString()));

		StringBuilder sbZodiaco = new StringBuilder();
		sbZodiaco.append("A palavra zod??aco (do latim zodiacus - significa \"c??rculo de animais\") ?? uma faixa imagin??ria "+
				"do c??u que inclui as ??rbitas aparentes do Sol, da Lua e dos planetas Merc??rio, V??nus, Marte, J??piter, "+
				"Saturno, Urano e Netuno.");
		sbZodiaco.append("As divis??es do zod??aco representam constela????es na astronomia e signos na astrologia, que s??o "+
				"(??ries ou Carneiro, Touro, G??meos, C??ncer ou Caranguejo, Le??o, Virgem, Libra ou Balan??a, Escorpi??o, "+
				"Sagit??rio, Capric??rnio, Aqu??rio e Peixes).");

		lista.add(buildParagrafo(TITULO_PARAGRAFO,"O Zod??aco"));
		lista.add(buildParagrafo(PARAGRAFO_NORMAL,sbZodiaco.toString()));

		return lista;
	}

	private List<Paragrafo> interpretarElemento(List<PlanetaSignoResponse> planetasSignos) {
		List<Paragrafo> lista = new ArrayList<>();

		EnumSigno enumSigno = planetasSignos.stream()
					.filter(ps -> EnumPlaneta.SOL.equals( EnumPlaneta.getBySigla(ps.getPlaneta()) ))
					.map(ps -> EnumSigno.getBySigla(ps.getSigno()))
					.findFirst()
				.orElse(null);

		StringBuilder sbElemento = new StringBuilder();

		sbElemento.append("A energia dos planetas ?? filtrada pelos quatro elementos da natureza (terra, ??gua, ar e fogo). "+
				"Cada um est?? relacionado com uma determinada fun????o. A terra ?? relacionada ao corpo. "+
				"A ??gua ??s emo????es. O ar ?? mente, ou  intelecto. O fogo ao esp??rito. Os elementos podem representar "+
				"diferentes maneiras de se perceber a vida. A classifica????o por elementos ?? algo fundamental na "+
				"Astrologia e ela ?? decidida por meio dos dez planetas que comp??e o sistema astrol??gico e mais o Ascendente.");

		lista.add(buildParagrafo(TITULO_PARAGRAFO,"Os Elementos"));
		lista.add(buildParagrafo(PARAGRAFO_NORMAL,sbElemento.toString()));

		if (nonNull(enumSigno) && nonNull(enumSigno.getElemento()) ){
			EnumElemento elemento = enumSigno.getElemento();

			MapaElemento me = buscador.findElemento(elemento.getSigla());
			if (nonNull(me)){
				lista.add(Paragrafo.builder().estilo(TITULO_PARAGRAFO)
						.texto("Seu Elemento: "+elemento.getNome().toUpperCase(Locale.ROOT)).build());

				List<Pair> tabela = new ArrayList<>();
				tabela.add(new Pair("Signos",me.getSignos()));
				tabela.add(new Pair("Planetas",me.getPlanetas()));
				tabela.add(new Pair("Tipo",me.getTipo()));
				tabela.add(new Pair("Polaridade",me.getPolaridade()));
				tabela.add(new Pair("Palavras chave",me.getPalavrasChave()));
				tabela.add(new Pair("Fisiologia",me.getFisiologia()));
				tabela.add(new Pair("Apar??ncia",me.getAparencia()));
				tabela.add(new Pair("Temperamento",me.getTemperamento()));
				tabela.add(new Pair("Temperamento",me.getTemperamento()));
				tabela.add(new Pair("Associa????es",me.getAssociacoes()));
				Paragrafo paragrafo = Paragrafo.builder().estilo(TABELA).tabela(tabela).build();
				lista.add(paragrafo);

				lista.add(buildParagrafo(TITULO_PARAGRAFO,String.format("%s em equil??brio",elemento.getNome())));
				lista.add(buildParagrafo(PARAGRAFO_NORMAL,me.getTextoEquilibrio()));

				lista.add(buildParagrafo(TITULO_PARAGRAFO,String.format("%s em desequil??brio",elemento.getNome())));
				lista.add(buildParagrafo(PARAGRAFO_NORMAL,me.getTextoDesequilibrio()));
			}
		}
		return lista;
	}

	private List<Paragrafo> interpretarQualidade(List<PlanetaSignoResponse> planetasSignos) {
		List<Paragrafo> lista = new ArrayList<>();

		EnumSigno enumSigno = planetasSignos.stream()
				.filter(ps -> EnumPlaneta.SOL.equals( EnumPlaneta.getBySigla(ps.getPlaneta()) ))
				.map(ps -> EnumSigno.getBySigla(ps.getSigno()))
				.findFirst()
				.orElse(null);

		StringBuilder sbQualidade = new StringBuilder();

		sbQualidade.append("As qualidades regulam sutilmente a energia astrol??gica. "+
				"As quatro esta????es e cada um desses per??odos de tr??s meses de dura????o ?? composto de um come??o, meio e fim. "+
				"Todo m??s eles se manifestam na natureza de uma maneira variada."+LF);
		sbQualidade.append("Cada um dos quatro elementos apresenta uma express??o cardinal, uma express??o fixa e uma "+
				"express??o mut??vel. As qualidades influenciam a maneira da pessoa interagir com o ambiente e as pessoas.");

		lista.add(buildParagrafo(TITULO_PARAGRAFO,"As Qualidades"));

		List<String> lst = quebrarParagrafos(sbQualidade.toString());
		lst.stream().forEach(s -> {
			lista.add(buildParagrafo(PARAGRAFO_NORMAL,s));
		});

		if (nonNull(enumSigno) && nonNull(enumSigno.getQualidade()) ){
			EnumQualidade qualidade = enumSigno.getQualidade();

			MapaQualidade q = buscador.findQualidade(qualidade.getSigla());
			if (nonNull(q)){
				lista.add(buildParagrafo(TITULO_PARAGRAFO,"Sua qualidade: "+enumSigno.getQualidade().getNome()));

				List<Pair> tabela = new ArrayList<>();
				tabela.add(new Pair("Signos", q.getSignos()));
				tabela.add(new Pair("Casas", q.getCasas()));
				tabela.add(new Pair("Palavras chave", q.getPalavrasChave() ));
				tabela.add(new Pair("Personalidade", q.getPersonalidade()));
				Paragrafo paragrafo = Paragrafo.builder().estilo(TABELA).tabela(tabela).build();
				lista.add(paragrafo);
			}
		}
		return lista;
	}

	private List<Paragrafo> interpretarSignoSolar(List<PlanetaSignoResponse> planetasSignos) {
    	List<Paragrafo> lista = new ArrayList<>();

		planetasSignos.stream()
				.filter(ps -> EnumPlaneta.SOL.equals( EnumPlaneta.getBySigla(ps.getPlaneta()) ))
				.forEach( ps -> {
					SignoSolar signoSolarCabecalho = buscador.findSignoSolar("XX");

					String textoCabecalho = isNull(signoSolarCabecalho) ? null : signoSolarCabecalho.getTexto();
					lista.addAll(buildParagrafos("O Signo Solar", textoCabecalho));

					EnumSigno enumSigno = EnumSigno.getBySigla(ps.getSigno());
					SignoSolar signoSolar = buscador.findSignoSolar(enumSigno.getSigla());
					String key = String.format("%s %s", enumSigno.getNome(), Simbolos.getSimboloSigno(enumSigno.getSigla()));

					String texto = isNull(signoSolar) ? null : signoSolar.getTexto();
					lista.addAll(buildParagrafos(key, texto));

				});
		return lista;
	}

	/*
	public <T extends Building> void genericMethod(T t) {
    ...
	}
	*/

	private List<Paragrafo> interpretarPlanetasSignos(List<PlanetaSignoResponse> planetasSignos) {
		List<Paragrafo> lista = new ArrayList<>();

		EnumPlaneta[] array = {EnumPlaneta.SOL, EnumPlaneta.ASC, EnumPlaneta.MCE};
		List<EnumPlaneta> desconsiderados = Arrays.asList(array);

		planetasSignos.stream()
				.filter(ps -> !desconsiderados.contains(EnumPlaneta.getBySigla(ps.getPlaneta())) )
				.forEach(pp -> {
					EnumPlaneta enumPlaneta = EnumPlaneta.getBySigla(pp.getPlaneta());
					PlanetaSigno psTitulo = buscador.findPlanetaSigno(pp.getPlaneta(), "XX");
					String keyTitulo = String.format("%s nos Signos", enumPlaneta.getNome());

					String textoTitulo = isNull(psTitulo) ? null : psTitulo.getTexto();
					lista.addAll(buildParagrafos(keyTitulo, textoTitulo));

					PlanetaSigno ps = buscador.findPlanetaSigno(pp.getPlaneta(), pp.getSigno() );
					EnumSigno enumSigno = EnumSigno.getBySigla(pp.getSigno());
					String key = String.format("%s em %s (%s %s)", 	enumPlaneta.getNome(),
																	enumSigno.getNome(),
																	Simbolos.getSimboloPlaneta(enumPlaneta.getSigla()),
																	Simbolos.getSimboloSigno(enumSigno.getSigla()));

					String texto = isNull(ps) ? null : ps.getTexto();
					lista.addAll(buildParagrafos(key, texto));
				});
		return lista;
	}

	private List<Paragrafo> interpretarCuspidesTituloGeral() {
		List<Paragrafo> lista = new ArrayList<>();
		String keyTitulo = "As Casas";
		lista.add(Paragrafo.builder().estilo(TITULO_PARAGRAFO).texto(keyTitulo).build());
		MapaCuspide mc = buscador.findCuspide("XX", 0);

		String textoTitulo = isNull(mc) ? null : mc.getTexto();
		lista.addAll(buildParagrafos(keyTitulo, textoTitulo));

		return lista;
	}

	private List<Paragrafo> interpretarAspectos(List<AspectoResponse> aspectos) {
		List<Paragrafo> lista = new ArrayList<>();

    	aspectos.stream().forEach(ia -> {
			EnumPlaneta enumPlanetaOrigem = EnumPlaneta.getBySigla(ia.getPlanetaOrigem());
			EnumPlaneta enumPlanetaDestino = EnumPlaneta.getBySigla(ia.getPlanetaDestino());
			EnumAspecto enumAspecto = EnumAspecto.getBySigla(ia.getAspecto());

			String key = String.format("%s em %s com %s (%s %s %s)",enumPlanetaOrigem.getNome(),
																	enumAspecto.getNome(),
																	enumPlanetaDestino.getNome(),

																	Simbolos.getSimboloPlaneta(enumPlanetaOrigem.getSigla()),
																	Simbolos.getSimboloAspecto(enumAspecto.getSigla()),
																	Simbolos.getSimboloPlaneta(enumPlanetaDestino.getSigla())

																	);
			MapaPlanetaAspecto a = buscador.findAspecto(ia.getPlanetaOrigem(), ia.getPlanetaDestino(), enumAspecto.getSigla() );
			String textoTitulo = isNull(a) ? null : a.getTexto();
			lista.addAll(buildParagrafos(key, textoTitulo));
		});
    	return lista;
	}

	private List<Paragrafo> interpretarCuspides(List<CuspideResponse> cuspides) {
		List<Paragrafo> lista = new ArrayList<>();

		cuspides.stream()
				.filter(c -> c.getCasa() <= 12)
				.forEach(c -> {
					String keyTitulo = String.format("Casa %s", c.getCasa());
					MapaCuspide mcTitulo = buscador.findCuspide("XX", c.getCasa());

					String textoTitulo = isNull(mcTitulo) ? null : mcTitulo.getTexto();
					lista.addAll(buildParagrafos(keyTitulo, textoTitulo));

					EnumSigno enumSigno = EnumSigno.getBySigla(c.getSigno());
					String casa = Casa.getByNumero(c.getCasa());
					String key = String.format("%s na C??spide da %s Casa (%s %s)", enumSigno.getNome(), casa, Simbolos.getSimboloSigno(enumSigno.getSigla()), c.getCasa() );
					MapaCuspide mc = buscador.findCuspide(enumSigno.getSigla(), c.getCasa());

					String texto = isNull(mc) ? null : mc.getTexto();
					lista.addAll(buildParagrafos(key, texto));
				});
		return lista;
	}

	private List<Paragrafo> interpretarPlanetasCasas(List<PlanetaSignoResponse> planetasSignos) {
		List<Paragrafo> lista = new ArrayList<>();

		EnumPlaneta[] array = {EnumPlaneta.SOL, EnumPlaneta.ASC, EnumPlaneta.MCE};
		List<EnumPlaneta> desconsiderados = Arrays.asList(array);

		planetasSignos.stream()
				.filter(ps -> !desconsiderados.contains(EnumPlaneta.getBySigla(ps.getPlaneta())) )
				.forEach(pp -> {
					EnumPlaneta enumPlaneta = EnumPlaneta.getBySigla(pp.getPlaneta());
					String casa = Casa.getByNumero(pp.getCasa());

					String keyTitulo = String.format("%s nas Casas %s", enumPlaneta.getNome(), Simbolos.getSimboloPlaneta(enumPlaneta.getSigla()) );
					PlanetaCasa pcTitulo = buscador.findPlanetaCasa(pp.getPlaneta(), 0);

					String textoTitulo = isNull(pcTitulo) ? null : pcTitulo.getTexto();
					lista.addAll(buildParagrafos(keyTitulo, textoTitulo));

					String key = String.format("%s na %s Casa (%s %d)", enumPlaneta.getNome(), casa, Simbolos.getSimboloPlaneta(enumPlaneta.getSigla()), pp.getCasa());
					PlanetaCasa pc = buscador.findPlanetaCasa(pp.getPlaneta(), pp.getCasa());

					String texto = isNull(pc) ? null : pc.getTexto();
					lista.addAll(buildParagrafos(key, texto));

				});
		return lista;
	}

	private List<Paragrafo> buildParagrafos(String titulo, String texto) {
		List<Paragrafo> lista = new ArrayList<>();
		lista.add(buildParagrafo(TITULO_PARAGRAFO,titulo));
		if (isNull(texto)){
			lista.add(buildParagrafo(PARAGRAFO_NORMAL,NOT_FOUND));
		} else {
			//TODO: J?? deveria estar apenas com \n ao inv??s de \r\n\r\n
			List<String> lst = quebrarParagrafos(texto);
			lst.stream().forEach(s -> {
				lista.add(buildParagrafo(PARAGRAFO_NORMAL,s));
			});
		}
		return lista;
	}

	private Paragrafo buildParagrafo(EstiloParagrafo estilo, String texto){
		return Paragrafo.builder().estilo(estilo).texto(texto).build();
	}

	private List<String> quebrarParagrafos(String texto){
		List<String> retorno = new LinkedList<>();
		texto = texto.replace("\r", "");
		String[] aaa = texto.split("\n\\s+");
		for (int i = 0; i < aaa.length; i++ ) {
			retorno.add(aaa[i]);
		}
		return retorno;
	}

}

/*

class StrategyContext {
    double price; // price for some item or air ticket etc.
    Map<String,OfferStrategy> strategyContext = new HashMap<String,OfferStrategy>();
    StrategyContext(double price){
        this.price= price;
        strategyContext.put(NoDiscountStrategy.class.getName(),new NoDiscountStrategy());
        strategyContext.put(QuarterDiscountStrategy.class.getName(),new QuarterDiscountStrategy());
    }
    public void applyStrategy(OfferStrategy strategy){
        //Currently applyStrategy has simple implementation. You can Context for populating some more information,
        //which is required to call a particular operation
        System.out.println("Price before offer :"+price);
				double finalPrice = price - (price*strategy.getDiscountPercentage());
				System.out.println("Price after offer:"+finalPrice);
				}
public OfferStrategy getStrategy(int monthNo){
            //In absence of this Context method, client has to import relevant concrete Strategies everywhere.
            //Context acts as single point of contact for the Client to get relevant Strategy
		if ( monthNo < 6 )  {
		return strategyContext.get(NoDiscountStrategy.class.getName());
		}else{
		return strategyContext.get(QuarterDiscountStrategy.class.getName());
		}

		}
		}

public class StrategyDemo{
    public static void main(String args[]){
        StrategyContext context = new StrategyContext(100);
        System.out.println("Enter month number between 1 and 12");
        int month = Integer.parseInt(args[0]);
        System.out.println("Month ="+month);
        OfferStrategy strategy = context.getStrategy(month);
        context.applyStrategy(strategy);
    }

}

 */