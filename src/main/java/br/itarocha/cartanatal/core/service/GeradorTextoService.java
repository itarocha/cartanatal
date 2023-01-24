package br.itarocha.cartanatal.core.service;

import br.itarocha.cartanatal.core.model.domain.CuspideCasa;
import br.itarocha.cartanatal.core.model.domain.ItemAspecto;
import br.itarocha.cartanatal.core.model.domain.Mapa;
import br.itarocha.cartanatal.core.model.domain.PlanetaPosicao;
import br.itarocha.cartanatal.core.util.Funcoes;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

@Component
public class GeradorTextoService {

    private StringBuilder sb;

    public void display(Mapa mapa){
        sb = new StringBuilder();
        sb.append("============================================================\n");
        sb.append("CABECALHO\n");
        sb.append("------------------------------------------------------------\n");
        displayCabecalho(mapa);

        sb.append("============================================================\n");
        sb.append("POSICOES DOS PLANETAS\n");
        sb.append("------------------------------------------------------------\n");
        mapa.getPosicoesPlanetas().forEach(p -> displayPlanetaPosicao(p));

        sb.append("============================================================\n");
        sb.append("CUSPIDES DAS CASAS\n");
        sb.append("------------------------------------------------------------\n");
        mapa.getListaCuspides().forEach(c -> displayCuspides(c));

        sb.append("============================================================\n");
        sb.append("ASPECTOS\n");
        sb.append("------------------------------------------------------------\n");
        mapa.getListaAspectos().forEach(this::displayAspecto);
        sb.append("============================================================\n");
        System.out.println(sb.toString());
    }

    private void displayCabecalho(Mapa mapa) {
        sb.append(String.format("%s %s\n", fixedLengthString("Nome:",22), mapa.getNome()));
        sb.append(String.format("%s %s\n", fixedLengthString("Data Hora Nascimento:",22), mapa.getDataHora()));
        sb.append(String.format("%s %s\n", fixedLengthString("Cidade Nascimento:",22), mapa.getNomeCidade()));

        String latitude = String.format("%02d%s%02d'%02d",
                mapa.getLatitude().getGrau(),
                mapa.getLatitude().isPositive() ? "n" : "s",
                mapa.getLatitude().getMinuto(),
                mapa.getLatitude().getSegundo());

        String longitude = String.format("%02d%s%02d'%02d",
                mapa.getLongitude().getGrau(),
                mapa.getLongitude().isPositive() ? "e" : "w",
                mapa.getLongitude().getMinuto(),
                mapa.getLongitude().getSegundo());

        sb.append(String.format("%s %s, %s\n", fixedLengthString("Coordenadas:",22), longitude, latitude));
        sb.append(String.format("%s %s\n", fixedLengthString("Jul.Dia:",22), mapa.getJulDay()));

        int[] gnc = Funcoes.grauToArray(mapa.getSideralTime());
        String horaSideral = String.format("%02d:%02d:%02d", gnc[0],gnc[1],gnc[2]);

        sb.append(String.format("%s %s\n", fixedLengthString("Tempo Sideral:",22), mapa.getSideralTime()));
        sb.append(String.format("%s %ss\n", fixedLengthString("Delta T:",22), mapa.getDeltaTSec()));

    }

    private void displayPlanetaPosicao(PlanetaPosicao p) {
        String planeta = fixedLengthString(p.getEnumPlaneta().getNome(), 12);
        String retrogrado = p.isRetrogrado() ? "R" : " ";

        String dsc = String.format("%s %s %s°%s'%s\" %s %02d",
                planeta,
                p.getEnumSigno().getSigla(),
                p.getLocalizacao().getGnc(),
                p.getLocalizacao().getM(),
                p.getLocalizacao().getS(),
                retrogrado,
                p.getCasa()
        );
        sb.append(dsc+"\n");
    }

    private void displayCuspides(CuspideCasa c) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat formatter = (DecimalFormat) nf;
        formatter.applyPattern("000.000000000000000");

        String dscCasa = "";
        switch ( c.getNumero() ){
            case 1 : dscCasa = "ASC"; break;
            case 7 : dscCasa = "DESC"; break;
            case 10 : dscCasa = "MC"; break;
        }

        String dsc = String.format("%02d %s %s°%s'%s\" %s %s",
                c.getNumero(),
                c.getEnumSigno().getSigla(),
                c.getLocalizacao().getGnc(),
                c.getLocalizacao().getM(),
                c.getLocalizacao().getS(),
                formatter.format(c.getLocalizacao().getPosicao()),
                dscCasa
                );
        sb.append(dsc+"\n");
    }

    private String fixedLengthString(String string, int length) {
        //return String.format("%1$"+length+ "s", string);
        return String.format("%-"+length+ "s", string);
    }

    private void displayAspecto(ItemAspecto a) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat formatter = (DecimalFormat) nf;
        formatter.applyPattern("000.000000");
        String dscAspectoSimbolico = String.format("%s %s %s", a.getPlanetaOrigem(),
                a.getAspecto().getUnicode(),
                a.getPlanetaDestino());

        String dscAspectoTexto = String.format("%s %s %s", a.getPlanetaOrigem(),
                a.getAspecto().getSigla().toUpperCase(),
                a.getPlanetaDestino());

        String coordenada = String.format("[%02d,%02d]", a.getX(), a.getY());

        String posPlanetas = String.format("%s %s",
                formatter.format(a.getPlanetaOrigemAngulo()),
                formatter.format(a.getPlanetaDestinoAngulo())
        );
        sb.append(dscAspectoSimbolico+" "+a.getAspecto().getSigla()+ " "+a.getOrbeDescricao()+" "+posPlanetas+" "+coordenada+"\n");
    }

}