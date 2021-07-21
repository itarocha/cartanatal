package br.itarocha.cartanatal.core.util.horarioverao;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HorarioVeraoTest {

    private HorarioVerao horarioVerao;

    @Test
    public void testar1986a1987() {
        LocalDateTime dhThaysa = LocalDateTime.of(1987, 11, 21, 8, 30);
        LocalDateTime dhIsaque = LocalDateTime.of(2018, 11, 21, 0, 2);
        horarioVerao = new HorarioVerao();
        assertTrue(horarioVerao.isHorarioVerao("MG", dhThaysa));

        assertFalse(horarioVerao.isHorarioVerao("MG", dhIsaque));

        assertTrue(horarioVerao.isHorarioVerao("BA", LocalDateTime.of(1988,10,16,0,0,0)));
        assertTrue(horarioVerao.isHorarioVerao("RJ", LocalDateTime.of(1989,1,29,0,0,0)));
        assertFalse(horarioVerao.isHorarioVerao("AC", LocalDateTime.of(1989,1,29,0,0,0)));
        assertFalse(horarioVerao.isHorarioVerao("RJ", LocalDateTime.of(1989,1,29,0,0,1)));
    }

}