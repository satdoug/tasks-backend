package br.ce.wcaquino.taskbackend.utils;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DateUtilsTest {

    @Test
    public void deveRetornarTrueParaDatasFuturas(){
        LocalDate localDate = LocalDate.of(2030,01,25);
        assertTrue(DateUtils.isEqualOrFutureDate(localDate));
    }

    @Test
    public void deveRetornarFalseParaDatasPassadas(){
        LocalDate localDate = LocalDate.of(2019,01,25);
        assertFalse(DateUtils.isEqualOrFutureDate(localDate));
    }

    @Test
    public void deveRetornarTrueParaDatasPresente(){
        LocalDate localDate = LocalDate.now();
        assertTrue(DateUtils.isEqualOrFutureDate(localDate));
    }
}
