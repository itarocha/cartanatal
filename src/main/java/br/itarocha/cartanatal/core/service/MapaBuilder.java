package br.itarocha.cartanatal.core.service;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import br.itarocha.cartanatal.core.model.DadosPessoais;
import br.itarocha.cartanatal.core.model.domain.*;
import br.itarocha.cartanatal.core.util.Funcoes;
import de.thmac.swisseph.SweConst;
import de.thmac.swisseph.SweDate;
import de.thmac.swisseph.SwissEph;

import static java.util.Objects.isNull;

public class MapaBuilder {
	
	private static final Logger	log = Logger.getAnonymousLogger();
	
	private SweDate sweDate; 

	private int[] aspectos_planetas = new int[18];
	private double[] aspectos_posicoes = new double[18];
	private double[] casas= new double[23];
	private SwissEph sw;
	private double ayanamsa;
	
	private static String imagePath;

	private static final String FORMATO_DATA = "dd/MM/yyyy";
	private static final int SID_METHOD = SweConst.SE_SIDM_LAHIRI;
	
	private static MapaBuilder instance = null;
	
	public static MapaBuilder getInstance(String path) throws Exception{
		if (instance == null) instance = new MapaBuilder();
		imagePath = path;
		return instance;
	}
	
	public String getPath() {
		return imagePath;
	}

	private MapaBuilder() throws Exception {
		String path = getClass().getClassLoader().getResource("").getPath()+"ephe";
		log.info("PATH: "+path);
		try {
			sw = new SwissEph(path);
		} catch (Exception e) {
			throw e;
		}
	}

	public Mapa build(DadosPessoais dadosPessoais) {
		MapeadorCidades mapeador = MapeadorCidades.getInstance();
		
		Cidade c = mapeador.getCidade(dadosPessoais.getCidade(), dadosPessoais.getUf());
		if (c != null) {
			Mapa m = new Mapa(dadosPessoais.getNome(), dadosPessoais.getDataHoraNascimento(), c);
			calcular(m); 
			return m;
		}
		return null;
	}
	
	// TODO: Deve retornar uma classe Mapa
	private void calcular(Mapa mapa){
		this.sweDate = new SweDate(mapa.getAnoUT(),mapa.getMesUT(),mapa.getDiaUT(), mapa.getHoraDouble() );
		this.sweDate.setCalendarType(this.sweDate.SE_GREG_CAL, this.sweDate.SE_KEEP_DATE);
		this.ayanamsa = this.sw.swe_get_ayanamsa_ut(this.sweDate.getJulDay());
		
		double tmp = sweDate.getDeltaT(sweDate.getJulDay());
		
		System.out.println(tmp);
		
		mapa.setDeltaTSec(this.sweDate.getDeltaT() * 86400);
		mapa.setJulDay(this.sweDate.getJulDay()+tmp);
		
		// Rigorosamente nesta ordem
		buildCasas(mapa);
		buildPlanetas(mapa);
		buildAspectos(mapa);
	}
	
	// Fabricando Cuspides
	// Depende do calculo das casas
	private void buildCasas(Mapa mapa){
		int sign;
		mapa.getListaCuspides().clear();
		
		this.casas = this.getHouses(this.sw, 
									mapa, 
									sweDate.getJulDay(), 
									mapa.getLatitude().Coordenada2Graus(),
									mapa.getLongitude().Coordenada2Graus() );
		
		//for (int i = 1; i < 21; i++){
		for (int i = 1; i <= 12; i++){
			mapa.getListaCuspides().add(new CuspideCasa(i, casas[i]));
		}

		int intGrauDef = 0;
    	if (!mapa.getListaCuspides().isEmpty()) {
    		String grauDef = mapa.getListaCuspides().get(0).getGrau();
    		grauDef = grauDef.replace('.', '-');
    		String[] gms = grauDef.split("-");
    		intGrauDef = Integer.parseInt(gms[0]);
    	}
    	mapa.setGrausDefasagemAscendente(intGrauDef);
	}
	

	// Do the coordinate calculation for this planet p
	// x2[0] = longitude (Planeta)
	// x2[1] = latitude
	// x2[2] = distancia
	// x2[3] = velocidade do planeta em longitude // Se negativo, retrogrado
	// x2[4] = velodicade em latitude
	// x2[5] = velocidade em distancia???
	private void buildPlanetas(Mapa mapa){
		long iflag, iflgret;
		EnumPlaneta enumPlaneta;
		PlanetaPosicao pp;
		iflag = SweConst.SEFLG_SPEED;

		double tjd, te;
		tjd=sweDate.getJulDay();
		te = tjd + sweDate.getDeltaT(tjd);
		double x[]=new double[6];
		double x2[]=new double[6];
		StringBuffer serr=new StringBuffer();
		boolean isRetrogrado = false;
		int idxpos = -1;

		mapa.getPosicoesPlanetas().clear();
		
		iflgret = sw.swe_calc(te, SweConst.SE_ECL_NUT, (int)iflag, x, serr);
		
		// O ultimo era SE_CHIRON
		for(int index = 0; index <= 9; index++){
			//Planeta planeta = mapPlanetas.get(xis);
			
			iflgret = sw.swe_calc(te, index, (int)iflag, x2, serr);
			// if there is a problem, a negative value is returned and an errpr message is in serr.
			if (iflgret < 0)
				System.out.print("error: "+serr.toString()+"\n");
			else if (iflgret != iflag)
				System.out.print("warning: iflgret != iflag. "+serr.toString()+"\n");
		  
			//print the coordinates
			//house = (sign + 12 - signoAscendente) % 12 +1;
			isRetrogrado = (x2[3] < 0);
		  
			// Atualizando posicoes para calculo de aspectos
			double _geolat = mapa.getLatitude().Coordenada2Graus(); // ok
			double _armc = mapa.getSideralTime(); // ok
			double _eps_true = x[0];
			
	        double casaDouble = sw.swe_house_pos(_armc, _geolat, _eps_true, 'P', x2, serr);

	        double latitude = x2[1];
	        double distancia = x2[2];
	        double direcao = x2[3];
	        double posicao = x2[0];
			
	        enumPlaneta = EnumPlaneta.getByCodigo(index);
			pp = new PlanetaPosicao(enumPlaneta, posicao);
			
			pp.setRetrogrado(isRetrogrado);
			pp.setLatitude(latitude);
			pp.setDistancia(distancia);
			pp.setDirecao(direcao);
			pp.setCasaDouble(casaDouble);
			mapa.getPosicoesPlanetas().add(pp);

			aspectos_planetas[index] = enumPlaneta.getCodigo(); //planeta.getId();
			aspectos_posicoes[index] = posicao; 			
		}
		
		// Ascendente e Meio do Ceu
		aspectos_planetas[10] = EnumPlaneta.getByCodigo(10).getCodigo();
		aspectos_posicoes[10] = casas[1];
		
		enumPlaneta = EnumPlaneta.getByCodigo(10);
		pp = new PlanetaPosicao(enumPlaneta, casas[1]);
		pp.setCasaDouble(1);
		mapa.getPosicoesPlanetas().add(pp);

		aspectos_planetas[11] = EnumPlaneta.getByCodigo(11).getCodigo();
		aspectos_posicoes[11] = casas[10];

		enumPlaneta = EnumPlaneta.getByCodigo(11);
		pp = new PlanetaPosicao(enumPlaneta, casas[10]);
		pp.setCasaDouble(10);
		mapa.getPosicoesPlanetas().add(pp);
	}
	
	private void buildAspectos(Mapa mapa){
		mapa.getListaAspectos().clear();
		//String aspecto;
		for (int x=0; x < 11; x++){
			for(int y=x+1; y < 12; y++){
				EnumPlaneta eA = EnumPlaneta.getByCodigo(x);
				EnumPlaneta eB = EnumPlaneta.getByCodigo(y);
				
				EnumAspecto aspecto = Funcoes.buildAspect(eA.getSigla(),eB.getSigla(),aspectos_posicoes[x], aspectos_posicoes[y]);
				if (!isNull(aspecto)){
					ItemAspecto item = new ItemAspecto();
					
					item.getPlanetaA().setEnumPlaneta(EnumPlaneta.getByCodigo(x));
					
					item.getPlanetaA().setPosicao(aspectos_posicoes[x]);

					item.getPlanetaB().setEnumPlaneta(EnumPlaneta.getByCodigo(y));
					item.getPlanetaB().setPosicao(aspectos_posicoes[y]);
					
					//EnumAspecto enumAspecto = EnumAspecto.getBySigla(aspecto);
					
					item.setAspecto(aspecto);
					item.getPlanetaA().setCoordenada(x);
					item.getPlanetaB().setCoordenada(y);

					item.getPlanetaA().setGrau(Funcoes.grau(aspectos_posicoes[x]));
					item.getPlanetaB().setGrau(Funcoes.grau(aspectos_posicoes[y]));
					
					mapa.getListaAspectos().add(item);
				}
			}
		} // end aspecto
	}
	
    /// <summary>
    /// Calculate houses
    /// </summary>
    /// <param name="jdnr">Julian day number</param>
    /// <param name="lat">Geographical latitude</param>
    /// <param name="lon">Geographical longitude</param>
    /// <param name="system">Index to define housesystem</param>
    /// <returns>Array of doubles with with the following values:
    ///  0: not used, 1..12 cusps 1..12, 13: asc., 14: MC, 15: ARMC, 16: Vertex,
    ///  17: Equatorial asc., 18: co-ascendant (Koch), 19: co-ascendant(Munkasey),
    ///  20: polar ascendant 
	///
	///  yy[0] = ascendant
	///  yy[1] = mc
	///  yy[2] = armc (= sidereal time) !!!!!
	///  yy[3] = vertex
	///  yy[4] = equatorial ascendant
	///  yy[5] = co-ascendant (Walter Koch)
	///  yy[6] = co-ascendant (Michael Munkasey)
	///  yy[7] = polar ascendant (Michael Munkasey)
	///  yy[8] = reserved for future use
	///  yy[9] = reserved for future use
    ///</returns>
    private double[] getHouses(SwissEph sw, Mapa mapa, double jdnr, double lat, double lon) {
        double[] xx = new double[13];
        double[] yy = new double[10];
        double[] zz = new double[23];
        int flag = sw.swe_houses(jdnr, SweConst.SEFLG_SPEED, lat, lon, 'P', xx, yy);
        
        mapa.setSideralTime(yy[2]);
        
        for (int i = 0; i < 13; i++) {
            zz[i] = xx[i];
        }
        for (int i = 0; i < 10; i++) {
            zz[i + 13] = yy[i];
        }
        return zz;
    }
}

//http://www.timeanddate.com/worldclock/switzerland/zurich
//http://www.sadhana.com.br/cgi-local/mapas/mapanow.cgi?indic=10003&ref=http%3A//www.deldebbio.com.br/
// http://www.astrosage.com/astrology/ayanamsa-calculator.asp
//http://www.astro.com/swisseph/swephprg.htm#_Toc471829052


//SweConst.SE_MEAN_APOG 
//SweConst.SE_OSCU_APOG
//SweConst.SE_MEAN_NODE
//SweConst.SE_CHIRON
//get the name of the planet p
//snam=sw.swe_get_planet_name(p);



//%-12s %10.7f\t %10.7f\t %10.7f\t
//pp.getSiglaPlaneta()
//pp.getLatitude(),			// Latitude
//pp.getDistancia(),   		// Distancia
//pp.getDirecao()   		// Direcao