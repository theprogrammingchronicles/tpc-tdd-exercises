/*
 * Copyright (C) 2010-2011, Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 *
 * This file is part of The Programming Chronicles Test-Driven Development
 * Exercises(http://theprogrammingchronicles.com/)
 *
 * This copyrighted material is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This material is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this material. This copy is available in LICENSE-GPL.txt
 * file. If not, see <http://www.gnu.org/licenses/>.
 */
package com.programmingchronicles.tdd.horoscope;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test de la clase encargada de predecir los horoscopos.
 *
 * <p/>
 * <b>COBERTURA:</b>
 *    <p>
 *    Aunque hemos conseguido cobertura completa en el código funcional, en
 *    un caso como este sería conveniente también probar los valores que hacen
 *    frontera entre condicionantes. </p>
 *
 *    <p>
 *    Este ejemplo se ha desarrollo con TDD, los tests se han escrito
 *    incrementalmente conforme se necesitaban para construir el código
 *    funcional.</p>
 *
 *    <p>
 *    En este caso posiblemente lo que ha pasado es que no hemos ideado desde
 *    el principio todos los tests que serían necesarios para validar completamente
 *    la funcionalidad, ya que los test que teniamos nos estaban dirigiendo bien
 *    los desarrollos, sería bueno repasar y ver si realmente sirve de algo probar
 *    los valores de frontera. </p>
 *
 * <b>BRANCH COVERAGE:</b>
 *    <p>
 *    Quitar uno de los tests para ver que es posible conseguir cobertura
 *    completa de lineas pero no de condicionales (con los informes de
 *    cobertura).</p>
 *
 * <b>ANTIPATRONES</b>
 *    <p>
 *    Aparte de los temas de cobertura de código, es muy importante fijarse
 *    en el hecho de que en la clase TestHoroscope, probamos UNICA
 *    y EXCLUSIVAMENTE la clase Horoscope. La clase SmartDateParser ya tiene
 *    sus propios tests y asumimos que funciona bien.</p>
 *
 *    <p>
 *    Es un antipatron el convertir un test de una clase en un test de
 *    sus dependencias. Por ejemplo, probando en Horoscope con todos los
 *    formatos de fechas. Al final el test de una clase acaba
 *    convirtiendose en test de las dependencias. Esto hace los tests
 *    más complejos a la vez que redundantes. Los tests de formato deben
 *    estar en la clase TestSmartDateParser.</p>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class TestHoroscope {
    private Horoscope horoscope = new Horoscope();

    @Test
    public void testLuckSummerSunday() {
        // El 1 de agosto del 2010 es domingo
        String birthday = "1/8/2010";

        horoscope.makePretictions(birthday);

        assertEquals(10, horoscope.getLuck());
    }

    @Test
    public void testLuckSummerWorkingDay() {
        // El 11 de agosto del 2010 es miercoles
        String birthday = "11/8/2010";

        horoscope.makePretictions(birthday);

        assertEquals(8, horoscope.getLuck());
    }

    // Como los meses se inicializan en el cambio
    // de año vamos a probar los dos años
    @Test
    public void testLuckChristmasBeforeNewYear() {
        // El 26 de diciembre estamos en navidad
        String birthday = "26/12/2010";

        horoscope.makePretictions(birthday);

        assertEquals(9, horoscope.getLuck());
    }


    @Test
    public void testLuckChristmasAfterNewYear() {
        // El 2 de enero estamos en navidad
        String birthday = "2/1/2010";

        horoscope.makePretictions(birthday);

        assertEquals(9, horoscope.getLuck());
    }

    /**
     * Los meses se inicializan en el cambio de año. Para la navidad hay que
     * probar los dos años.
     *
     * <p>
     * Desactivar este test implica una perdida de cobertura en la evaluación
     * de expresiones de un condicional en lugar lineas de codigo (branch
     * coverage).</p>
     *
     * <p>
     * Se puede ver más claro si se descomenta el método "isJanuaryChristmas"
     * y cambiar el if testeado para ver la perdida de cobertura en forma
     * de lineas en lugar de ramas.
     *
     * <code> if((month==12 && day >= 25) || isJanuaryChristmas(day, month)) </code>
     */
    @Test
    public void testLuckWinterBeforeNewYear() {
        // El 23 de diciembre no estamos en navidad
        String birthday = "23/12/2010";

        horoscope.makePretictions(birthday);

        assertEquals(6, horoscope.getLuck());
    }

    @Test
    public void testLuckWinterAfterNewYear() {
        // El 8 de enero ya terminaron las vacaciones.
        String birthday = "8/1/2010";

        horoscope.makePretictions(birthday);

        assertEquals(6, horoscope.getLuck());
    }

    /**
     * Desactivar el test para observar la perdida de cobertura.
     *
     * <p>
     * Si se desactiva este test, a parte de perder cobertura de líneas también
     * se pierde cobertura en la evaluación de expresiones en condicionales
     * (branch coverage), es decir una condición de un if que no se ha evaluado.</p>
     *
     *  <code>if(month == 12 || month <= 2)</code>
     *
     *  <p>
     *  La expresión "month == 12" si ha sido evaluada por otros
     *  test, pero la expresión "month &lt;= 2" no ha sido evaluada.</p>
     *
     *  <p>
     *  Recordemos que en un OR la segunda expresión no se evalua si
     *  la primera es cierta.</p>
     */
    @Test
    public void testLuckOtherDays() {
        // El que nace el resto del año no tiene nada de suerte.
        String birthday = "4/4/2010";

        horoscope.makePretictions(birthday);

        assertEquals(1, horoscope.getLuck());
    }
}