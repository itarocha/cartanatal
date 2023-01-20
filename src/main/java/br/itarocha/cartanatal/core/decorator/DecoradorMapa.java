package br.itarocha.cartanatal.core.decorator;

import java.text.DecimalFormat;

import br.itarocha.cartanatal.core.model.domain.Mapa;
import br.itarocha.cartanatal.core.model.domain.CuspideCasa;
import br.itarocha.cartanatal.core.model.domain.ItemAspecto;
import br.itarocha.cartanatal.core.model.domain.PlanetaAspecto;
import br.itarocha.cartanatal.core.model.domain.PlanetaPosicao;
import br.itarocha.cartanatal.core.util.Funcoes;

public class DecoradorMapa {

	private Mapa mapa;
	
	public DecoradorMapa(Mapa mapa){
		this.mapa = mapa;
	}
	
	public String getJSON(){
		String retorno = "";
		retorno += displayCabecalho();
		retorno += ",\n";
		retorno += displayPlanetasNosSignos();
		retorno += ",\n";
		retorno += displayCuspides();
		retorno += ",\n";
		retorno += displayAspectos();
		retorno = "{"+retorno+"}";
		return retorno;
	}
    
    private String displayCabecalho(){
    	double latitude = mapa.getLatitude().Coordenada2Graus();
    	double longitude = mapa.getLongitude().Coordenada2Graus();	
    	////////int signoAscendente = (int)(casas[1] / 30)+1;
    	String retorno = "";
    	
    	String lon = (longitude > 0 ? "E" : "W");
    	String lat = (latitude > 0 ? "N" : "S");
    	
    	retorno += String.format("\"dados_pessoais\": {\"nome\": \"%s\", "+
    								"\"data\":\"%s\", "+
    								"\"hora\":\"%s\", "+
    								"\"deltaT\":\"%s\", "+
    								"\"julDay\":\"%s\", "+
    								"\"lat\":\"%s\", "+
    								"\"lon\":\"%s\"}",
    							mapa.getNome(),
    							mapa.getData(),
    							mapa.getHora(),
    							
    							new DecimalFormat("#.####").format(mapa.getDeltaTSec()),
    							new DecimalFormat("#.######").format(mapa.getJulDay()),
    							
    							Funcoes.grau(latitude)+lat,
    							Funcoes.grau(longitude)+lon
    			);
    	
		return retorno;
		// VEJA MAIS TARDE
		//System.out.println("Ayanamsa: " + CartaUtil.grau(ayanamsa) + " (" + sw.swe_get_ayanamsa_name(SID_METHOD) + ")");
		///////System.out.println("Ascendente: " + CartaUtil.grauNaCasa(casas[1])+" "+signos[signoAscendente-1]);
    }
	
	private String displayPlanetasNosSignos(){
		String retorno = "";
		
		for(PlanetaPosicao pp : mapa.getPosicoesPlanetas()){
			retorno += String.format("{\"planeta\":\"%s\", \"signo\":\"%s\", \"casa\": \"%02d\",  \"grau\": \"%s\", \"gg\":\"%s\", \"mm\":\"%s\", \"ss\":\"%s\"},\n",
					pp.getEnumPlaneta().getSigla(), 	// Planeta
					pp.getEnumSigno().getSigla(),		// Signo
					pp.getCasa(),
					pp.getLocalizacao().getGrau(),
					pp.getLocalizacao().getGnc(),			// gg
					pp.getLocalizacao().getM(),				// mm
					pp.getLocalizacao().getS()				// ss
					);
		}
		retorno =  "\"planetas_signos\":[\n"+
					retorno.substring(0,retorno.length()-2)+
					"\n]";
		return retorno;
	}

	private String displayCuspides(){ 
		String retorno = "";
		for (CuspideCasa c: mapa.getListaCuspides() ){
			if (c.getNumero() > 12) { break; }
			
			String g = c.getLocalizacao().getGrau();
			String gnc = c.getLocalizacao().getGrauNaCasa();
			gnc = gnc.replace('.', '-');
			String[] gms = gnc.split("-");
			
			retorno += String.format("{\"casa\":\"%02d\", \"signo\":\"%s\", \"grau\": \"%s\", \"gg\":\"%s\", \"mm\":\"%s\", \"ss\":\"%s\"},\n",
					c.getNumero(), 	// Casa
					c.getEnumSigno().getSigla(),	// Signo
					g,
					gms[0],			// gg 
					gms[1],			// mm 
					gms[2]			// ss
					);
		}
		retorno =  "\"cuspides\":[\n"+
				  retorno.substring(0,retorno.length()-2)+
				  "\n]";
		return retorno;
	}

	private String displayAspectos(){
		String retorno = "";
		for(ItemAspecto ite : mapa.getListaAspectos()){
			PlanetaAspecto pA = ite.getPlanetaA();
			PlanetaAspecto pB = ite.getPlanetaB();
			
			retorno += String.format("{\"planeta_origem\":\"%s\", \"planeta_destino\":\"%s\", \"aspecto\":\"%s\", \"x\":\"%s\", \"y\":\"%s\"},\n",
					pA.getEnumPlaneta().getSigla(), 
					pB.getEnumPlaneta().getSigla(),
					ite.getAspecto(),
					pA.getCoordenada(),
					pB.getCoordenada()
					);
		}
		retorno = "\"aspectos\":[\n"+
				  retorno.substring(0,retorno.length()-2)+
				  "\n]";
		return retorno;
	}
	
}
