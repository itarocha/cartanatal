package br.itarocha.cartanatal.core.decorator;

import br.itarocha.cartanatal.core.model.domain.*;
import br.itarocha.cartanatal.core.model.presenter.AspectoResponse;
import br.itarocha.cartanatal.core.model.presenter.CartaNatalResponse;
import br.itarocha.cartanatal.core.model.presenter.CuspideResponse;
import br.itarocha.cartanatal.core.model.presenter.PlanetaSignoResponse;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

// É para ser @Service
public class ChartDraw {

	private static final int SIZE = 600;
	private static final int PADDING = 10;
	private static final int MARGEM_CASA = 60;
	private static final int MARGEM_INTERNA = 300;
	private static final int MARGEM_ASPECTOS = 360;

	private static final int WIDTH = 640;
	private static final int HEIGHT = 640;
	private static final int CX = WIDTH / 2;
	private static final int CY = HEIGHT / 2;

	private static final int RAIO_SEGUNDO_CIRCULO = (WIDTH / 2) - 50;
	private static final int RAIO_CIRCULO_INTERNO = (WIDTH / 4) - 40;
	private static final int BIG_DOT = 8;
	private static final int PQ_DOT = 5;

	private CartaNatalResponse mapa;
	private String pathToSave;

	public ChartDraw(CartaNatalResponse mapa, String path) {
		this.mapa = mapa;
		this.pathToSave = path;
		drawMapa();
		drawAspectos();
	}
	  
	private void drawMapa() {
		try {
			BufferedImage bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
	
			Graphics2D g = bi.createGraphics();
			g.setRenderingHint(	RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		    drawMapaFundo(g);
		    drawMapaPosicoes(g);
		    // Escolha o formato: JPEG, GIF ou PNG
		    
		    String nome = mapa.getDadosPessoais().getNome().replaceAll(" ", "_").toUpperCase();
		    String url = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		    url = this.pathToSave;
		    //String dest = String.format("%s/map_%s.png",url,nome);		    
		    String dest = String.format("%s/mapa.png",url);		    
		    ImageIO.write(bi, "png",  new File(dest));

		  } catch (IOException ie) {
		    ie.printStackTrace();
		  }
	}

	private void drawMapaFundo(Graphics2D g) {
		g.setStroke(new BasicStroke(1));
		g.setColor(Color.WHITE);
		g.fillOval(PADDING, PADDING, SIZE, SIZE);

		// Circulo Grande
		g.setColor(Color.BLACK);
		g.drawOval(PADDING, PADDING, SIZE, SIZE);

		// Segundo círculo
		int RAIO_MAIOR = PADDING + 30;
		int RAIO_MAIOR_B = SIZE - (30 * 2);
		g.setColor(Color.RED);
		g.drawOval(RAIO_MAIOR, RAIO_MAIOR, RAIO_MAIOR_B, RAIO_MAIOR_B);

		// Media
		int MARGEM_INTERNA = SIZE / 2;
		int RAIO_MEDIO = PADDING + (MARGEM_INTERNA / 2);
		int RAIO_MEDIO_B = SIZE - MARGEM_INTERNA;
		g.setColor(Color.MAGENTA);
		g.drawOval(RAIO_MEDIO, RAIO_MEDIO, RAIO_MEDIO_B, RAIO_MEDIO_B);

		g.setColor(Color.BLUE);
		g.drawOval(	PADDING + (MARGEM_ASPECTOS / 2),
				PADDING + (MARGEM_ASPECTOS / 2),
				SIZE - MARGEM_ASPECTOS,
				SIZE - MARGEM_ASPECTOS);

		// CASAS
		Font font = new Font("TimesRoman", Font.BOLD, 14);
		g.setFont(font);
		for (int i = 0; i <= 11; i++) {
			Point ptLetra = angleToPoint(CX, CY, i*30+15, RAIO_CIRCULO_INTERNO +18);

			String numeroCasa = Integer.toString(i+1);
			g.drawString(numeroCasa,
					ptLetra.x - (BIG_DOT / 2) - PADDING,
					ptLetra.y - (BIG_DOT / 2));

			Point ptAlfa = angleToPoint(CX, CY, i * 30, RAIO_SEGUNDO_CIRCULO);
			Point ptBeta = angleToPoint(CX, CY,i * 30, RAIO_CIRCULO_INTERNO);

			int xIni = ptAlfa.x - PADDING;
			int yIni = ptAlfa.y - PADDING;
			int xFim = ptBeta.x - PADDING;
			int yFim = ptBeta.y - PADDING;

			g.drawLine(xIni, yIni, xFim, yFim);
		}
	}

	private void drawAspectos() {
		try {
			int largura = 500 + 300;
			int altura = 300;
			
			BufferedImage bi = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = bi.createGraphics();
	    
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	
		    drawAspectosFundo(g, largura, altura);
		    //desenharPosicoesPlanetas(g);
		    // Escolha o formato: JPEG, GIF ou PNG
		    String nome = mapa.getDadosPessoais().getNome().replaceAll(" ", "_").toUpperCase();
		    
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
	    	  
	    	  //PlanetaPosicao pp = mapa.getPosicoesPlanetas().get(y);
			  PlanetaSignoResponse pp = mapa.getPlanetasSignos().get(y);

			  EnumPlaneta enumPlaneta = EnumPlaneta.getBySigla(pp.getPlaneta());
			  EnumSigno enumSigno = EnumSigno.getBySigla(pp.getSigno());
	    	  
	    	  // Símbolo Planeta
		      g.setFont(fontAstro);
	    	  g.drawString(	enumPlaneta.getLetra(),
	       					w,
	       					26 + (h * (y+1)) );

	    	  // Nome Planeta
	    	  g.setFont(fontTimes);
	    	  g.drawString(	enumPlaneta.getNome(),
		       				22 + w,
		       				26 + (h * (y+1)) );

	    	  // Grau Signo
	    	  g.setFont(fontTimes);
	    	  g.drawString(	pp.getGg() + "°" ,
	       				130 + w,
	       				26 + (h * (y+1)) );
	    	  
	    	  // Simbolo Signo
		      g.setFont(fontAstro);
	    	  g.drawString(	enumSigno.getLetra() ,
		       				160 + w,
		       				26 + (h * (y+1)) );
	    	  
	    	  g.setFont(fontTimes);
	    	  g.drawString(	pp.getMm()+"'" ,
	       				176 + w,
	       				26 + (h * (y+1)) );
	    	  
	    	  g.drawString(	enumSigno.getNome() ,
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
	      for (AspectoResponse ite : mapa.getAspectos()) {
			  EnumAspecto enumAspecto = EnumAspecto.getBySigla(ite.getAspecto());

			  int x = ite.getX();
	    	  int y = ite.getY();

	    	  g.drawString(enumAspecto.getLetra(),
	    			       margemX + (x * w) +10,
	    			       30 + ((y+1) * h)  -4);
	      }
	  }

	private void drawMapaPosicoes(Graphics2D g) {
		g.setColor(Color.red);
		boolean alternador = false;
		Integer acrescimo = 50;
		Font font = this.getFontAstrologia();
		// Planetas
		g.setFont(font.deriveFont(28f));

		List<ItemDesenhoMapa> lista =
		mapa.getPlanetasSignos().stream().filter(ps -> {
			EnumPlaneta enumPlaneta = EnumPlaneta.getBySigla(ps.getPlaneta());
			return !(EnumPlaneta.ASC.equals(enumPlaneta) || EnumPlaneta.MCE.equals(enumPlaneta));
		}).map(ps -> {
			EnumPlaneta enumPlaneta = EnumPlaneta.getBySigla(ps.getPlaneta());
			return new ItemDesenhoMapa(	enumPlaneta.getLetra(),
					ps.getG360(),
					ps.getGg(),
					ps.getMm()
			);
		}).sorted().collect(Collectors.toList());

		for ( ItemDesenhoMapa item : lista ) {
			Integer grau = item.getGrau360()-mapa.getDadosPessoais().getGrausDefasagemAscendente();

			alternador = !alternador;
			acrescimo = alternador ? 50 : 90;

			// ATENCAO! REDUZIR A DEFASAGEM DO SIGNO ASCENDENTE!!!
			Point ptLetra = angleToPoint(CX, CY, grau, RAIO_CIRCULO_INTERNO +acrescimo);
			g.drawString(item.getTexto(),
						ptLetra.x - (BIG_DOT / 2) - PADDING,
			  			ptLetra.y - (BIG_DOT / 2));

			Point ptBeta = angleToPoint(CX, CY, grau, RAIO_CIRCULO_INTERNO);
			g.fillOval(	ptBeta.x - (PQ_DOT / 2) - PADDING,
					ptBeta.y - (PQ_DOT / 2) - PADDING,
					PQ_DOT,
					PQ_DOT);
		}

		g.setColor(Color.black);
		for (int i = 0; i <= 11; i++) {
			CuspideResponse c = mapa.getCuspides().get(i);

			Point ptLetra = angleToPoint(CX, CY, i*30, RAIO_SEGUNDO_CIRCULO + 15);
			Point ptAntes = angleToPoint(CX, CY,i*30-5, RAIO_SEGUNDO_CIRCULO + 15);
			Point ptDepois = angleToPoint(CX, CY,i*30+5, RAIO_SEGUNDO_CIRCULO + 15);
			g.setFont(font.deriveFont(20f));

			EnumSigno enumSigno = EnumSigno.getBySigla(c.getSigno());

			g.drawString(enumSigno.getLetra() ,
				ptLetra.x - (BIG_DOT / 2) - PADDING,
				ptLetra.y - (BIG_DOT / 2));

			g.setFont(new Font("TimesRoman", Font.PLAIN , 13));

			String grau = c.getGg()+"°";
			String minuto = c.getMm().toString()+"'";

			String txtAntes = grau;
			String txtDepois = minuto;
			if (Arrays.asList(8,9,10,11,12).contains(i+1)) {
			  txtAntes = minuto;
			  txtDepois = grau;
			}

			g.drawString(	txtAntes,
						ptAntes.x - (BIG_DOT / 2) - PADDING,
						ptAntes.y - (BIG_DOT / 2));

			g.drawString(	txtDepois,
						ptDepois.x - (BIG_DOT / 2) - PADDING,
						ptDepois.y - (BIG_DOT / 2));
			}
  }

  public static Font getFontAstrologia() {
	    Font font = null;
	  	//String fName = "/fonts/AstroDotBasic.ttf";
	  String fName = "/fonts/AstroDotBasic.ttf";
	  URL arquivoFonte = ChartDraw.class.getClassLoader().getResource("fonts/AstroDotBasic.ttf");
	    try {
	      InputStream is = ChartDraw.class.getResourceAsStream(fName);
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

  private Point angleToPoint(int x, int y, int angulo, int radius) {
	double a = 2 * Math.PI * (angulo - 90) / 360;
	int ptX = (int)(radius * Math.sin(a) + x);
	int ptY = (int)(radius * Math.cos(a) + y);
    return new Point(ptX, ptY);
  }

}