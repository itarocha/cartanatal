package br.itarocha.cartanatal.core.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import static java.util.Objects.isNull;

import de.thmac.swisseph.SweConst;
import de.thmac.swisseph.SweDate;
import de.thmac.swisseph.SwissEph;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import br.itarocha.cartanatal.core.model.DadosPessoais;
import br.itarocha.cartanatal.core.model.domain.*;
import br.itarocha.cartanatal.core.util.Funcoes;


@RequiredArgsConstructor
@Service
public class MapaService {

	private final SWService swService;

	private SweDate sweDate;

	private double[] casas = new double[23];

	private static final int SID_METHOD = SweConst.SE_SIDM_LAHIRI;

	public Mapa build(DadosPessoais dadosPessoais, Cidade cidade) {
		System.out.println("SID_METHOD "+SID_METHOD);
		if (cidade != null) {
			Mapa m = new Mapa(dadosPessoais.getNome(), dadosPessoais.getDataHoraNascimento(), cidade);
			return calcular(m);
		}
		return null;
	}
	
	private Mapa calcular(Mapa mapa){
		sweDate = new SweDate(mapa.getAnoUT(),mapa.getMesUT(),mapa.getDiaUT(), mapa.getHoraDouble() );
		sweDate.setCalendarType(SweDate.SE_GREG_CAL, SweDate.SE_KEEP_DATE);
		double ayanamsa = this.swService.getSw().swe_get_ayanamsa_ut(sweDate.getJulDay());
		
		double deltaT = SweDate.getDeltaT(sweDate.getJulDay());
		mapa.setDeltaTSec(this.sweDate.getDeltaT() * 86400);
		mapa.setJulDay(this.sweDate.getJulDay()+deltaT);
		
		// Rigorosamente nesta ordem
		buildCuspides(mapa);
		double[] aspectos_pos = buildPosicoesPlanetas(mapa);
		buildAspectos(mapa, aspectos_pos);
		return mapa;
	}
	
	// Fabricando Cuspides
	// Depende do calculo das casas
	private void buildCuspides(Mapa mapa){
		//int sign;
		mapa.getListaCuspides().clear();
		this.casas = this.getHouses(this.swService.getSw(),
									mapa, 
									sweDate.getJulDay(), 
									mapa.getLatitude().Coordenada2Graus(),
									mapa.getLongitude().Coordenada2Graus() );

		AtomicInteger numeroCasa = new AtomicInteger(1);
		Arrays.stream(casas)
				.skip(1)
				.limit(12)
				.forEach(c -> {
					int signo = (int)(c / 30);
					mapa.getListaCuspides().add(
							CuspideCasa.builder()
									.numero(numeroCasa.getAndIncrement())
									.localizacao(buildLocalizacao(c))
									.enumSigno(EnumSigno.getByCodigo(signo))
									.build());
				});
		int intGrauDef = 0;
    	if (!mapa.getListaCuspides().isEmpty()) {
    		String grauDef = mapa.getListaCuspides().get(0).getLocalizacao().getGrau();
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
	private double[] buildPosicoesPlanetas(Mapa mapa){
		final int[] aspectos_planetas = new int[18];
		final double[] aspectos_posicoes = new double[18];

		long iflag, iflgret;
		EnumPlaneta enumPlaneta;
		//PlanetaPosicao pp;
		iflag = SweConst.SEFLG_SPEED;

		double tjd, te;
		tjd = sweDate.getJulDay();
		double deltaT = sweDate.getDeltaT(tjd);
		te = tjd + deltaT;

		double[] x = new double[6];
		double[] x2 = new double[6];
		StringBuffer serr= new StringBuffer();
		boolean isRetrogrado; // = false;
		//int idxpos = -1;

		mapa.getPosicoesPlanetas().clear();
		
		iflgret = this.swService.getSw().swe_calc(te, SweConst.SE_ECL_NUT, (int)iflag, x, serr);
		
		// O ultimo era SE_CHIRON
		for(int index = 0; index <= 9; index++){
			//Planeta planeta = mapPlanetas.get(xis);
			
			iflgret = this.swService.getSw().swe_calc(te, index, (int)iflag, x2, serr);
			// DEBUG
			System.out.println(Arrays.toString(x2));

			// if there is a problem, a negative value is returned and an errpr message is in serr.
			if (iflgret < 0)
				System.out.print("error: "+serr+"\n");
			else if (iflgret != iflag)
				System.out.print("warning: iflgret != iflag. "+serr+"\n");
		  
			//print the coordinates
			//house = (sign + 12 - signoAscendente) % 12 +1;
			isRetrogrado = (x2[3] < 0);
		  
			// Atualizando posicoes para calculo de aspectos
			double _geolat = mapa.getLatitude().Coordenada2Graus(); // ok
			double _armc = mapa.getSideralTime(); // ok
			double _eps_true = x[0];
			
	        double casaDouble = this.swService.getSw().swe_house_pos(_armc, _geolat, _eps_true, 'P', x2, serr);

	        double latitude = x2[1];
	        double distancia = x2[2];
	        double direcao = x2[3];
	        double posicao = x2[0];
			
	        enumPlaneta = EnumPlaneta.getByCodigo(index);
			mapa.getPosicoesPlanetas().add(buildPlanetaPosicao(
					enumPlaneta,
					posicao,
					casaDouble,
					latitude,
					distancia,
					direcao,
					isRetrogrado));

			aspectos_planetas[index] = enumPlaneta.getCodigo(); //planeta.getId();
			aspectos_posicoes[index] = posicao; 			
		}
		
		// Ascendente e Meio do Ceu
		aspectos_planetas[10] = EnumPlaneta.getByCodigo(10).getCodigo();
		aspectos_posicoes[10] = casas[1];
		
		enumPlaneta = EnumPlaneta.getByCodigo(10);
		mapa.getPosicoesPlanetas().add(buildPlanetaPosicao(
				enumPlaneta,
				casas[1],
				1,
				0,
				0,
				0,
				false));

		aspectos_planetas[11] = EnumPlaneta.getByCodigo(11).getCodigo();
		aspectos_posicoes[11] = casas[10];

		enumPlaneta = EnumPlaneta.getByCodigo(11);
		mapa.getPosicoesPlanetas().add(buildPlanetaPosicao(
				enumPlaneta,
				casas[10],
				10,
				0,
				0,
				0,
				false));

		return aspectos_posicoes;
	}

	private PlanetaPosicao buildPlanetaPosicao(EnumPlaneta enumPlaneta,
											   double posicao,
											   double casaDouble,
											   double latitude,
											   double distancia,
											   double direcao,
											   boolean isRetrogrado
											   ) {
		int signo = (int)(posicao / 30);

		return PlanetaPosicao.builder()
				.enumPlaneta(enumPlaneta)
				.enumSigno(EnumSigno.getByCodigo(signo))
				.retrogrado(isRetrogrado)
				.statusRetrogrado(isRetrogrado ? "R" : "D")
				.latitude(latitude)
				.distancia(distancia)
				.direcao(direcao)
				.casaDouble(casaDouble)
				.casa((int) casaDouble)
				.localizacao(buildLocalizacao(posicao))
				.build();
	}

	private Localizacao buildLocalizacao(double position) {
		String grau = Funcoes.grau(position);
		String tmpGrau = grau.replace('.', '-');
		String[] grauNaCasaGms = tmpGrau.split("-");
		String grauNaCasa = Funcoes.grauNaCasa(position);
		String tmp = grauNaCasa.replace('.', '-');
		String[] gms = tmp.split("-");

		return Localizacao.builder()
				.posicao(position)
				.angulo(BigDecimal.valueOf(position).setScale(8, RoundingMode.DOWN))
				.grau(grau)
				.grauNaCasa(grauNaCasa)
				.gnc(gms[0])
				.g(grauNaCasaGms[0])
				.m(gms[1])
				.s(gms[2])
				.build();
	}

	private void buildAspectos(Mapa mapa, double[] aspectos_posicoes){
		mapa.getListaAspectos().clear();

		for (int x=0; x < 11; x++){
			for(int y=x+1; y < 12; y++){
				EnumPlaneta eA = EnumPlaneta.getByCodigo(x);
				EnumPlaneta eB = EnumPlaneta.getByCodigo(y);
				ItemAspecto itemAspecto = Funcoes.buildAspect(	eA,
																eB,
																aspectos_posicoes[x],
																aspectos_posicoes[y],
																x,
																y);
				if (Objects.nonNull(itemAspecto)){
					mapa.getListaAspectos().add(itemAspecto);
				}

				/*
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
				*/
			}
		}
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

		sw.swe_houses(jdnr, SweConst.SEFLG_SPEED, lat, lon, 'P', xx, yy);
        
        mapa.setSideralTime(yy[2]);

		System.arraycopy(xx, 0, zz, 0, 13);
		System.arraycopy(yy, 0, zz, 13, 10);
        return zz;
    }
}

//http://www.timeanddate.com/worldclock/switzerland/zurich
//http://www.sadhana.com.br/cgi-local/mapas/mapanow.cgi?indic=10003&ref=http%3A//www.deldebbio.com.br/
//http://www.astrosage.com/astrology/ayanamsa-calculator.asp
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