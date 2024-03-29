package br.itarocha.cartanatal.core.decorator;

import br.itarocha.cartanatal.core.model.domain.*;
import br.itarocha.cartanatal.adapter.in.web.controller.dto.AspectoResponse;
import br.itarocha.cartanatal.adapter.in.web.controller.dto.CartaNatalResponse;
import br.itarocha.cartanatal.adapter.in.web.controller.dto.CuspideResponse;
import br.itarocha.cartanatal.adapter.in.web.controller.dto.PlanetaSignoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChartDraw {

	private static final int SIZE = 600;
	private static final int PADDING = 10;
	private static final int MARGEM_ASPECTOS = 360;

	private static final int WIDTH = 640;
	private static final int HEIGHT = 640;
	private static final int CX = WIDTH / 2;
	private static final int CY = HEIGHT / 2;

	private static final int RAIO_SEGUNDO_CIRCULO = (WIDTH / 2) - 50;
	private static final int RAIO_CIRCULO_INTERNO = (WIDTH / 4) - 40;
	private static final int BIG_DOT = 8;
	private static final int PQ_DOT = 5;

	@Value("${parametros.diretorioExportacao}")
	private String EXPORT_DIRECTORY;

	@Value("${parametros.diretorioFonts}")
	private String FONTS_DIR;

	private Font fontRegular = null;
	private Font fontAstrologia = null;

	@PostConstruct
	public void carregarFontes(){
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		/*
		Font[] fonts = ge.getAllFonts();
		System.out.println("****************** LISTANDO FONTES **********************");
		for(Font font : fonts){
			System.out.print(font.getFontName() + " : ");
			System.out.println(font.getFamily());
		}
		System.out.println("****************** FIM DA LISTAGEM DE FONTES **********************");
		*/

		// String path = tempFile.getAbsolutePath();
		//FontFactory.register(path);

		//URL font_path = Thread.currentThread().getContextClassLoader().getResource("fontname");
		//FontFactory.register(font_path.toString(), "test_font");
		//FontFactory.getFont("LiberationSans", 8));
		//fontAstrologia = loadFont("/fonts/AstroDotBasic.ttf"); //buildFontAstrologia();
		//fontRegular = loadFont("/fonts/Roboto-Regular.ttf"); //buildFontRegular();

		fontAstrologia = buildFontAstrologia();
		fontRegular = buildFontRegular();

	}

	public Font loadFont(String fontName) {
		File f = null;
		try {
			f = File.createTempFile("dang", "tmp");
			assert f != null;
			f.delete();
			ClassLoader classLoader = getClass().getClassLoader();
			Font font = Font.createFont(Font.TRUETYPE_FONT, classLoader.getResourceAsStream(fontName));
			//font.deriveFont(105f);
			System.out.println(font.getFontName());
			return font;
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
		return new Font("serif", Font.PLAIN, 24);
	}

	public Font getFontAstrologia(){
		return this.fontAstrologia;
	}

	public Font getFontRegular(){
		return this.fontRegular;
	}

	public void drawMapa(CartaNatalResponse mapa) {
		try {
			BufferedImage bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
	
			Graphics2D g = bi.createGraphics();
			g.setRenderingHint(	RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		    drawMapaFundo(g);
		    drawMapaPosicoes(mapa, g);
		    // Escolha o formato: JPEG, GIF ou PNG
		    
		    String nome = mapa.getDadosPessoais().getNome().replaceAll(" ", "_").toUpperCase();
		    String dest = String.format("%smapa.png",EXPORT_DIRECTORY);
		    ImageIO.write(bi, "png",  new File(dest));

		  } catch (IOException ie) {
		    ie.printStackTrace();
		  }
	}

	public void drawAspectos(CartaNatalResponse mapa) {
		try {
			int largura = 500 + 300;
			int altura = 300;

			BufferedImage bi = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = bi.createGraphics();

			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			drawAspectosFundo(mapa, g, largura, altura);
			String dest = String.format("%saspectos.png",EXPORT_DIRECTORY);
			ImageIO.write(bi, "png",  new File(dest));

			//ImageIO.write(bi, "PNG",  file);
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
		int RAIO_MAIOR_A = PADDING + 30;
		int RAIO_MAIOR_B = SIZE - (30 * 2);
		g.setColor(Color.BLACK);
		g.drawOval(RAIO_MAIOR_A, RAIO_MAIOR_A, RAIO_MAIOR_B, RAIO_MAIOR_B);

		// Media
		int MARGEM_INTERNA = SIZE / 2;
		int RAIO_MEDIO_A = PADDING + (MARGEM_INTERNA / 2);
		int RAIO_MEDIO_B = SIZE - MARGEM_INTERNA;
		g.setColor(Color.BLACK);
		g.drawOval(RAIO_MEDIO_A, RAIO_MEDIO_A, RAIO_MEDIO_B, RAIO_MEDIO_B);

		g.setColor(Color.BLACK);
		g.drawOval(	PADDING + (MARGEM_ASPECTOS / 2),
				PADDING + (MARGEM_ASPECTOS / 2),
				SIZE - MARGEM_ASPECTOS,
				SIZE - MARGEM_ASPECTOS);

		// CASAS
		//Font font = new Font("TimesRoman", Font.BOLD, 14);
		Font font = this.getFontRegular().deriveFont(Font.BOLD, 14f);
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

	private void drawAspectosFundo(CartaNatalResponse mapa, Graphics2D g, int largura, int altura) {
	      g.setStroke(new BasicStroke(1));
	      g.setColor(Color.white );
	      //g.fillRect(0, 0, 500, 380);
	      g.fillRect(0, 0, largura, altura);
	      
	      int margemX = 40 + 320;
	      g.setColor(Color.black );
	      
	      int w = 35; // pode ser a largura da letra "W"
	      int h = 20; // pode ser a altura da letra "W"
	      
	      //Font fontAstroX = this.getFontAstrologia();
	      
	      Font fontAstro = this.getFontAstrologia().deriveFont(20f);

		  //Font fontTimes = new Font("TimesRoman", Font.BOLD, 14);
		  Font fontTimes = this.getFontRegular().deriveFont(Font.BOLD,14f);

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

	private void drawMapaPosicoes(CartaNatalResponse mapa, Graphics2D g) {
		g.setColor(Color.RED);
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

		System.out.println("defasagem ascendente (shift) = "+mapa.getDadosPessoais().getGrausDefasagemAscendente());
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

			//g.setFont(new Font("TimesRoman", Font.PLAIN , 13));
			g.setFont(this.getFontRegular().deriveFont(Font.PLAIN, 13f) );

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

	public static Font getFont(String name) {
		Font font = null;
		/*
		if (cache != null) {
			if ((font = cache.get(name)) != null) {
				return font;
			}
		}
		*/
		String fName = "/fonts/" + name;
		try {
			InputStream is = ChartDraw.class.getResourceAsStream(fName);
			font = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(fName + " not loaded.  Using serif font.");
			font = new Font("serif", Font.PLAIN, 24);
		}
		return font;
	}

	public Font buildFontAstrologia() {
		//String f = DIRETORIO_FONTS + "AstroDotBasic.ttf";
		//URL url = this.getClass().getClassLoader().getResource(f);
		Path arquivoFonte = Paths.get(FONTS_DIR, "AstroDotBasic.ttf");
		try {
			System.out.println("Tentando carregar fonte de Astrologia: "+arquivoFonte);
			//File file = new File(url.getFile()); // arquivoFonte.toFile();
			File file = arquivoFonte.toFile();
			return Font.createFont(Font.TRUETYPE_FONT, file);
		} catch (Exception ex) {
			System.err.println(arquivoFonte + " nao carregada. Usando fonte serif.");
			ex.printStackTrace();
		}
		return null;
  	}

	public Font buildFontRegular() {
		//String f = DIRETORIO_FONTS + "Roboto-Regular.ttf";
		//URL url = this.getClass().getClassLoader().getResource(f);
		Path arquivoFonte = Paths.get(FONTS_DIR, "Roboto-Regular.ttf");
		try {
			System.out.println("Tentando carregar fonte de Regular: "+arquivoFonte);
			//File file = new File(url.getFile()); // arquivoFonte.toFile();
			File file = arquivoFonte.toFile();
			System.out.println("Tentando carregar fonte de Regular: "+file.toPath());
			return Font.createFont(Font.TRUETYPE_FONT, file);
		} catch (Exception ex) {
			System.err.println(arquivoFonte + " nao carregada. Usando fonte serif.");
			ex.printStackTrace();
		}
		return null;
	}

  	/*
	public Font buildFontRegular() {
		Path arquivoFonte = Paths.get(FONTS_DIR, "Roboto-Regular.ttf");
		try {
			System.err.println("Tentando carregar fonte Regular: "+arquivoFonte.toAbsolutePath());
			return Font.createFont(Font.TRUETYPE_FONT, arquivoFonte.toFile());
		} catch (Exception ex) {
			System.err.println(arquivoFonte.toFile() + " nao carregada. Usando fonte serif.");
			ex.printStackTrace();
		}
		return null;
	}
	*/

	private Point angleToPoint(int x, int y, int angulo, int radius) {
		double a = 2 * Math.PI * (angulo - 90) / 360;
		int ptX = (int)(radius * Math.sin(a) + x);
		int ptY = (int)(radius * Math.cos(a) + y);
		return new Point(ptX, ptY);
	}

}

/*

<path d="M 405.344799471418, 706.2033566416449 l 0.7635427816311477, 43.743336663092116 A 350, 350,0 ,0, 0, 580.2633262185191, 700.0085552457392 l -22.53291577731488, -37.50106940571741 A 306.25, 306.25,0 ,0, 1, 405.344799471418, 706.2033566416449" fill="none"></path>

 */