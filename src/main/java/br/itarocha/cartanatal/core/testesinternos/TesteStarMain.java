package br.itarocha.cartanatal.core.testesinternos;

import br.itarocha.cartanatal.core.StarMain;
import br.itarocha.cartanatal.core.model.presenter.CartaNatal;

public class TesteStarMain {

    //http://th-mack.de/international/download/index.html
    //http://www.radixpro.org/fix-east-west.html
    public static void main( String[] args) {
        // Rode com os seguintes argumentos
        // "Itamar Rocha" "29/06/1972" "05:00" "Caxias" "MA"

        String[] argumentos = new String[5];
        String nome;
        String data;
        String hora;
        String cidade;
        String uf;
        if (args.length < 5){
            System.out.println("Entre com os 5 parametros com espaco");
            System.out.println("Nome");
            System.out.println("Data de nascimento formato DD/MM/AAAA");
            System.out.println("Hora de nascimento qualquer formato com ou sem segundos com delimitador \".\" ou \":\" HH:MM OU HH.MM.SS");
            System.out.println("Cidade de nascimento");
            System.out.println("UF de nascimento");
            nome = "Itamar Rocha Chaves Junior";
            data = "29/06/1972";
            hora = "05:00";
            cidade = "Caxias";
            uf = "MA";
        } else {
            nome = args[0];
            data = args[1];
            hora = args[2];
            cidade = args[3];
            uf = args[4];
        }

        StarMain m = new StarMain();
        try {
            CartaNatal mapa = m.buildMapa(nome, data, hora, cidade, uf);
            /*
            if (mapa != null) {
                String json = new DecoradorMapa(mapa).getJSON();
                //Grafico.build(mapa);
                ChartPainter cp = new ChartPainter(mapa,".");
                System.out.println(json);
            }
             */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
