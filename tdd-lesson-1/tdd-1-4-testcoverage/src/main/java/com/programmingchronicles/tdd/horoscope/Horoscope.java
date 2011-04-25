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

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Implementación del horoscopo que mide la suerte en función del
 * día de nacimiento.
 * <p>
 * <b>NOTA</b>: Se ha implementado de forma un tanto extraña para explicar
 *              algunos conceptos de unit testing.
 * </p>
 *
 * <u>IMPLEMENTACION</u>:
 * <pre>
 * - Las fechas se pueden introducir en tres formatos y el
 *   y se detecta automaticamente el formato:
 *     + Corto: "12/10/2010"
 *     + Largo: "12 de octubre de 2010"
 *     + Día: "10" (sería el día 10 del mes y año actual)
 *
 * - Si el cumpleaños es en verano (Junio, Julio y Agosto):
 *    + Si es domingo la suerte es 10.
 *    + Si no es domingo la suerte es 8.
 *
 * - Si el cumpleaños es en invierno (Diciembre, Enero, Febrero):
 *    + Si es navidad (del 25 al 6) la suerte es 9.
 *    + Si no es navidad la suerte es 6.
 *
 * - El resto del año la suerte es 1.
 * </pre>
 * 
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class Horoscope {
    private static Calendar calendar = Calendar.getInstance(new Locale("es"));

    SmartDateParser parser = new SmartDateParser();
    int luck;

    /**
     * Realiza las predicciones según la fecha de nacimiento.
     *
     * @return fecha de nacimiento en los formatos soportados
     */
    public void makePretictions(String birthday) {
        Date date = parser.parse(birthday);

        calendar.setTime(date);

        // La clase calendar numera los meses de 0 a 11.
        makePredictions(calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.DAY_OF_WEEK));
    }

    /**
     * Acceso a la suerte que predice nuestro horoscopo de 1 a 10.
     *
     * @return suerte
     */
    public int getLuck() {
        return luck;
    }

    // Método auxiliar que realiza las predicciones con las fechas
    // ya parseadas y descompuestas.
    //
    // Como siempre los MÉTODOS PRIVADOS se prueban indirectamente a través
    // de la API pública de la clase.
    private void makePredictions(int day, int month, int weekDay) {

        if(month >= 6 && month <= 8) {
           // Verano
           if(weekDay == Calendar.SUNDAY) {
               luck = 10;
           } else {
               luck = 8;
           }

        } else if(month == 12 || month <= 2) {
           // Invierto
           if((month==12 && day >= 25) || isJanuaryChristmas(day, month)) {
              // Navidad
              luck = 9;
           } else {
              luck = 6;
           }
        } else {
           luck =1;
        }
    }

    // Metodo auxiliar que decide si es un día de navidad
    // del mes de febrero (no tengo ni idea de porque se
    // ha extraido esta condición de los ifs :)
    private boolean isJanuaryChristmas(int day, int month) {
        return month==1 && day <= 6;
    }
}
