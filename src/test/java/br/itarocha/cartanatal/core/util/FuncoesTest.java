package br.itarocha.cartanatal.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FuncoesTest {

    @Test
    void grauToArray() {
        double sol = 97.66305319;
        double lua = 307.46536473;
        double mer = 120.93410234;
        double ven = 80.14591501;
        double mar = 120.41847230;
        double jup = 272.87639667;
        double sat = 73.65477718;
        double ura = 194.21887711;
        double net = 243.00431590;
        double plu = 179.42141108;
        assertEquals("097-39-47",formatGMS(Funcoes.grauToArray(sol)));
        assertEquals("307-27-55",formatGMS(Funcoes.grauToArray(lua)));
        assertEquals("120-56-03",formatGMS(Funcoes.grauToArray(mer)));
        assertEquals("080-08-45",formatGMS(Funcoes.grauToArray(ven)));
        assertEquals("120-25-07",formatGMS(Funcoes.grauToArray(mar)));
        assertEquals("272-52-35",formatGMS(Funcoes.grauToArray(jup)));
        assertEquals("073-39-17",formatGMS(Funcoes.grauToArray(sat)));
        assertEquals("194-13-08",formatGMS(Funcoes.grauToArray(ura)));
        assertEquals("243-00-16",formatGMS(Funcoes.grauToArray(net)));
        assertEquals("179-25-17",formatGMS(Funcoes.grauToArray(plu)));
    }

    @Test
    void grauNaCasaToArray() {
        double sol = 97.66305319;
        double lua = 307.46536473;
        double mer = 120.93410234;
        double ven = 80.14591501;
        double mar = 120.41847230;
        double jup = 272.87639667;
        double sat = 73.65477718;
        double ura = 194.21887711;
        double net = 243.00431590;
        double plu = 179.42141108;
        assertEquals("007-39-47",formatGMS(Funcoes.grauNaCasaToArray(sol)));
        assertEquals("007-27-55",formatGMS(Funcoes.grauNaCasaToArray(lua)));
        assertEquals("000-56-03",formatGMS(Funcoes.grauNaCasaToArray(mer)));
        assertEquals("020-08-45",formatGMS(Funcoes.grauNaCasaToArray(ven)));
        assertEquals("000-25-07",formatGMS(Funcoes.grauNaCasaToArray(mar)));
        assertEquals("002-52-35",formatGMS(Funcoes.grauNaCasaToArray(jup)));
        assertEquals("013-39-17",formatGMS(Funcoes.grauNaCasaToArray(sat)));
        assertEquals("014-13-08",formatGMS(Funcoes.grauNaCasaToArray(ura)));
        assertEquals("003-00-16",formatGMS(Funcoes.grauNaCasaToArray(net)));
        assertEquals("029-25-17",formatGMS(Funcoes.grauNaCasaToArray(plu)));
    }

    private String formatGMS(int[] gms){
        return String.format("%03d-%02d-%02d",gms[0],gms[1],gms[2]);
    }

}