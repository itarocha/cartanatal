package br.itarocha.cartanatal.core.service;

import br.itarocha.cartanatal.core.model.Casa;
import br.itarocha.cartanatal.core.model.Interpretacao;
import br.itarocha.cartanatal.core.model.domain.*;
import br.itarocha.cartanatal.core.model.interpretacao.*;
import br.itarocha.cartanatal.core.model.presenter.*;
import br.itarocha.cartanatal.core.util.Simbolos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class InterpretadorServiceNew {

	private static final String NOT_FOUND = "<NOT_FOUND>";
	private static final String LF = "\r\n\r\n";

	@Autowired
	private BuscadorService buscador;

    public List<Paragrafo> gerarInterpretacoes(CartaNatalResponse cartaNatal) {

    	List<Paragrafo> retorno = new ArrayList<>();

		// CABECALHO
		retorno.addAll(buildCabecalho(cartaNatal.getDadosPessoais()));

		retorno.addAll(buildIntroducao());

		retorno.addAll(interpretarElemento(cartaNatal.getPlanetasSignos()));

		retorno.addAll(interpretarQualidade(cartaNatal.getPlanetasSignos()));

		// SIGNO SOLAR
		retorno.addAll(interpretarSignoSolar(cartaNatal.getPlanetasSignos()));

		// PLANETAS NOS SIGNOS
		retorno.addAll(interpretarPlanetasSignos(cartaNatal.getPlanetasSignos()));

		// CÚSPIDES - TÍTULO GERAL
		retorno.addAll(interpretarCuspidesTituloGeral());

		// CÚSPIDES
		retorno.addAll(interpretarCuspides(cartaNatal.getCuspides()));

		// PLANETAS NAS CASAS
		retorno.addAll(interpretarPlanetasCasas(cartaNatal.getPlanetasSignos()));

		// ASPECTOS
		retorno.addAll(interpretarAspectos(cartaNatal.getAspectos()));

		/*
		// para cada chave de mapa, tratar paragrafos
		//retorno.add(this.tratarParagrafos(keyCabecalho, signoSolarCabecalho.getTexto()));
		/*
		List<Interpretacao> retorno = new LinkedList<>();

		map.entrySet().stream().forEach(entry -> {
			retorno.add(this.tratarParagrafos(entry.getKey(), entry.getValue() ));
		});
		 */
		return retorno;
	 }

	private List<Paragrafo> buildCabecalho(DadoPessoalResponse dadosPessoais) {
		List<Paragrafo> lista = new ArrayList<>();
    	lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_PRINCIPAL).texto(dadosPessoais.getNome()).build());
		lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_PARAGRAFO).texto(dadosPessoais.getData() + " "  + dadosPessoais.getHora()).build());
		lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_PARAGRAFO).texto(dadosPessoais.getCidade()).build());

		lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_SIMPLES).texto(String.format("Latitude: %s", dadosPessoais.getLat())).build());
		lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_SIMPLES).texto(String.format("Longitude: %s", dadosPessoais.getLon())).build());
		lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_SIMPLES).texto(String.format("Julian Day: %s", dadosPessoais.getJulDay())).build());
		lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_SIMPLES).texto(String.format("Delta T: %s sec", dadosPessoais.getDeltaT())).build());
		return lista;
	}

	private List<Paragrafo> buildIntroducao() {
		List<Paragrafo> lista = new ArrayList<>();

		StringBuilder sbAstrologia = new StringBuilder();
		sbAstrologia.append("A astrologia (do grego astron, \"astros\", \"estrelas\" ou \"corpo celeste\"; e logos, \"palavra\", \"estudo\") é uma ferramenta matemática, segundo a qual as posições relativas das estrelas poderiam prover informação sobre a personalidade, as relações humanas, e outros assuntos relacionados à vida do ser humano.");
		sbAstrologia.append("Os astrólogos estudam, há milhares de anos, os efeitos da atividade planetária e as suas correspondências com o comportamento humano. Durante séculos, a astrologia se baseou na observação de objetos celestes e no registro de seus movimentos. Mais recentemente, os astrólogos têm usado dados coletados pelos astrônomos e organizados em tabelas chamadas efemérides, que mostram as posições dos corpos celestes.");
		lista.add(this.buildParagrafo(EstiloParagrafo.TITULO_PARAGRAFO,"Astrologia"));
		lista.add(this.buildParagrafo(EstiloParagrafo.PARAGRAFO_NORMAL,sbAstrologia.toString()));

		StringBuilder sbMapaNatal = new StringBuilder();
		sbMapaNatal.append("O Mapa Natal mostra a posição correta dos astros em relação à Terra no momento de nascimento "+
				"de uma determinada pessoa. Ele captura o momento no tempo e o congela. Da sua perspectiva na Terra, os "+
				"planetas parecem se mover à sua volta, de modo que você está no centro do seu mapa astral. "+
				"As configurações de um Mapa Natal se repetem apenas a cada 26.000 anos, portanto ele é quase como uma "+
				"impressão digital - não existe um igual ao outro.");
		lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_PARAGRAFO).texto("O Mapa Natal").build());
		lista.add(Paragrafo.builder().estilo(EstiloParagrafo.PARAGRAFO_NORMAL).texto(sbMapaNatal.toString()).build());

		StringBuilder sbZodiaco = new StringBuilder();
		sbZodiaco.append("A palavra zodíaco (do latim zodiacus - significa \"círculo de animais\") é uma faixa imaginária "+
				"do céu que inclui as órbitas aparentes do Sol, da Lua e dos planetas Mercúrio, Vênus, Marte, Júpiter, "+
				"Saturno, Urano e Netuno.");
		sbZodiaco.append("As divisões do zodíaco representam constelações na astronomia e signos na astrologia, que são "+
				"(Áries ou Carneiro, Touro, Gêmeos, Câncer ou Caranguejo, Leão, Virgem, Libra ou Balança, Escorpião, "+
				"Sagitário, Capricórnio, Aquário e Peixes).");

		lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_PARAGRAFO).texto("O Zodíaco").build());
		lista.add(Paragrafo.builder().estilo(EstiloParagrafo.PARAGRAFO_NORMAL).texto(sbZodiaco.toString()).build());

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

		sbElemento.append("A energia dos planetas é filtrada pelos quatro elementos da natureza (terra, água, ar e fogo). "+
				"Cada um está relacionado com uma determinada função. A terra é relacionada ao corpo. "+
				"A água às emoções. O ar à mente, ou  intelecto. O fogo ao espírito. Os elementos podem representar "+
				"diferentes maneiras de se perceber a vida. A classificação por elementos é algo fundamental na "+
				"Astrologia e ela é decidida por meio dos dez planetas que compõe o sistema astrológico e mais o Ascendente.");

		lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_PARAGRAFO).texto("Os Elementos").build());
		lista.add(Paragrafo.builder().estilo(EstiloParagrafo.PARAGRAFO_NORMAL).texto(sbElemento.toString()).build());


		if (nonNull(enumSigno) && nonNull(enumSigno.getElemento()) ){
			EnumElemento elemento = enumSigno.getElemento();

			MapaElemento me = buscador.findElemento(elemento.getSigla());
			if (nonNull(me)){
				lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_PARAGRAFO)
						.texto("Seu Elemento: "+elemento.getNome().toUpperCase(Locale.ROOT)).build());

				// TODO será table
				lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_SIMPLES).texto(String.format("Signos: %s", me.getSignos())).build());
				lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_SIMPLES).texto(String.format("Planetas: %s", me.getPlanetas())).build());
				lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_SIMPLES).texto(String.format("Tipo: %s", me.getTipo())).build());
				lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_SIMPLES).texto(String.format("Polaridade: %s", me.getPolaridade() )).build());
				lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_SIMPLES).texto(String.format("Palavras chave: %s", me.getPalavrasChave())).build());
				lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_SIMPLES).texto(String.format("Fisiologia: %s", me.getFisiologia() )).build());
				lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_SIMPLES).texto(String.format("Aparência: %s", me.getAparencia() )).build());
				lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_SIMPLES).texto(String.format("Temperamento: %s", me.getTemperamento() )).build());
				lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_SIMPLES).texto(String.format("Associações: %s", me.getAssociacoes() )).build());

				lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_PARAGRAFO).texto(String.format("%s em equilíbrio",elemento.getNome())).build());
				lista.add(Paragrafo.builder().estilo(EstiloParagrafo.PARAGRAFO_NORMAL).texto(me.getTextoEquilibrio()).build());

				lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_PARAGRAFO).texto(String.format("%s em desequilíbrio",elemento.getNome())).build());
				lista.add(Paragrafo.builder().estilo(EstiloParagrafo.PARAGRAFO_NORMAL).texto(me.getTextoDesequilibrio()).build());
			}
		}

		return lista;
	}


	private List<Paragrafo>  interpretarQualidade(List<PlanetaSignoResponse> planetasSignos) {
		List<Paragrafo> lista = new ArrayList<>();

		EnumSigno enumSigno = planetasSignos.stream()
				.filter(ps -> EnumPlaneta.SOL.equals( EnumPlaneta.getBySigla(ps.getPlaneta()) ))
				.map(ps -> EnumSigno.getBySigla(ps.getSigno()))
				.findFirst()
				.orElse(null);

		StringBuilder sbQualidade = new StringBuilder();

		sbQualidade.append("As qualidades regulam sutilmente a energia astrológica. "+
				"As quatro estações e cada um desses períodos de três meses de duração é composto de um começo, meio e fim. "+
				"Todo mês eles se manifestam na natureza de uma maneira variada."+LF);
		sbQualidade.append("Cada um dos quatro elementos apresenta uma expressão cardinal, uma expressão fixa e uma "+
				"expressão mutável. As qualidades influenciam a maneira da pessoa interagir com o ambiente e as pessoas.");

		lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_PARAGRAFO).texto("As Qualidades").build());

		List<String> lst = quebrarParagrafos(sbQualidade.toString());
		lst.stream().forEach(s -> {
			lista.add(Paragrafo.builder().estilo(EstiloParagrafo.PARAGRAFO_NORMAL).texto(s).build());
		});

		if (nonNull(enumSigno) && nonNull(enumSigno.getQualidade()) ){
			EnumQualidade qualidade = enumSigno.getQualidade();

			MapaQualidade q = buscador.findQualidade(qualidade.getSigla());
			if (nonNull(q)){
				lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_PARAGRAFO)
						.texto("Sua qualidade: "+enumSigno.getQualidade().getNome()).build());

				// TODO será table
				lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_SIMPLES).texto(String.format("Signos: %s", q.getSignos())).build());
				lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_SIMPLES).texto(String.format("Casas: %s", q.getCasas())).build());
				lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_SIMPLES).texto(String.format("Palavras chave: %s", q.getPalavrasChave())).build());
				lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_SIMPLES).texto(String.format("Personalidade: %s", q.getPersonalidade() )).build());
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

					lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_PARAGRAFO).texto("O Signo Solar").build());
					lista.add(Paragrafo.builder().estilo(EstiloParagrafo.PARAGRAFO_NORMAL).texto(isNull(signoSolarCabecalho) ? NOT_FOUND : signoSolarCabecalho.getTexto()).build());

					EnumSigno enumSigno = EnumSigno.getBySigla(ps.getSigno());
					SignoSolar signoSolar = buscador.findSignoSolar(enumSigno.getSigla());
					String key = String.format("%s %s", enumSigno.getNome(), Simbolos.getSimboloSigno(enumSigno.getSigla()));

					lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_PARAGRAFO).texto(key).build());

					if (isNull(signoSolar)){
						lista.add(Paragrafo.builder().estilo(EstiloParagrafo.PARAGRAFO_NORMAL).texto(NOT_FOUND).build());
					} else {
						//TODO: Já deveria estar apenas com \n ao invés de \r\n\r\n
						List<String> lst = quebrarParagrafos(signoSolar.getTexto());
						lst.stream().forEach(s -> {
							lista.add(Paragrafo.builder().estilo(EstiloParagrafo.PARAGRAFO_NORMAL).texto(s).build());
						});
					}
				});
		return lista;
	}

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

					lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_PARAGRAFO).texto(keyTitulo).build());
					if (isNull(psTitulo)){
						lista.add(Paragrafo.builder().estilo(EstiloParagrafo.PARAGRAFO_NORMAL).texto(NOT_FOUND).build());
					} else {
						//TODO: Já deveria estar apenas com \n ao invés de \r\n\r\n
						List<String> lst = quebrarParagrafos(psTitulo.getTexto());
						lst.stream().forEach(s -> {
							lista.add(Paragrafo.builder().estilo(EstiloParagrafo.PARAGRAFO_NORMAL).texto(s).build());
						});
					}

					PlanetaSigno ps = buscador.findPlanetaSigno(pp.getPlaneta(), pp.getSigno() );
					EnumSigno enumSigno = EnumSigno.getBySigla(pp.getSigno());
					String key = String.format("%s em %s (%s %s)", 	enumPlaneta.getNome(),
																	enumSigno.getNome(),
																	Simbolos.getSimboloPlaneta(enumPlaneta.getSigla()),
																	Simbolos.getSimboloSigno(enumSigno.getSigla()));

					lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_PARAGRAFO).texto(key).build());
					if (isNull(ps)){
						lista.add(Paragrafo.builder().estilo(EstiloParagrafo.PARAGRAFO_NORMAL).texto(NOT_FOUND).build());
					} else {
						//TODO: Já deveria estar apenas com \n ao invés de \r\n\r\n
						List<String> lst = quebrarParagrafos(ps.getTexto());
						lst.stream().forEach(s -> {
							lista.add(Paragrafo.builder().estilo(EstiloParagrafo.PARAGRAFO_NORMAL).texto(s).build());
						});
					}
				});
		return lista;
	}

	private List<Paragrafo> interpretarCuspidesTituloGeral() {
		List<Paragrafo> lista = new ArrayList<>();
		String keyCasas = "As Casas";
		lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_PARAGRAFO).texto(keyCasas).build());

		MapaCuspide mc = buscador.findCuspide("XX", 0);
		if (isNull(mc)){
			lista.add(Paragrafo.builder().estilo(EstiloParagrafo.PARAGRAFO_NORMAL).texto(NOT_FOUND).build());
		} else {
			//TODO: Já deveria estar apenas com \n ao invés de \r\n\r\n
			List<String> lst = quebrarParagrafos(mc.getTexto());
			lst.stream().forEach(s -> {
				lista.add(Paragrafo.builder().estilo(EstiloParagrafo.PARAGRAFO_NORMAL).texto(s).build());
			});
		}
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

			lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_PARAGRAFO).texto(key).build());

			if (isNull(a)){
				lista.add(Paragrafo.builder().estilo(EstiloParagrafo.PARAGRAFO_NORMAL).texto(NOT_FOUND).build());
			} else {
				//TODO: Já deveria estar apenas com \n ao invés de \r\n\r\n
				List<String> lst = quebrarParagrafos(a.getTexto());
				lst.stream().forEach(s -> {
					lista.add(Paragrafo.builder().estilo(EstiloParagrafo.PARAGRAFO_NORMAL).texto(s).build());
				});
			}
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
					lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_PARAGRAFO).texto(keyTitulo).build());
					if (isNull(mcTitulo)){
						lista.add(Paragrafo.builder().estilo(EstiloParagrafo.PARAGRAFO_NORMAL).texto(NOT_FOUND).build());
					} else {
						//TODO: Já deveria estar apenas com \n ao invés de \r\n\r\n
						List<String> lst = quebrarParagrafos(mcTitulo.getTexto());
						lst.stream().forEach(s -> {
							lista.add(Paragrafo.builder().estilo(EstiloParagrafo.PARAGRAFO_NORMAL).texto(s).build());
						});
					}

					EnumSigno enumSigno = EnumSigno.getBySigla(c.getSigno());
					String casa = Casa.getByNumero(c.getCasa());
					String key = String.format("%s na Cúspide da %s Casa (%s %s)", enumSigno.getNome(), casa, Simbolos.getSimboloSigno(enumSigno.getSigla()), c.getCasa() );
					MapaCuspide mc = buscador.findCuspide(enumSigno.getSigla(), c.getCasa());

					lista.add(Paragrafo.builder().estilo(EstiloParagrafo.TITULO_PARAGRAFO).texto(key).build());
					if (isNull(mc)){
						lista.add(Paragrafo.builder().estilo(EstiloParagrafo.PARAGRAFO_NORMAL).texto(NOT_FOUND).build());
					} else {
						//TODO: Já deveria estar apenas com \n ao invés de \r\n\r\n
						List<String> lst = quebrarParagrafos(mc.getTexto());
						lst.stream().forEach(s -> {
							lista.add(Paragrafo.builder().estilo(EstiloParagrafo.PARAGRAFO_NORMAL).texto(s).build());
						});
					}
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
					lista.add(buildParagrafo(EstiloParagrafo.TITULO_PARAGRAFO,keyTitulo));
					if (isNull(pcTitulo)){
						lista.add(Paragrafo.builder().estilo(EstiloParagrafo.PARAGRAFO_NORMAL).texto(NOT_FOUND).build());
					} else {
						//TODO: Já deveria estar apenas com \n ao invés de \r\n\r\n
						List<String> lst = quebrarParagrafos(pcTitulo.getTexto());
						lst.stream().forEach(s -> {
							lista.add(buildParagrafo(EstiloParagrafo.PARAGRAFO_NORMAL,s));
						});
					}

					String key = String.format("%s na %s Casa (%s %d)", enumPlaneta.getNome(), casa, Simbolos.getSimboloPlaneta(enumPlaneta.getSigla()), pp.getCasa());
					PlanetaCasa pc = buscador.findPlanetaCasa(pp.getPlaneta(), pp.getCasa());

					lista.add(buildParagrafo(EstiloParagrafo.TITULO_PARAGRAFO,key));
					if (isNull(pc)){
						lista.add(Paragrafo.builder().estilo(EstiloParagrafo.PARAGRAFO_NORMAL).texto(NOT_FOUND).build());
					} else {
						//TODO: Já deveria estar apenas com \n ao invés de \r\n\r\n
						List<String> lst = quebrarParagrafos(pc.getTexto());
						lst.stream().forEach(s -> {
							lista.add(buildParagrafo(EstiloParagrafo.PARAGRAFO_NORMAL,s));
						});
					}

				});
		return lista;
	}

	private Paragrafo buildParagrafo(EstiloParagrafo estilo, String texto){
		return Paragrafo.builder().estilo(estilo).texto(texto).build();
	}

	private Interpretacao tratarParagrafos(String titulo, String texto) {
		 List<String> textos = new LinkedList<>();
		 String[] aaa = texto.split("\n\\s+");
		 for (int i = 0; i < aaa.length; i++ ) {
			 textos.add(aaa[i]);
		 }
		 return Interpretacao.builder().titulo(titulo).paragrafos(textos).build();
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

	 private List<String> tratarParagrafos(String texto){
		 List<String> retorno = new LinkedList<>();
		 String[] aaa = texto.split("\n\\s+");
		 for (int i = 0; i < aaa.length; i++ ) {
			 retorno.add(aaa[i]);
		 }
		 return retorno;
	 }

	public String buildConteudoArquivoTxt(Map<String, String> map) {
		//String  nome = mapa.getDadosPessoais().getNome().replaceAll(" ", "_").toLowerCase();

		//String url = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		//String dest = String.format("%s/file_%s.txt",url,nome);
		//String dest = String.format("d:/%s.txt",nome);

		///////FileWriter arq = new FileWriter(dest);
		///////PrintWriter gravarArq = new PrintWriter(arq);

		StringBuilder sb = new StringBuilder();

		for(String k : map.keySet()) {
			//Paragraph p = new Paragraph();
			//p.add(new Text(k)).setFontSize(14).setBold();
			// título
			sb.append(k+"\n");
			sb.append("---------------------------------------------------\n");
			///////////////document.add(p);

			// Remover enter
			String texto = map.get(k);
			texto = texto.replace("\n", "");//.replace("\r", "");
			sb.append(texto);
			sb.append("\n\n");

			//p = new Paragraph();
			//p.add(new Text(texto).setFontSize(10) );

			///////////////document.add(p);
		}
		return sb.toString();
	}

	public String buildConteudoArquivoWord(CartaNatalResponse mapa, Map<String, String> map) {
		StringBuilder sb = new StringBuilder();

		for(String k : map.keySet()) {
			// título
			sb.append(k+"\n");
			sb.append("---------------------------------------------------\n");

			// Remover enter
			String texto = map.get(k);
			texto = texto.replace("\n", "");//.replace("\r", "");
			sb.append(texto);
			sb.append("\n\n");
		}
		return sb.toString();
	}


	/*
	private void montarArquivoTxt(CartaNatalResponse mapa, Map<String, String> map)  throws IOException {
		String  nome = mapa.getDadosPessoais().getNome().replaceAll(" ", "_").toLowerCase();

		String url = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		String dest = String.format("%s/file_%s.txt",url,nome);
		//String dest = String.format("d:/%s.txt",nome);

		FileWriter arq = new FileWriter(dest);
		PrintWriter gravarArq = new PrintWriter(arq);

		for(String k : map.keySet()) {
			//Paragraph p = new Paragraph();
			//p.add(new Text(k)).setFontSize(14).setBold();
			// título
			gravarArq.printf(k+"\n");
			gravarArq.printf("---------------------------------------------------\n");
			///////////////document.add(p);

			// Remover enter
			String texto = map.get(k);
			texto = texto.replace("\n", "");//.replace("\r", "");
			gravarArq.printf(texto);
			gravarArq.printf("\n\n");

			//p = new Paragraph();
			//p.add(new Text(texto).setFontSize(10) );

			///////////////document.add(p);
		}
		arq.close();
	}
	*/

}
