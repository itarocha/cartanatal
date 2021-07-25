package br.itarocha.cartanatal.core.service;

import br.itarocha.cartanatal.core.model.Pair;
import br.itarocha.cartanatal.core.model.presenter.Paragrafo;
import br.itarocha.cartanatal.core.model.presenter.ParagrafoImagem;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.isNull;

// https://www.tutorialspoint.com/apache_poi_word/index.htm

public class MapaWord {

    private static final String FONT_NAME = "Roboto";

    public static void gerarWord(String fileName, List<Paragrafo> paragrafos) throws Exception{
        try (XWPFDocument doc = new XWPFDocument()) {

            paragrafos.stream().forEach(p -> {
                switch (p.getEstilo()){
                    case IMAGEM:
                        imprimirImagem(doc, p);
                        break;
                    case TITULO_PRINCIPAL:
                        imprimirTitulo(doc, p.getTexto());
                        break;
                    case TITULO_PARAGRAFO:
                        imprimirTituloParagrafo(doc, p.getTexto());
                        break;
                    case TITULO_SIMPLES:
                        imprimirTituloSimples(doc, p.getTexto());
                        break;
                    case PARAGRAFO_NORMAL:
                    case PARAGRAFO_ITALICO:
                        imprimirParagrafo(doc, p.getTexto());
                        break;
                    case TABELA:
                        imprimirTabela(doc, p.getTabela());
                }
            });
            try (FileOutputStream out = new FileOutputStream(fileName)) {
                doc.write(out);
            }
        }
    }

    private static void imprimirImagem(XWPFDocument doc, Paragrafo p) {
        ParagrafoImagem imagem = p.getImagem();
        if (isNull(imagem)) {
            return;
        }

        XWPFParagraph title = doc.createParagraph();
        XWPFRun run = title.createRun();
        //run.setText("Fig.1 A Natural Scene");
        run.setBold(true);
        title.setAlignment(ParagraphAlignment.CENTER);

        FileInputStream is = null;
        try {
            is = new FileInputStream(imagem.getFileName());
            run.addBreak();
            run.addPicture(is, XWPFDocument.PICTURE_TYPE_PNG,
                    imagem.getFileName(),
                    Units.toEMU(imagem.getWidth()),
                    Units.toEMU(imagem.getHeight())); // pixels
            is.close();
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    private static void inserirImagem(XWPFDocument doc, String imgFile, int width, int height) {
    }

    public static void main(String[] args) throws Exception {
        try (XWPFDocument doc = new XWPFDocument()) {

            String titulo = "O Mapa Natal";
            String textoParagrafo = "O Mapa Natal mostra a posição correta dos astros em relação à Terra no momento de nascimento de uma determinada pessoa. Ele captura o momento no tempo e o congela. Da sua perspectiva na Terra, os planetas parecem se mover à sua volta, de modo que você está no centro do seu mapa astral. As configurações de um Mapa Natal se repetem apenas a cada 26.000 anos, portanto ele é quase como uma impressão digital - não existe um igual ao outro.";

            imprimirTitulo(doc, titulo);
            imprimirParagrafo(doc, textoParagrafo);
            imprimirParagrafo(doc, textoParagrafo);
            imprimirParagrafo(doc, textoParagrafo);


            /*
            XWPFParagraph _p2 = doc.createParagraph();
            //_p2.setWordWrapped(true);
            _p2.setAlignment(ParagraphAlignment.BOTH);
            _p2.setIndentationFirstLine(600);
            XWPFRun _r2 = _p2.createRun();
            //_r1.setBold(true);
            _r2.setFontFamily("Calibri");
            _r2.setText("O Mapa Natal mostra a posição correta dos astros em relação à Terra no momento de nascimento de uma determinada pessoa. Ele captura o momento no tempo e o congela. Da sua perspectiva na Terra, os planetas parecem se mover à sua volta, de modo que você está no centro do seu mapa astral. As configurações de um Mapa Natal se repetem apenas a cada 26.000 anos, portanto ele é quase como uma impressão digital - não existe um igual ao outro.");
            _r2.setFontSize(12);
            _r2.addBreak();

            XWPFParagraph _p3 = doc.createParagraph();
            //_p2.setWordWrapped(true);
            _p3.setAlignment(ParagraphAlignment.BOTH);
            _p3.setIndentationFirstLine(600);
            XWPFRun _r3 = _p3.createRun();
            //_r1.setBold(true);
            _r3.setFontFamily("Calibri");
            _r3.setText("O Mapa Natal mostra a posição correta dos astros em relação à Terra no momento de nascimento de uma determinada pessoa. Ele captura o momento no tempo e o congela. Da sua perspectiva na Terra, os planetas parecem se mover à sua volta, de modo que você está no centro do seu mapa astral. As configurações de um Mapa Natal se repetem apenas a cada 26.000 anos, portanto ele é quase como uma impressão digital - não existe um igual ao outro.");
            _r3.setFontSize(12);
            _r3.addBreak();
            */

            XWPFParagraph p1 = doc.createParagraph();
            p1.setAlignment(ParagraphAlignment.CENTER);
            //p1.setBorderBottom(Borders.DOUBLE);
            //p1.setBorderTop(Borders.DOUBLE);

            //p1.setBorderRight(Borders.DOUBLE);
            //p1.setBorderLeft(Borders.DOUBLE);
            //p1.setBorderBetween(Borders.SINGLE);

            p1.setVerticalAlignment(TextAlignment.TOP);

            XWPFRun r1 = p1.createRun();
            r1.setFontFamily("Courier");
            r1.setBold(true);
            r1.setText("A rápida raposa marrom");
            r1.setBold(true);
            r1.setUnderline(UnderlinePatterns.DOT_DOT_DASH);
            r1.setTextPosition(100);


            XWPFParagraph p2 = doc.createParagraph();
            p2.setAlignment(ParagraphAlignment.RIGHT);

            //BORDERS
            //p2.setBorderBottom(Borders.DOUBLE);
            //p2.setBorderTop(Borders.DOUBLE);
            //p2.setBorderRight(Borders.DOUBLE);
            //p2.setBorderLeft(Borders.DOUBLE);
            //p2.setBorderBetween(Borders.SINGLE);

            XWPFRun r2 = p2.createRun();
            r2.setText("jumped over the lazy dog");
            r2.setStrikeThrough(true);
            r2.setFontSize(20);

            XWPFRun r3 = p2.createRun();
            r3.setText("and went away");
            r3.setStrikeThrough(true);
            r3.setFontSize(20);
            r3.setSubscript(VerticalAlign.SUPERSCRIPT);

            /*
            // hyperlink
            XWPFHyperlinkRun hyperlink = p2.insertNewHyperlinkRun(0, "http://poi.apache.org/");
            hyperlink.setUnderline(UnderlinePatterns.SINGLE);
            hyperlink.setColor("0000ff");
            hyperlink.setText("Apache POI");
            */

            XWPFParagraph p3 = doc.createParagraph();
            p3.setWordWrapped(true);
            p3.setPageBreak(true);

            //p3.setAlignment(ParagraphAlignment.DISTRIBUTE);
            p3.setAlignment(ParagraphAlignment.BOTH);
            p3.setIndentationFirstLine(600);


            XWPFRun r4 = p3.createRun();
            r4.setTextPosition(20);
            r4.setText("To be, or not to be: that is the question: "
                    + "Whether 'tis nobler in the mind to suffer "
                    + "The slings and arrows of outrageous fortune, "
                    + "Or to take arms against a sea of troubles, "
                    + "And by opposing end them? To die: to sleep; ");
            r4.addBreak(BreakType.PAGE);
            r4.setText("No more; and by a sleep to say we end "
                    + "The heart-ache and the thousand natural shocks "
                    + "That flesh is heir to, 'tis a consummation "
                    + "Devoutly to be wish'd. To die, to sleep; "
                    + "To sleep: perchance to dream: ay, there's the rub; "
                    + ".......");
            r4.setItalic(true);
//This would imply that this break shall be treated as a simple line break, and break the line after that word:

            /*
            XWPFRun r5 = p3.createRun();
            r5.setTextPosition(-10);
            r5.setText("For in that sleep of death what dreams may come");
            r5.addCarriageReturn();
            r5.setText("When we have shuffled off this mortal coil, "
                    + "Must give us pause: there's the respect "
                    + "That makes calamity of so long life;");
            r5.addBreak();
            r5.setText("For who would bear the whips and scorns of time, "
                    + "The oppressor's wrong, the proud man's contumely,");

            r5.addBreak(BreakClear.ALL);
            r5.setText("The pangs of despised love, the law's delay, "
                    + "The insolence of office and the spurns " + ".......");
            */

            try (FileOutputStream out = new FileOutputStream("/home/itamar/teste/arquivo.docx")) {
                doc.write(out);
            }
        }
    }

    private static void imprimirParagrafo(XWPFDocument doc, String s) {
        XWPFParagraph _p3 = doc.createParagraph();
        //_p2.setWordWrapped(true);
        _p3.setAlignment(ParagraphAlignment.BOTH);
        _p3.setIndentationFirstLine(600);
        XWPFRun _r3 = _p3.createRun();
        _r3.setFontFamily(FONT_NAME);
        _r3.setText(s);
        _r3.setFontSize(12);
        _r3.addBreak();
    }

    private static void imprimirTitulo(XWPFDocument doc, String texto) {
        XWPFParagraph p = doc.createParagraph();
        XWPFRun r = p.createRun();
        r.setBold(true);
        r.setFontFamily(FONT_NAME);
        r.setText(texto);
        r.setFontSize(16);
        r.addBreak();
    }

    private static void imprimirTituloParagrafo(XWPFDocument doc, String texto) {
        XWPFParagraph p = doc.createParagraph();
        XWPFRun r = p.createRun();
        r.setBold(true);
        r.setFontFamily(FONT_NAME);
        r.setText(texto);
        r.setFontSize(14);
        r.addBreak();
    }

    private static void imprimirTituloSimples(XWPFDocument doc, String texto) {
        XWPFParagraph p = doc.createParagraph();
        XWPFRun r = p.createRun();
        p.setIndentationFirstLine(0);
        r.setFontFamily(FONT_NAME);
        r.setText(texto);
        r.setFontSize(12);
        r.addBreak();
    }


    private static void imprimirTabela(XWPFDocument doc, List<Pair> tabela) {
        XWPFTable table = doc.createTable();

        AtomicInteger index = new AtomicInteger(0);
        tabela.stream().forEach(p -> {
            if (index.getAndAdd(1) == 0){
                XWPFTableRow tableRowOne = table.getRow(0);
                tableRowOne.getCell(0).setText(p.getKey());
                tableRowOne.addNewTableCell().setText(p.getValue());
            } else {
                XWPFTableRow tableRowTwo = table.createRow();
                tableRowTwo.getCell(0).setText(p.getKey());
                tableRowTwo.getCell(1).setText(p.getValue());
            }
        });
    }

    public static void outroArquivo()throws Exception {

        //Blank Document
        XWPFDocument document = new XWPFDocument();

        //Write the Document in file system
        FileOutputStream out = new FileOutputStream(new File("/home/itamar/teste/arquivo.docx"));


        //create paragraph
        XWPFParagraph paragraph = document.createParagraph();

        //Set Bold an Italic
        XWPFRun paragraphOneRunOne = paragraph.createRun();
        paragraphOneRunOne.setBold(true);
        paragraphOneRunOne.setItalic(true);
        paragraphOneRunOne.setFontFamily(FONT_NAME);
        paragraphOneRunOne.setText("Font Style");
        paragraphOneRunOne.addBreak();

        //Set text Position
        XWPFRun paragraphOneRunTwo = paragraph.createRun();
        paragraphOneRunTwo.setText("Font Style two");
        paragraphOneRunTwo.setTextPosition(100);

        //Set Strike through and Font Size and Subscript
        XWPFRun paragraphOneRunThree = paragraph.createRun();
        paragraphOneRunThree.setStrike(true);
        paragraphOneRunThree.setFontSize(20);
        paragraphOneRunThree.setSubscript(VerticalAlign.SUBSCRIPT);
        paragraphOneRunThree.setText(" Different Font Styles");

        document.write(out);
        out.close();
        System.out.println("fontstyle.docx written successully");
    }
}
