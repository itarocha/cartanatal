package br.itarocha.cartanatal.core.decorator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import br.itarocha.cartanatal.core.Mapa;
import br.itarocha.cartanatal.core.model.domain.*;

public class ChartPainter {

	private static final int SIZE = 600;
	private static final int MARGEM = 10;
	private static final int MARGEM_CASA = 60;  
	private static final int MARGEM_INTERNA = 300;  
	private static final int MARGEM_ASPECTOS = 360;  
	
	
	private static final int HORIZONTAL_SIZE = 640;
	private static final int VERTICAL_SIZE = 640;
	private static final int DISTANCE_SHAMA = HORIZONTAL_SIZE / 2 - 35;
	 
	private static final int DISTANCE_ALFA = HORIZONTAL_SIZE / 2 - 50;
	private static final int DISTANCE_BETA = HORIZONTAL_SIZE / 4 - 40;
	private static final int BIG_DOT = 8;
	private static final int PQ_DOT = 5;
	
	private Mapa mapa;
	private String pathToSave;
		
	public ChartPainter(Mapa mapa, String path) {
		this.mapa = mapa;
		this.pathToSave = path;
		drawMapa();
		drawAspectos();
	}
	  
	private void drawMapa() {
		try {
			BufferedImage bi = new BufferedImage(HORIZONTAL_SIZE, VERTICAL_SIZE, BufferedImage.TYPE_INT_ARGB);
	
			Graphics2D g = bi.createGraphics();
	    
			g.setRenderingHint(	RenderingHints.KEY_ANTIALIASING,
	            				RenderingHints.VALUE_ANTIALIAS_ON);
	
		    drawMapaFundo(g);
		    drawMapaPosicoes(g);
		    // Escolha o formato: JPEG, GIF ou PNG
		    
		    String nome = mapa.getNome().replaceAll(" ", "_").toUpperCase();
		    String url = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		    url = this.pathToSave;
		    //String dest = String.format("%s/map_%s.png",url,nome);		    
		    String dest = String.format("%s/mapa.png",url);		    
		    ImageIO.write(bi, "png",  new File(dest));

		  } catch (IOException ie) {
		    ie.printStackTrace();
		  }
	}
  
	private void drawAspectos() {
		try {
			
			int largura = 500 + 300;
			int altura = 300;
			
			BufferedImage bi = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
	
			Graphics2D g = bi.createGraphics();
	    
			g.setRenderingHint(	RenderingHints.KEY_ANTIALIASING,
	            				RenderingHints.VALUE_ANTIALIAS_ON);
	
		    drawAspectosFundo(g, largura, altura);
		    //desenharPosicoesPlanetas(g);
		    // Escolha o formato: JPEG, GIF ou PNG
		    String nome = mapa.getNome().replaceAll(" ", "_").toUpperCase();
		    
		    String url = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		    url = this.pathToSave;
		    //String dest = String.format("%s/asp_%s.png",url,nome);		    
		    String dest = String.format("%s/aspectos.png",url);		    
		    ImageIO.write(bi, "png",  new File(dest));

		    //ImageIO.write(bi, "PNG",  file);
		  } catch (IOException ie) {
		    ie.printStackTrace();
		  }
	}

	private void drawAspectosFundo(Graphics2D g, int largura, int altura) {
	      g.setStroke(new BasicStroke(1));
	      g.setColor(Color.white );
	      //g.fillRect(0, 0, 500, 380);
	      g.fillRect(0, 0, largura, altura);
	      
	      //int margemX = 40;
	      int margemX = 40 + 320;
	      int margemY = 10;
	      g.setColor(Color.black );
	      
	      int w = 35; // pode ser a largura da letra "W"
	      int h = 20; // pode ser a altura da letra "W"
	      
	      //Font fontAstroX = this.getFontAstrologia();
	      
	      Font fontAstro = this.getFontAstrologia().deriveFont(20f);
	      
	      Font fontTimes = new Font("TimesRoman", Font.BOLD, 14);
		  // Planetas
	      for(int y = 0; y < 12; y++){
	    	  EnumPlaneta e = EnumPlaneta.getByCodigo(y);
	    	  
	    	  PlanetaPosicao pp = mapa.getPosicoesPlanetas().get(y);
	    	  
	    	  // Símbolo Planeta
		      g.setFont(fontAstro);
	    	  g.drawString(	pp.getEnumPlaneta().getLetra(),
	       					w,
	       					26 + (h * (y+1)) );

	    	  
	    	  // Nome Planeta
	    	  g.setFont(fontTimes);
	    	  g.drawString(	pp.getEnumPlaneta().getNome(),
		       				22 + w,
		       				26 + (h * (y+1)) );

	    	  // Grau Signo
	    	  g.setFont(fontTimes);
	    	  g.drawString(	pp.getGnc() + "°" ,
	       				130 + w,
	       				26 + (h * (y+1)) );
	    	  
	    	  // Simbolo Signo
		      g.setFont(fontAstro);
	    	  g.drawString(	pp.getEnumSigno().getLetra() ,
		       				160 + w,
		       				26 + (h * (y+1)) );
	    	  
	    	  g.setFont(fontTimes);
	    	  g.drawString(	pp.getM()+"'" ,
	       				176 + w,
	       				26 + (h * (y+1)) );
	    	  
	    	  g.drawString(	pp.getEnumSigno().getNome() ,
	       				210 + w,
	       				26 + (h * (y+1)) );
	    	  
	    	  
	    	 // Cabecalhos (planetas)
	    	 g.setFont(fontAstro);
	    	 g.drawString(	e.getLetra(),
 			       			margemX + 10 + (y * w),
 			       			26 + (h * (y+1)) );
	    	 
	         for(int x = 0; x < 12; x++){
	        	  if (y > x) {
	        		  g.drawRect(	margemX + (x * w),
	        				  		30 + (y * h),
	        				  		w,
	        				  		h);
	        	  }
	         }
	      }	      

	      // Aspectos
	      for (ItemAspecto ite : mapa.getListaAspectos()) {
	    	  int x = ite.getPlanetaA().getCoordenada();
	    	  int y = ite.getPlanetaB().getCoordenada();
	    	  EnumAspecto aspecto = ite.getAspecto();
	    	  
	    	  g.drawString(aspecto.getLetra(), 
	    			       margemX + (x * w) +10,
	    			       30 + ((y+1) * h)  -4);
	      }
	  }
	
	private void drawMapaFundo(Graphics2D g) {
      g.setStroke(new BasicStroke(1));
      g.setColor(Color.white );
      g.fillOval(MARGEM, MARGEM, SIZE, SIZE);
      
      g.setColor(Color.black);
      // Circulo Grande
      g.drawOval(MARGEM, MARGEM, SIZE, SIZE);
      
      g.setColor(Color.black);
      // Maior
      int RAIO_MAIOR = MARGEM + (MARGEM_CASA / 2); 
      int RAIO_MAIOR_B = SIZE - MARGEM_CASA;
      g.drawOval(RAIO_MAIOR, RAIO_MAIOR, RAIO_MAIOR_B, RAIO_MAIOR_B);

      // Media
      int RAIO_MEDIO = MARGEM + (MARGEM_INTERNA / 2);
      int RAIO_MEDIO_B = SIZE - MARGEM_INTERNA;
      g.drawOval(RAIO_MEDIO, RAIO_MEDIO, RAIO_MEDIO_B, RAIO_MEDIO_B);

      g.drawOval(	MARGEM + (MARGEM_ASPECTOS / 2), 
    		  		MARGEM + (MARGEM_ASPECTOS / 2), 
    		  		SIZE - MARGEM_ASPECTOS, 
    		  		SIZE - MARGEM_ASPECTOS);

      // CASAS
      Font font = new Font("TimesRoman", Font.BOLD, 14);
      g.setFont(font);
      for (int i = 0; i <= 11; i++) {

    	  Point ptAlfa = minToLocation(i*30, DISTANCE_ALFA);
          Point ptBeta = minToLocation(i*30, DISTANCE_BETA);
          
          Point ptLetra = minToLocation(i*30+15, DISTANCE_BETA+18);

          String xis = new Integer(i+1).toString();
          g.drawString(xis,
          		ptLetra.x - (BIG_DOT / 2) - MARGEM,
          		ptLetra.y - (BIG_DOT / 2));
          
          int xIni = ptAlfa.x - MARGEM;
          int yIni = ptAlfa.y - MARGEM;
          int xFim = ptBeta.x - MARGEM;
          int yFim = ptBeta.y - MARGEM;
          g.drawLine(xIni, yIni, xFim, yFim);
      }    
  }
  
  private void drawMapaPosicoes(Graphics2D g) {
	  g.setColor(Color.red);
	  boolean alternador = false;
	  Integer acrescimo = 50;
	  Font font = this.getFontAstrologia();
	  // Planetas
      g.setFont(font.deriveFont(28f));

      // TODO: Lembrar de ordenar conforme os graus pp.getGrau()
      
      List<ItemDesenhoMapa> lista = new ArrayList<ItemDesenhoMapa>();
      for(PlanetaPosicao pp : mapa.getPosicoesPlanetas()){
		  if("nor".equalsIgnoreCase(pp.getEnumPlaneta().getSigla())) continue;
		  if("asc".equalsIgnoreCase(pp.getEnumPlaneta().getSigla())) continue;
		  if("mce".equalsIgnoreCase(pp.getEnumPlaneta().getSigla())) continue;

		  ItemDesenhoMapa item = new ItemDesenhoMapa(pp.getEnumPlaneta().getLetra(), Integer.parseInt(pp.getG()), Integer.parseInt(pp.getGnc()), Integer.parseInt(pp.getM()));		  
		  lista.add(item);
		  
	  }
      Collections.sort(lista);
      for ( ItemDesenhoMapa item : lista ) {
    	  Integer grau = item.getGrau360()-mapa.getGrausDefasagemAscendente();
    	  
    	  alternador = !alternador;
    	  acrescimo = alternador ? 50 : 90;
    	  // ATENCAO! REDUZIR A DEFASAGEM DO SIGNO ASCENDENTE!!!
    	  Point ptLetra = minToLocation(grau, DISTANCE_BETA+acrescimo);
    	  g.drawString(item.getTexto(),
    			  ptLetra.x - (BIG_DOT / 2) - MARGEM,
    			  ptLetra.y - (BIG_DOT / 2));
    	  
    	  Point ptBeta = minToLocation(grau, DISTANCE_BETA);
    	  g.fillOval(	ptBeta.x - (PQ_DOT / 2) - MARGEM,
    			  ptBeta.y - (PQ_DOT / 2) - MARGEM,
    			  PQ_DOT,
    			  PQ_DOT);     
      }
	  
      g.setColor(Color.black);
      for (int i = 0; i <= 11; i++) {

    	  CuspideCasa c = mapa.getListaCuspides().get(i);
    	  
    	  Point ptLetra = minToLocation(i*30, DISTANCE_ALFA+15);
          Point ptAntes = minToLocation(i*30-5, DISTANCE_ALFA+15);
          Point ptDepois = minToLocation(i*30+5, DISTANCE_ALFA+15);
          g.setFont(font.deriveFont(20f));
          g.drawString(c.getEnumSigno().getLetra() ,
          		ptLetra.x - (BIG_DOT / 2) - MARGEM,
          		ptLetra.y - (BIG_DOT / 2));
          
          g.setFont(new Font("TimesRoman", Font.PLAIN , 13));
          
          String grau = c.getGnc()+"°";
          String minuto = c.getM().toString()+"'";
          
          String txtAntes = grau;
          String txtDepois = minuto;
          if (Arrays.asList(8,9,10,11,12).contains(i+1)) {
        	  txtAntes = minuto;
        	  txtDepois = grau;
          }
          
          g.drawString(	txtAntes,
            			ptAntes.x - (BIG_DOT / 2) - MARGEM,
            			ptAntes.y - (BIG_DOT / 2));

          g.drawString(	txtDepois,
      					ptDepois.x - (BIG_DOT / 2) - MARGEM,
      					ptDepois.y - (BIG_DOT / 2));
      }    
  }

  public static Font getFontAstrologia() {
	    Font font = null;
	  	//String fName = "/fonts/AstroDotBasic.ttf";
	  String fName = "/fonts/AstroDotBasic.ttf";
	  URL arquivoFonte = ChartPainter.class.getClassLoader().getResource("fonts/AstroDotBasic.ttf");
	    try {
	      InputStream is = ChartPainter.class.getResourceAsStream(fName);
	      File file = new File(arquivoFonte.getFile());
			font = Font.createFont(Font.TRUETYPE_FONT, file);
			//font = Font.createFont(Font.TRUETYPE_FONT, is);
	    } catch (Exception ex) {
	      ex.printStackTrace();
	      System.err.println(arquivoFonte.getFile() + " nao carregada. Usando fonte serif.");
	      font = new Font("TimesRoman", Font.PLAIN, 12);
	    }
	    return font;
  }  

  private Point minToLocation(int angulo, int radius) {
	double t = 2 * Math.PI * (angulo - 90) / 360;
    int x = (int)(HORIZONTAL_SIZE / 2 + radius * Math.sin(t));
    int y = (int)(VERTICAL_SIZE / 2 + radius * Math.cos(t));
    return new Point(x, y);
  }    
  
}