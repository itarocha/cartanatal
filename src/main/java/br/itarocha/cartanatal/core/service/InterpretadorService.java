package br.itarocha.cartanatal.core.service;

import br.itarocha.cartanatal.adapter.in.web.controller.dto.*;
import br.itarocha.cartanatal.core.model.*;
import br.itarocha.cartanatal.core.model.domain.*;
import br.itarocha.cartanatal.core.model.interpretacao.*;
import br.itarocha.cartanatal.core.util.Simbolos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class InterpretadorService {

	private static final String NOT_FOUND = "<NOT_FOUND>";
	private static final String LF = "\r\n\r\n";

	@Autowired
	private BuscadorService buscador;

    public Map<String, String> gerarInterpretacoes(CartaNatalResponse cartaNatal) {

		Map<String, String> map = new LinkedHashMap<>();

		map.putAll(buildCabecalho(cartaNatal.getDadosPessoais()));
		map.putAll(this.buildIntroducao());
		map.putAll(interpretarElemento(cartaNatal.getPlanetasSignos()));
		map.putAll(interpretarQualidade(cartaNatal.getPlanetasSignos()));
		map.putAll(interpretarSignoSolar(cartaNatal.getPlanetasSignos()));
		map.putAll(interpretarPlanetasSignos(cartaNatal.getPlanetasSignos()));
		map.putAll(interpretarCuspidesTituloGeral());
		map.putAll(interpretarCuspides(cartaNatal.getCuspides()));
		map.putAll(interpretarPlanetasCasas(cartaNatal.getPlanetasSignos()));
		map.putAll(interpretarAspectos(cartaNatal.getAspectos()));

		// para cada chave de mapa, tratar paragrafos
		//retorno.add(this.tratarParagrafos(keyCabecalho, signoSolarCabecalho.getTexto()));
		/*
		List<Interpretacao> retorno = new LinkedList<>();

		map.entrySet().stream().forEach(entry -> {
			retorno.add(this.tratarParagrafos(entry.getKey(), entry.getValue() ));
		});
		 */
		return map;
	 }


	private Map<String, String> buildCabecalho(DadoPessoalResponse dadosPessoais) {
		StringBuilder sb = new StringBuilder();
		sb.append(dadosPessoais.getNome() + "\r\n\r\n");
		sb.append(dadosPessoais.getData() + " "  + dadosPessoais.getHora() + "\r\n\r\n");
		sb.append(dadosPessoais.getCidade() + "\r\n\r\n");
		sb.append(String.format("Latitude: %s \r\n\r\n", dadosPessoais.getLat()));
		sb.append(String.format("Longitude: %s \r\n\r\n", dadosPessoais.getLon()));
		sb.append(String.format("Julian Day: %s \r\n\r\n", dadosPessoais.getJulDay()));
		sb.append(String.format("Delta T: %s seg.\r\n\r\n", dadosPessoais.getDeltaT()));

		Map<String, String> map = new LinkedHashMap<>();
		map.put("Carta Natal", sb.toString());
		return map;
	}

	private Map<String, String> buildIntroducao() {
		Map<String, String> map = new LinkedHashMap<>();
		StringBuilder sbAstrologia = new StringBuilder();
		sbAstrologia.append("A astrologia (do grego astron, \"astros\", \"estrelas\" ou \"corpo celeste\"; e logos, \"palavra\", \"estudo\") é uma ferramenta matemática, segundo a qual as posições relativas das estrelas poderiam prover informação sobre a personalidade, as relações humanas, e outros assuntos relacionados à vida do ser humano."+LF);
		sbAstrologia.append("Os astrólogos estudam, há milhares de anos, os efeitos da atividade planetária e as suas correspondências com o comportamento humano. Durante séculos, a astrologia se baseou na observação de objetos celestes e no registro de seus movimentos. Mais recentemente, os astrólogos têm usado dados coletados pelos astrônomos e organizados em tabelas chamadas efemérides, que mostram as posições dos corpos celestes."+LF);
		map.put("Astrologia", sbAstrologia.toString());

		StringBuilder sbMapaNatal = new StringBuilder();
		sbMapaNatal.append("O Mapa Natal mostra a posição correta dos astros em relação à Terra no momento de nascimento "+
				"de uma determinada pessoa. Ele captura o momento no tempo e o congela. Da sua perspectiva na Terra, os "+
				"planetas parecem se mover à sua volta, de modo que você está no centro do seu mapa astral. "+
				"As configurações de um Mapa Natal se repetem apenas a cada 26.000 anos, portanto ele é quase como uma "+
				"impressão digital - não existe um igual ao outro."+LF);
		map.put("O Mapa Natal", sbMapaNatal.toString());

		StringBuilder sbZodiaco = new StringBuilder();
		sbZodiaco.append("A palavra zodíaco (do latim zodiacus - significa \"círculo de animais\") é uma faixa imaginária "+
				"do céu que inclui as órbitas aparentes do Sol, da Lua e dos planetas Mercúrio, Vênus, Marte, Júpiter, "+
				"Saturno, Urano e Netuno.");
		sbZodiaco.append("As divisões do zodíaco representam constelações na astronomia e signos na astrologia, que são "+
				"(Áries ou Carneiro, Touro, Gêmeos, Câncer ou Caranguejo, Leão, Virgem, Libra ou Balança, Escorpião, "+
				"Sagitário, Capricórnio, Aquário e Peixes).");
		return map;
	}

	private Map<String, String> interpretarElemento(List<PlanetaSignoResponse> planetasSignos) {
		EnumSigno enumSigno = planetasSignos.stream()
					.filter(ps -> EnumPlaneta.SOL.equals( EnumPlaneta.getBySigla(ps.getPlaneta()) ))
					.map(ps -> EnumSigno.getBySigla(ps.getSigno()))
					.findFirst()
				.orElse(null);


		Map<String, String> map = new LinkedHashMap<>();
		StringBuilder sbElemento = new StringBuilder();

		sbElemento.append("A energia dos planetas é filtrada pelos quatro elementos da natureza (terra, água, ar e fogo). "+
				"Cada um está relacionado com uma determinada função. A terra é relacionada ao corpo. "+
				"A água às emoções. O ar à mente, ou  intelecto. O fogo ao espírito. Os elementos podem representar "+
				"diferentes maneiras de se perceber a vida. A classificação por elementos é algo fundamental na "+
				"Astrologia e ela é decidida por meio dos dez planetas que compõe o sistema astrológico e mais o Ascendente."+LF);
		map.put("Os Elementos", sbElemento.toString());

		if (nonNull(enumSigno) && nonNull(enumSigno.getElemento()) ){
			EnumElemento elemento = enumSigno.getElemento();

			MapaElemento me = buscador.findElemento(elemento.getSigla());
			if (nonNull(me)){
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("Signos: %s"+LF, me.getSignos()));
				sb.append(String.format("Planetas: %s"+LF, me.getPlanetas()));
				sb.append(String.format("Tipo: %s"+LF, me.getTipo() ));
				sb.append(String.format("Polaridade: %s"+LF, me.getPolaridade() ));
				sb.append(String.format("Palavras chave: %s"+LF, me.getPalavrasChave()));
				sb.append(String.format("Fisiologia: %s"+LF, me.getFisiologia() ));
				sb.append(String.format("Aparência: %s"+LF, me.getAparencia() ));
				sb.append(String.format("Temperamento: %s"+LF, me.getTemperamento() ));
				sb.append(String.format("Associações: %s"+LF, me.getAssociacoes() ));
				map.put("Seu Elemento: "+elemento.getNome().toUpperCase(Locale.ROOT), sb.toString());

				// replace LF ???
				map.put(me.getEstiloVida(), me.getTextoEstiloVida()+LF);
				map.put(String.format("%s em equilíbrio",elemento.getNome()), me.getTextoEquilibrio() +LF);
				map.put(String.format("%s em desequilíbrio",elemento.getNome()), me.getTextoDesequilibrio() +LF);
			}
		}

		return map;
	}

	private Map<String, String> interpretarQualidade(List<PlanetaSignoResponse> planetasSignos) {
		EnumSigno enumSigno = planetasSignos.stream()
				.filter(ps -> EnumPlaneta.SOL.equals( EnumPlaneta.getBySigla(ps.getPlaneta()) ))
				.map(ps -> EnumSigno.getBySigla(ps.getSigno()))
				.findFirst()
				.orElse(null);


		Map<String, String> map = new LinkedHashMap<>();
		StringBuilder sbQualidade = new StringBuilder();

		sbQualidade.append("As qualidades regulam sutilmente a energia astrológica. "+
				"As quatro estações e cada um desses períodos de três meses de duração é composto de um começo, meio e fim. "+
				"Todo mês eles se manifestam na natureza de uma maneira variada."+LF);
		sbQualidade.append("Cada um dos quatro elementos apresenta uma expressão cardinal, uma expressão fixa e uma "+
				"expressão mutável. As qualidades influenciam a maneira da pessoa interagir com o ambiente e as pessoas."+LF);

		map.put("As Qualidades", sbQualidade.toString());

		if (nonNull(enumSigno) && nonNull(enumSigno.getQualidade()) ){
			EnumQualidade qualidade = enumSigno.getQualidade();

			MapaQualidade q = buscador.findQualidade(qualidade.getSigla());
			if (nonNull(q)){
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("Signos: %s"+LF, q.getSignos()));
				sb.append(String.format("Casas: %s"+LF, q.getCasas()));
				sb.append(String.format("Palavras chave: %s"+LF, q.getPalavrasChave()));
				sb.append(String.format("Personalidade: %s"+LF, q.getPersonalidade() ));
				map.put("Sua qualidade: "+enumSigno.getQualidade().getNome(),sb.toString()+LF);
			}
		}
		return map;
	}

	private Map<String, String> interpretarSignoSolar(List<PlanetaSignoResponse> planetasSignos) {
		Map<String, String> map = new LinkedHashMap<>();

		planetasSignos.stream()
				.filter(ps -> EnumPlaneta.SOL.equals( EnumPlaneta.getBySigla(ps.getPlaneta()) ))
				.forEach( ps -> {
					SignoSolar signoSolarCabecalho = buscador.findSignoSolar("XX");
					String keyCabecalho = "O Signo Solar";
					map.put(keyCabecalho, isNull(signoSolarCabecalho) ? NOT_FOUND : signoSolarCabecalho.getTexto() );

					EnumSigno enumSigno = EnumSigno.getBySigla(ps.getSigno());
					SignoSolar signoSolar = buscador.findSignoSolar(enumSigno.getSigla());
					String key = String.format("%s %s", enumSigno.getNome(), Simbolos.getSimboloSigno(enumSigno.getSigla()));
					map.put(key, isNull(signoSolar) ? NOT_FOUND : signoSolar.getTexto());
				});
		return map;
	}

	private Map<String, String> interpretarPlanetasSignos(List<PlanetaSignoResponse> planetasSignos) {
		Map<String, String> map = new LinkedHashMap<>();

		EnumPlaneta[] array = {EnumPlaneta.SOL, EnumPlaneta.ASC, EnumPlaneta.MCE};
		List<EnumPlaneta> desconsiderados = Arrays.asList(array);

		planetasSignos.stream()
				.filter(ps -> !desconsiderados.contains(EnumPlaneta.getBySigla(ps.getPlaneta())) )
				.forEach(pp -> {
					EnumPlaneta enumPlaneta = EnumPlaneta.getBySigla(pp.getPlaneta());
					PlanetaSigno psTitulo = buscador.findPlanetaSigno(pp.getPlaneta(), "XX");
					String keyTitulo = String.format("%s nos Signos", enumPlaneta.getNome());
					map.put(keyTitulo, isNull(psTitulo) ? NOT_FOUND : psTitulo.getTexto());

					PlanetaSigno ps = buscador.findPlanetaSigno(pp.getPlaneta(), pp.getSigno() );
					EnumSigno enumSigno = EnumSigno.getBySigla(pp.getSigno());
					String key = String.format("%s em %s (%s %s)", 	enumPlaneta.getNome(),
																	enumSigno.getNome(),
																	Simbolos.getSimboloPlaneta(enumPlaneta.getSigla()),
																	Simbolos.getSimboloSigno(enumSigno.getSigla()));
					map.put(key, isNull(ps) ? NOT_FOUND : ps.getTexto());
				});
		return map;
	}

	private Map<String, String> interpretarAspectos(List<AspectoResponse> aspectos) {
		Map<String, String> map = new LinkedHashMap<>();

    	aspectos.stream().forEach(ia -> {
			EnumPlaneta enumPlanetaOrigem = EnumPlaneta.getBySigla(ia.getPlanetaOrigem());
			EnumPlaneta enumPlanetaDestino = EnumPlaneta.getBySigla(ia.getPlanetaDestino());
			EnumAspecto enumAspecto = EnumAspecto.getBySigla(ia.getAspecto());

			String key = String.format("%s em %s com %s (%s %s %s)", 	enumPlanetaOrigem.getNome(),
																	enumAspecto.getNome(),
																	enumPlanetaDestino.getNome(),

																	Simbolos.getSimboloPlaneta(enumPlanetaOrigem.getSigla()),
																	Simbolos.getSimboloAspecto(enumAspecto.getSigla()),
																	Simbolos.getSimboloPlaneta(enumPlanetaDestino.getSigla())

																	);
			MapaPlanetaAspecto a = buscador.findAspecto(ia.getPlanetaOrigem(), ia.getPlanetaDestino(), enumAspecto.getSigla() );
			map.put(key, isNull(a) ? NOT_FOUND : a.getTexto());
		});
    	return map;
	}

	private Map<String, String> interpretarPlanetasCasas(List<PlanetaSignoResponse> planetasSignos) {
		Map<String, String> map = new LinkedHashMap<>();

		EnumPlaneta[] array = {EnumPlaneta.SOL, EnumPlaneta.ASC, EnumPlaneta.MCE};
		List<EnumPlaneta> desconsiderados = Arrays.asList(array);

		planetasSignos.stream()
				.filter(ps -> !desconsiderados.contains(EnumPlaneta.getBySigla(ps.getPlaneta())) )
				.forEach(pp -> {
					EnumPlaneta enumPlaneta = EnumPlaneta.getBySigla(pp.getPlaneta());
					String casa = Casa.getByNumero(pp.getCasa());

					String keyTitulo = String.format("%s nas Casas %s", enumPlaneta.getNome(), Simbolos.getSimboloPlaneta(enumPlaneta.getSigla()) );
					PlanetaCasa pcTitulo = buscador.findPlanetaCasa(pp.getPlaneta(), 0);
					map.put(keyTitulo, isNull(pcTitulo) ? NOT_FOUND : pcTitulo.getTexto());

					String key = String.format("%s na %s Casa %s", enumPlaneta.getNome(), casa, Simbolos.getSimboloPlaneta(enumPlaneta.getSigla()));
					PlanetaCasa pc = buscador.findPlanetaCasa(pp.getPlaneta(), pp.getCasa());
					map.put(key, isNull(pc) ? NOT_FOUND : pc.getTexto());
				});
		return map;
	}

	private Map<String, String> interpretarCuspides(List<CuspideResponse> cuspides) {
		Map<String, String> map = new LinkedHashMap<>();

		cuspides.stream()
				.filter(c -> c.getCasa() <= 12)
				.forEach(c -> {
					String keyTitulo = String.format("Casa %s", c.getCasa());
					MapaCuspide mcTitulo = buscador.findCuspide("XX", c.getCasa());
					map.put(keyTitulo, isNull(mcTitulo) ? NOT_FOUND : mcTitulo.getTexto());

					EnumSigno enumSigno = EnumSigno.getBySigla(c.getSigno());
					String casa = Casa.getByNumero(c.getCasa());
					String key = String.format("%s na Cúspide da %s Casa (%s %s)", enumSigno.getNome(), casa, Simbolos.getSimboloSigno(enumSigno.getSigla()), c.getCasa() );
					MapaCuspide mc = buscador.findCuspide(enumSigno.getSigla(), c.getCasa());

					map.put(key, isNull(mc) ? NOT_FOUND : mc.getTexto());
				});
		return map;
	}

	private Map<String, String> interpretarCuspidesTituloGeral() {
		Map<String, String> map = new LinkedHashMap<>();
		String keyCasas = "As Casas";
		MapaCuspide mc = buscador.findCuspide("XX", 0);
		map.put(keyCasas, isNull(mc) ? NOT_FOUND : mc.getTexto());
		return map;
	}

	private Interpretacao tratarParagrafos(String titulo, String texto) {
		 List<String> textos = new LinkedList<>();
		 String[] aaa = texto.split("\n\\s+");
		 for (int i = 0; i < aaa.length; i++ ) {
			 textos.add(aaa[i]);
		 }
		 return Interpretacao.builder().titulo(titulo).paragrafos(textos).build();
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
