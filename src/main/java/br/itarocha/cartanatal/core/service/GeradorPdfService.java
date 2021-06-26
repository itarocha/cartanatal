package br.itarocha.cartanatal.core.service;

//import br.itarocha.cartanatal.core.model.*;
//import br.itarocha.cartanatal.core.model.domain.*;
//import br.itarocha.cartanatal.core.model.presenter.CartaNatal;
import br.itarocha.cartanatal.core.model.*;
import br.itarocha.cartanatal.core.model.domain.CuspideCasa;
import br.itarocha.cartanatal.core.model.domain.EnumAspecto;
import br.itarocha.cartanatal.core.model.domain.EnumPlaneta;
import br.itarocha.cartanatal.core.model.domain.EnumSigno;
import br.itarocha.cartanatal.core.model.presenter.CartaNatal;
import br.itarocha.cartanatal.core.model.presenter.Cuspide;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class GeradorPdfService {

	@Autowired
	private BuscadorService servico;

    public static final String FONT_ASTRO = "src/main/resources/fonts/AstroDotBasic.ttf";
	
	 public List<Interpretacao> createArquivo(boolean isTudo, CartaNatal mapa) throws IOException {

		 List<Interpretacao> retorno = new LinkedList<Interpretacao>();
		 
		 //Map<String, Map<String, String>> mapTextos = new HashMap<>();
		 String NOT_FOUND = "<NOT_FOUND>";
		 Map<String, String> map = new LinkedHashMap<>();
		 String key = "";
		 
		 // SIGNO SOLAR
		 key = "";

		 for(br.itarocha.cartanatal.core.model.presenter.PlanetaSigno pp : mapa.getPlanetasSignos()){

			 if (!EnumPlaneta.SOL.equals(EnumPlaneta.getBySigla(pp.getPlaneta()))) continue;

			 SignoSolar ss = servico.findSignoSolar("XX");
			 key = "O Signo Solar";

			 if (ss != null) {
				 if (isTudo || (!isTudo && TipoLogico.N.equals(ss.getConferido())) ) {
					 map.put(key, ss.getTexto());
					 retorno.add(this.tratarParagrafos(key, ss.getTexto()));
				 }
			 } else {
				 map.put(key, NOT_FOUND);
				 retorno.add(this.tratarParagrafos(key, NOT_FOUND));
			 }

			 //String signo = TipoSigno.getByString(pp.getEnumSigno().getSigla());
			 String signo = pp.getSigno(); //TipoSigno.getByString(pp.getEnumSigno().getSigla());
			 ss = servico.findSignoSolar(signo);
			 key = String.format("%s", signo);
			 if (ss != null) {
				 if (isTudo || (!isTudo && TipoLogico.N.equals(ss.getConferido())) ) {
					 map.put(key, ss.getTexto());
					 retorno.add(this.tratarParagrafos(key, ss.getTexto()));
				 }
			 } else {
				 map.put(key, NOT_FOUND);
				 retorno.add(this.tratarParagrafos(key, NOT_FOUND));
			 }
			 break;
		 }

		 // PLANETAS NOS SIGNOS
		 key = "";
		 for(br.itarocha.cartanatal.core.model.presenter.PlanetaSigno pp : mapa.getPlanetasSignos() ){

			 EnumPlaneta enumPlaneta = EnumPlaneta.getBySigla(pp.getPlaneta());

			 //if (!EnumPlaneta.NOR.equals(EnumPlaneta.getBySigla(pp.getPlaneta()))) continue;
			 if (EnumPlaneta.SOL.equals(enumPlaneta) ) continue;
			 if (EnumPlaneta.ASC.equals(enumPlaneta) ) continue;
			 if (EnumPlaneta.MCE.equals(enumPlaneta) ) continue;


			 // Apresentação
			 //PlanetaSigno ps = servico.findPlanetaSigno(pp.getEnumPlaneta().getSigla(), pp.getEnumPlaneta().getSigla());
			 PlanetaSigno ps = servico.findPlanetaSigno(pp.getPlaneta(), "XX");
			 String planeta = enumPlaneta.getNome(); // TipoPlaneta.getByString( pp.getEnumPlaneta().getSigla());
			 key = String.format("%s nos Signos", planeta);
			 if (ps != null) {
				 if (isTudo || (!isTudo && TipoLogico.N.equals(ps.getConferido())) ) {
					 map.put(key, ps.getTexto());
					 retorno.add(this.tratarParagrafos(key, ps.getTexto()));
				 }
			 } else {
				 map.put(key, NOT_FOUND);
				 retorno.add(this.tratarParagrafos(key, NOT_FOUND));
			 }
			 
			 ps = servico.findPlanetaSigno(pp.getPlaneta(), pp.getSigno() );
			 //key = String.format("%s.%s", pp.getSiglaPlaneta(), pp.getNomeSigno());
			 planeta = enumPlaneta.getNome(); // TipoPlaneta.getByString(pp.getEnumPlaneta().getSigla());

			 EnumSigno enumSigno = EnumSigno.getBySigla(pp.getSigno());


			 String signo = enumSigno.getNome(); //TipoSigno.getByString(pp.getEnumSigno().getSigla());
			 key = String.format("%s em %s", planeta, signo);
			 if (ps != null) {
				 if (isTudo || (!isTudo && TipoLogico.N.equals(ps.getConferido())) ) {
					 map.put(key, ps.getTexto());
					 retorno.add(this.tratarParagrafos(key, ps.getTexto()));
				 }
			 } else {
				 if ( !EnumPlaneta.SOL.equals(enumPlaneta) ) {
					 map.put(key, NOT_FOUND);
					 retorno.add(this.tratarParagrafos(key, NOT_FOUND));
				 }
			 }
		 }


		 // CÚSPIDES

		 // CÚSPIDES - TÍTULO GERAL
		 key = "As Casas";
		 MapaCuspide mc = servico.findCuspide("XX", 0);
		 if (mc != null) {
			 map.put(key, mc.getTexto());
			 retorno.add(this.tratarParagrafos(key, mc.getTexto()));
		 } else {
			 map.put(key, NOT_FOUND);
			 retorno.add(this.tratarParagrafos(key, NOT_FOUND));
		 }
		 
		 for( Cuspide c : mapa.getCuspides()){
			 if( c.getCasa() > 12 ) continue;

			 // CÚSPIDES - TÍTULOS
			 //String casa = Casa.getByNumero(c.getNumero());
			 key = String.format("Casa %s", c.getCasa());
			 mc = servico.findCuspide("XX", c.getCasa());
			 if (mc != null) {
				 if (isTudo || (!isTudo) ) { //&& TipoLogico.N.equals(mc.getCon) !mc.getFoiConferido()
					 map.put(key, mc.getTexto());
					 retorno.add(this.tratarParagrafos(key, mc.getTexto()));
				 }
			 } else {
				 map.put(key, NOT_FOUND);
				 retorno.add(this.tratarParagrafos(key, NOT_FOUND));
			 }

			 EnumSigno enumSigno = EnumSigno.getBySigla(c.getSigno());
			 
			 String _key = String.format("%s.%02d", enumSigno.getSigla(), c.getCasa());
			 String signo = TipoSigno.getByString(enumSigno.getSigla());
			 String casa = Casa.getByNumero(c.getCasa());
			 key = String.format("%s na Cúspide da %s Casa", signo, casa);
			 mc = servico.findCuspide(enumSigno.getSigla(), c.getCasa());
			 if (mc != null) {
				 if (isTudo || (!isTudo ) ) { //&& !mc.getFoiConferido()
					 map.put(key, mc.getTexto());
					 retorno.add(this.tratarParagrafos(key, mc.getTexto()));
				 }
			 } else {
				 map.put(key, NOT_FOUND);
				 retorno.add(this.tratarParagrafos(key, NOT_FOUND));
			 }
		}

		// PLANETAS NAS CASAS
		
		for(br.itarocha.cartanatal.core.model.presenter.PlanetaSigno pp : mapa.getPlanetasSignos()){
			//PlanetaPosicao pp = mapa.getPosicoesPlanetas().get(i);

			EnumPlaneta enumPlaneta = EnumPlaneta.getBySigla(pp.getPlaneta());

			if (EnumPlaneta.SOL.equals(enumPlaneta) ) continue;
			if (EnumPlaneta.ASC.equals(enumPlaneta) ) continue;
			if (EnumPlaneta.MCE.equals(enumPlaneta) ) continue;


			 //if("nor".equalsIgnoreCase(pp.getEnumPlaneta().getSigla())) continue;
			 //if("asc".equalsIgnoreCase(pp.getEnumPlaneta().getSigla() )) continue;
			 //if("mce".equalsIgnoreCase(pp.getEnumPlaneta().getSigla() )) continue;
		 
			 String planeta = enumPlaneta.getNome(); //TipoPlaneta.getByString(pp.getEnumPlaneta().getSigla());
			 String casa = Casa.getByNumero((int)pp.getCasa());

			 key = String.format("%s nas Casas", planeta);
			 PlanetaCasa pc = servico.findPlanetaCasa(pp.getPlaneta(), 0);
			 if (pc != null) {
				 if (isTudo || (!isTudo && TipoLogico.N.equals(pc.getConferido())) ) {
					 map.put(key, pc.getTexto());
					 retorno.add(this.tratarParagrafos(key, pc.getTexto()));
				 }
			 } else {
				 map.put(key, NOT_FOUND);
				 retorno.add(this.tratarParagrafos(key, NOT_FOUND));
			 }
			 
			 key = String.format("%s na %s Casa", planeta, casa);
			 pc = servico.findPlanetaCasa(pp.getPlaneta(), pp.getCasa());
			 if (pc != null) {
				 if (isTudo || (!isTudo && TipoLogico.N.equals(pc.getConferido()) ) ) {
					 map.put(key, pc.getTexto());
					 retorno.add(this.tratarParagrafos(key, pc.getTexto()));
				 }
			 } else {
				 map.put(key, NOT_FOUND);
				 retorno.add(this.tratarParagrafos(key, NOT_FOUND));
			 }
		}

		// ASPECTOS
		for(br.itarocha.cartanatal.core.model.presenter.Aspecto ia : mapa.getAspectos() ){
			EnumPlaneta enumPlanetaOrigem = EnumPlaneta.getBySigla(ia.getPlanetaOrigem());
			EnumPlaneta enumPlanetaDestino = EnumPlaneta.getBySigla(ia.getPlanetaDestino());


			//TipoPlaneta pA = ia.getPlanetaOrigem();
			//TipoPlaneta pB = ia.getPlanetaDestino();
			
			String planeta1 = enumPlanetaOrigem.getNome(); // TipoPlaneta.getByString(pA.getEnumPlaneta().getSigla());
			String planeta2 = enumPlanetaDestino.getNome(); // TipoPlaneta.getByString(pB.getEnumPlaneta().getSigla());
			//EnumAspecto aspecto = ia.getAspecto();

			EnumAspecto aspecto = EnumAspecto.CJ.getBySigla(ia.getAspecto());
			
			key = String.format("%s em %s com %s", planeta1, aspecto.getNome(), planeta2 );
			MapaPlanetaAspecto a = servico.findAspecto(ia.getPlanetaOrigem(), ia.getPlanetaDestino(), aspecto.getSigla() );
			 if (a != null) {
				 if (isTudo || (!isTudo && TipoLogico.N.equals(a.getConferido()) ) ) {
					 map.put(key, a.getTexto());
					 retorno.add(this.tratarParagrafos(key, a.getTexto()));
				 }
			 } else {
				 map.put(key, NOT_FOUND);
				 retorno.add(this.tratarParagrafos(key, NOT_FOUND));
			 }
		}

		for(String k : map.keySet()) {
			System.out.println(k + (NOT_FOUND.equals(map.get(k)) ? " - NÃO ENCONTRADO" : ""));
		}
		
		//////////////montarArquivoPdf(mapa, map);
		montarArquivoTxt(mapa, map);
		
		return retorno;
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
	 
	 private void montarArquivoTxt(CartaNatal mapa, Map<String, String> map)  throws IOException {
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

	 /*
	 private void montarArquivoPdf(CartaNatal mapa, Map<String, String> map) throws IOException {
		 String  nome = mapa.getDadosPessoais().getNome().replaceAll(" ", "_").toLowerCase();

		 String url = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		 String dest = String.format("%s/file_%s.pdf",url,nome);		    
		 
		 PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
		 Document document = new Document(pdf);
		 document.setTextAlignment(TextAlignment.JUSTIFIED);
		 
		 PdfFont font = PdfFontFactory.createFont(FONT_ASTRO, true);
		 PdfFont bold = PdfFontFactory.createFont(FONT_ASTRO, true);
		 
		 PdfFont fontCourier = PdfFontFactory.createFont(FontConstants.COURIER);
		 
		 List<PlanetaPosicao> planetas = mapa.getPosicoesPlanetas();
		 
		 Paragraph pa = new Paragraph();
		 for(EnumPlaneta x : EnumPlaneta.values()) {
			 //System.out.println("buscando "+x.getId());
			 PlanetaPosicao pp = planetas.stream().filter(obj -> obj.getEnumPlaneta().getSigla().equals(x.getSigla())).findAny().orElse(null);
			 if (pp != null) {
				 
				 /////////EnumSigno eSigno = EnumSigno.getById(pp.getEnumSigno().getSigla());
				 //EnumPlaneta ePlaneta = EnumPlaneta.getBySigla(pp.getEnumPlaneta().getSigla());
				 
				 Text tLetraPlaneta = new Text(pp.getEnumPlaneta().getLetra()).setFontSize(12).setFont(font);
				 Text tLetraSigno = new Text(pp.getEnumSigno().getLetra()).setFontSize(12).setFont(font);
				 
				 String tmp = String.format(" %s°%s'%s\" casa %s ", pp.getGnc(), pp.getM(), pp.getS(), (int)pp.getCasa());
				 pa.add(tLetraPlaneta)
				 .add(new Text(" "+pp.getEnumPlaneta().getNome()+" ").setFontSize(8))
				 .add(tLetraSigno)
				 .add(new Text(tmp).setFontSize(8))
				 .add("\n");
			 }
		 }
		 document.add(pa);

		for(String k : map.keySet()) {
			//System.out.println(k + (NOT_FOUND.equals(map.get(k)) ? " - NÃO ENCONTRADO" : ""));
			Paragraph p = new Paragraph();
			p.add(new Text(k)).setFontSize(14).setBold();
			document.add(p);
			
			// Remover enter
			String texto = map.get(k);
			texto = texto.replace("\n", "");//.replace("\r", "");
			p = new Paragraph();
			p.add(new Text(texto).setFontSize(10) );
			
			document.add(p);
		}
		 
		 document.close();		 
	 }
	*/
}
