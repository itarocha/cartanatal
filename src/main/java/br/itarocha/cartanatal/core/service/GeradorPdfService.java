package br.itarocha.cartanatal.core.service;

import br.itarocha.cartanatal.core.model.domain.EnumPlaneta;
import br.itarocha.cartanatal.core.model.domain.EnumSigno;
import br.itarocha.cartanatal.core.model.presenter.CartaNatalResponse;
import br.itarocha.cartanatal.core.model.presenter.PlanetaSignoResponse;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class GeradorPdfService {

    private static final String FONT_ASTRO = "src/main/resources/fonts/AstroDotBasic.ttf";

    public void montarArquivoPdf(CartaNatalResponse mapa, Map<String, String> map) throws IOException {
        String  nome = mapa.getDadosPessoais().getNome().replaceAll(" ", "_").toLowerCase();

        String url = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        String dest = String.format("%s/file_%s.pdf",url,nome);

        PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdf);
        document.setTextAlignment(TextAlignment.JUSTIFIED);

        PdfFont font = PdfFontFactory.createFont(FONT_ASTRO, true);
        PdfFont bold = PdfFontFactory.createFont(FONT_ASTRO, true);

        PdfFont fontCourier = PdfFontFactory.createFont(FontConstants.COURIER);

        List<PlanetaSignoResponse> planetas = mapa.getPlanetasSignos();

        Paragraph pa = new Paragraph();

        EnumPlaneta[] array = {EnumPlaneta.SOL, EnumPlaneta.ASC, EnumPlaneta.MCE};
        List<EnumPlaneta> desconsiderados = Arrays.asList(array);

        planetas.stream()
                .filter(ps -> !desconsiderados.contains(EnumPlaneta.getBySigla(ps.getPlaneta())) )
                .forEach(pp -> {
                    EnumPlaneta enumPlaneta = EnumPlaneta.getBySigla(pp.getPlaneta());
                    enumPlaneta.getLetra();
                    EnumSigno enumSigno = EnumSigno.getBySigla(pp.getSigno());

                    Text tLetraPlaneta = new Text(   enumPlaneta.getLetra()   ).setFontSize(12).setFont(font);
                    Text tLetraSigno = new Text(  enumSigno.getLetra()  ).setFontSize(12).setFont(font);

                    String tmp = String.format(" %s°%s'%s\" casa %s ", pp.getGg(), pp.getMm(), pp.getSs(), (int)pp.getCasa());
                    pa.add(tLetraPlaneta)
                            .add(new Text(" "+enumPlaneta.getNome()+" ").setFontSize(8))
                            .add(tLetraSigno)
                            .add(new Text(tmp).setFontSize(8))
                            .add("\n");


                    //String key = String.format("%s em %s", enumPlaneta.getNome(), enumSigno.getNome());
                    //map.put(key, isNull(ps) ? NOT_FOUND : ps.getTexto());
                });
		 /*
		 for(EnumPlaneta x : EnumPlaneta.values()) {
			 //System.out.println("buscando "+x.getId());
			 PlanetaSignoResponse pp = planetas.stream().filter(obj -> obj.getEnumPlaneta().getSigla().equals(x.getSigla())).findAny().orElse(null);
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
		 */
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

}
