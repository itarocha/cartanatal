package br.itarocha.cartanatal.core.service;

import br.itarocha.cartanatal.core.model.*;
import br.itarocha.cartanatal.core.model.domain.EnumAspecto;
import br.itarocha.cartanatal.core.model.domain.EnumPlaneta;
import br.itarocha.cartanatal.core.model.domain.EnumSigno;
import br.itarocha.cartanatal.core.model.presenter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Objects.isNull;

@Service
public class InterpretadorService {

	private static final String NOT_FOUND = "<NOT_FOUND>";

	@Autowired
	private BuscadorService buscadorServide;

    public Map<String, String> gerarInterpretacoes(CartaNatalResponse cartaNatal) {

		Map<String, String> map = new LinkedHashMap<>();

		// CABECALHO
		map.putAll(buildDadosPessoais(cartaNatal.getDadosPessoais()));

		// SIGNO SOLAR
		map.putAll(interpretarSignoSolar(cartaNatal.getPlanetasSignos()));

		// PLANETAS NOS SIGNOS
		map.putAll(interpretarPlanetasSignos(cartaNatal.getPlanetasSignos()));

		// CÚSPIDES - TÍTULO GERAL
		map.putAll(interpretarCuspidesTituloGeral());

		// CÚSPIDES
		map.putAll(interpretarCuspides(cartaNatal.getCuspides()));

		// PLANETAS NAS CASAS
		map.putAll(interpretarPlanetasCasas(cartaNatal.getPlanetasSignos()));

		// ASPECTOS
		map.putAll(interpretarAspectos(cartaNatal.getAspectos()));

		// para cada chave de mapa, tratar paragrafos
		//retorno.add(this.tratarParagrafos(keyCabecalho, signoSolarCabecalho.getTexto()));
		List<Interpretacao> retorno = new LinkedList<>();

		map.entrySet().stream().forEach(entry -> {
			retorno.add(this.tratarParagrafos(entry.getKey(), entry.getValue() ));
		});

		return map;
	 }

	private Map<String, String> buildDadosPessoais(DadoPessoalResponse dadosPessoais) {
		StringBuilder sb = new StringBuilder();
		sb.append(dadosPessoais.getNome() + "\r\n\r\n");
		sb.append(dadosPessoais.getData() + " "  + dadosPessoais.getHora() + "\r\n\r\n");
		sb.append(dadosPessoais.getCidade() + "\r\n\r\n");
		Map<String, String> map = new LinkedHashMap<>();
		map.put("Carta Natal", sb.toString());
		return map;
	}

	private Map<String, String> interpretarSignoSolar(List<PlanetaSignoResponse> planetasSignos) {
		Map<String, String> map = new LinkedHashMap<>();

		planetasSignos.stream()
				.filter(ps -> EnumPlaneta.SOL.equals( EnumPlaneta.getBySigla(ps.getPlaneta()) ))
				.forEach( ps -> {
					SignoSolar signoSolarCabecalho = buscadorServide.findSignoSolar("XX");
					String keyCabecalho = "O Signo Solar";
					map.put(keyCabecalho, isNull(signoSolarCabecalho) ? NOT_FOUND : signoSolarCabecalho.getTexto() );

					EnumSigno enumSigno = EnumSigno.getBySigla(ps.getSigno());
					SignoSolar signoSolar = buscadorServide.findSignoSolar(enumSigno.getSigla());
					String key = String.format("%s", enumSigno.getNome());
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
					PlanetaSigno psTitulo = buscadorServide.findPlanetaSigno(pp.getPlaneta(), "XX");
					String keyTitulo = String.format("%s nos Signos", enumPlaneta.getNome());
					map.put(keyTitulo, isNull(psTitulo) ? NOT_FOUND : psTitulo.getTexto());

					PlanetaSigno ps = buscadorServide.findPlanetaSigno(pp.getPlaneta(), pp.getSigno() );
					EnumSigno enumSigno = EnumSigno.getBySigla(pp.getSigno());
					String key = String.format("%s em %s", enumPlaneta.getNome(), enumSigno.getNome());
					map.put(key, isNull(ps) ? NOT_FOUND : ps.getTexto());
				});
		return map;
	}

	private Map<String, String> interpretarAspectos(List<AspectoResponse> aspectos) {
		Map<String, String> map = new LinkedHashMap<>();

    	aspectos.stream().forEach(ia -> {
			EnumPlaneta enumPlanetaOrigem = EnumPlaneta.getBySigla(ia.getPlanetaOrigem());
			EnumPlaneta enumPlanetaDestino = EnumPlaneta.getBySigla(ia.getPlanetaDestino());
			EnumAspecto aspecto = EnumAspecto.getBySigla(ia.getAspecto());

			String key = String.format("%s em %s com %s", enumPlanetaOrigem.getNome(), aspecto.getNome(), enumPlanetaDestino.getNome() );
			MapaPlanetaAspecto a = buscadorServide.findAspecto(ia.getPlanetaOrigem(), ia.getPlanetaDestino(), aspecto.getSigla() );
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

					String keyTitulo = String.format("%s nas Casas", enumPlaneta.getNome());
					PlanetaCasa pcTitulo = buscadorServide.findPlanetaCasa(pp.getPlaneta(), 0);
					map.put(keyTitulo, isNull(pcTitulo) ? NOT_FOUND : pcTitulo.getTexto());

					String key = String.format("%s na %s Casa", enumPlaneta.getNome(), casa);
					PlanetaCasa pc = buscadorServide.findPlanetaCasa(pp.getPlaneta(), pp.getCasa());
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
					MapaCuspide mcTitulo = buscadorServide.findCuspide("XX", c.getCasa());
					map.put(keyTitulo, isNull(mcTitulo) ? NOT_FOUND : mcTitulo.getTexto());

					EnumSigno enumSigno = EnumSigno.getBySigla(c.getSigno());
					String casa = Casa.getByNumero(c.getCasa());
					String key = String.format("%s na Cúspide da %s Casa", enumSigno.getNome(), casa);
					MapaCuspide mc = buscadorServide.findCuspide(enumSigno.getSigla(), c.getCasa());

					map.put(key, isNull(mc) ? NOT_FOUND : mc.getTexto());
				});
		return map;
	}

	private Map<String, String> interpretarCuspidesTituloGeral() {
		Map<String, String> map = new LinkedHashMap<>();
		String keyCasas = "As Casas";
		MapaCuspide mc = buscadorServide.findCuspide("XX", 0);
		map.put(keyCasas, isNull(mc) ? NOT_FOUND : mc.getTexto());
		return map;
	}

	private Interpretacao tratarParagrafos(String titulo, String texto) {
		 List<String> textos = new LinkedList<String>();
		 String[] aaa = texto.split("\n\\s+");
		 for (int i = 0; i < aaa.length; i++ ) {
			 textos.add(aaa[i]);
		 }
		 return  new Interpretacao(titulo, textos);
	 }
	 
	 private List<String> tratarParagrafos(String texto){
		 List<String> retorno = new LinkedList<>();
		 String[] aaa = texto.split("\n\\s+");
		 for (int i = 0; i < aaa.length; i++ ) {
			 retorno.add(aaa[i]);
		 }
		 return retorno;
	 }

	public String buildConteudoArquivoTxt(CartaNatalResponse mapa, Map<String, String> map) {
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
