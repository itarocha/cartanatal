package br.itarocha.cartanatal.core.service;

import java.io.*;
import java.net.URL;
import java.util.*;

import br.itarocha.cartanatal.core.model.domain.Cidade;
import br.itarocha.cartanatal.core.util.Funcoes;
import com.fasterxml.jackson.databind.ObjectMapper;

import static br.itarocha.cartanatal.core.service.ArquivosConstantes.ARQUIVO_CIDADES_BRASIL;

public class MapeadorCidades {

	private static MapeadorCidades instance = null;
	private static final Map<String, Cidade> mapCidades = new HashMap<>();
	private static final Map<String,Integer> mapFuso;
	static
	{
		mapFuso = new HashMap<String,Integer>();
		mapFuso.put("AC",-5);
		mapFuso.put("AL",-3);
		mapFuso.put("AM",-4);
		mapFuso.put("AP",-3);
		mapFuso.put("BA",-3);
		mapFuso.put("CE",-3);
		mapFuso.put("DF",-3);
		mapFuso.put("ES",-3);
		mapFuso.put("GO",-3);
		mapFuso.put("MA",-3);
		mapFuso.put("MG",-3);
		mapFuso.put("MS",-4);
		mapFuso.put("MT",-4);
		mapFuso.put("PA",-3);
		mapFuso.put("PB",-3);
		mapFuso.put("PE",-3);
		mapFuso.put("PI",-3);
		mapFuso.put("PR",-3);
		mapFuso.put("RJ",-3);
		mapFuso.put("RN",-3);
		mapFuso.put("RO",-4);
		mapFuso.put("RR",-4);
		mapFuso.put("RS",-3);
		mapFuso.put("SC",-3);
		mapFuso.put("SE",-3);
		mapFuso.put("SP",-3);
		mapFuso.put("TO",-3);
	}
	
	public static MapeadorCidades getInstance() {
		if (instance == null) 
			instance = new MapeadorCidades();
		return instance;
	}

	private MapeadorCidades() {
		this.carregarArquivoCidades();
	}

	public Map<String, Cidade> _getCidades(){
		return this.mapCidades;
	}

	public Cidade _getCidade(String cidade, String uf) {
		String key = buildKey(cidade, uf);
		Cidade c = mapCidades.get(key);
		return c;
	}

	private String buildKey(String cidade, String uf) {
		String s = Funcoes.removerAcentos(cidade).toUpperCase();
		s = s.replaceAll(" ","");
		return String.format("%s.%s", uf, s);
	}

	private void carregarArquivoCidades() {
		URL arquivoCSV = this.getClass().getClassLoader().getResource(ARQUIVO_CIDADES_BRASIL);
		BufferedReader br = null;
		String linha = "";
		String COMMA_DELIMITER = ",";

		/*
		List<List<String>> records = new ArrayList<>();
		try (BufferedReader _br = new BufferedReader(new FileReader("/home/itamar/astrologia/cidades_brasil.csv"))) {
			String line;
			while ((line = _br.readLine()) != null) {
				String[] values = line.split(COMMA_DELIMITER);
				records.add(Arrays.asList(values));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/

		List<Cidade> listaCidades = new ArrayList<>();

		try {
			br = new BufferedReader(new InputStreamReader(arquivoCSV.openStream()));
			//br = new BufferedReader(new FileReader(arquivoCSV.toString()));
			int i = -1;
			while (((linha = br.readLine()) != null)) {
				i++;
				if (i > 0) {
					String[] cidade = linha.split(COMMA_DELIMITER);

					Cidade c = new Cidade();
					c.setCodigo(Integer.parseInt(cidade[0]));
					c.setNomeSemAcento(cidade[5].replaceAll("\"", ""));
					c.setUf(cidade[6].replaceAll("\"", ""));
					c.setNomeOriginal(cidade[7].replaceAll("\"", ""));
					c.setLatitude(cidade[1].replaceAll("\"", ""));
					c.setLongitude(cidade[3].replaceAll("\"", ""));
					c.setFuso(mapFuso.get(c.getUf()));

					String key = buildKey(c.getNomeSemAcento(), c.getUf());
					c.setKey(key);

					//System.out.println(key);
					this.mapCidades.put(key,c);
					listaCidades.add(c);
				}
			}
			gravarListaCidades(listaCidades);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void gravarListaCidades(List<Cidade> lista){
		String ARQUIVO_CIDADES = "/home/itamar/astrologia/cidadesBrasil.json";
		ObjectMapper om = new ObjectMapper();
		try {
			om.writeValue(new File(ARQUIVO_CIDADES), lista);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void carregarArquivoCidades_OLD() {
		URL arquivoCSV = this.getClass().getClassLoader().getResource(ARQUIVO_CIDADES_BRASIL);
		BufferedReader br = null;
		String linha = "";
		String csvDivisor = ",";
		try {
			br = new BufferedReader(new InputStreamReader(arquivoCSV.openStream()));
			//br = new BufferedReader(new FileReader(arquivoCSV.toString()));
			int i = -1;
			while (((linha = br.readLine()) != null)) {
				i++;
				if (i > 0) {
					String[] cidade = linha.split(csvDivisor);

					Cidade c = new Cidade();
					c.setCodigo(Integer.parseInt(cidade[0]));
					c.setNomeSemAcento(cidade[5].replaceAll("\"", ""));
					c.setUf(cidade[6].replaceAll("\"", ""));
					c.setNomeOriginal(cidade[7].replaceAll("\"", ""));
					c.setLatitude(cidade[1].replaceAll("\"", ""));
					c.setLongitude(cidade[3].replaceAll("\"", ""));
					
					c.setFuso(mapFuso.get(c.getUf()));

					String key = buildKey(c.getNomeSemAcento(), c.getUf());
					System.out.println(key);
					this.mapCidades.put(key,c);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
/*
 * Ler o arquivo //ephe//cidades_brasil.csv
 * "codigo","lat","e/w","lon","n/s","nome sem acento"
 * ,"uf","nome original","altitude","area"
 * 1,"-16.45.26","W","-49.26.15","S","abadia de goias"
 * ,"GO","Abadia de Goi�s",898.000,136.900
 * 2,"-18.29.08","W","-47.24.11","S","abadia dos dourados"
 * ,"MG","Abadia dos Dourados",742.000,897.400
 * 3,"-16.12.15","W","-48.42.25","S","abadiania","GO",
 * "Abadi�nia",1052.000,1047.700
 */
