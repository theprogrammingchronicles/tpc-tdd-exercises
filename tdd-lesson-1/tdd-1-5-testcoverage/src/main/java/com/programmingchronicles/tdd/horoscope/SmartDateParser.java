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

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Parseador de fechas. Acepta los siguientes formatos:<br/>
 * <pre>
 *  - "10/12/2010"
 *  - "10 de diciembre de 2010"
 *  - "10" día del mes y año actual
 * </pre>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class SmartDateParser {
    private static DateFormat shortFormat = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("es"));
    private static DateFormat longFormat = DateFormat.getDateInstance(DateFormat.LONG, new Locale("es"));
    private static Calendar calendar = Calendar.getInstance(new Locale("es"));

    /**
     * Convierte en una fecha el texto entregado según los formatos admitidos.
     *
     * @param text texto con la fecha
     * @return fecha parseada
     * @throws DateParserException fecha inválida
     */
    public Date parse(String text) {
        Date result;

        result = parseShort(text);
        if(result != null) {
           return result;
        } 

        result = parseLong(text);
        if(result != null) {
           return result;
        }

        result = parseCurrentMonthDay(text);
        if(result != null) {
           return result;
        } 
      
        throw new DateParserException();
    }

    private Date parseShort(String text) {
        Date result;
        try {
            result = shortFormat.parse(text);
        } catch (ParseException ex) {
            return null;
        } 
        return result;
    }

    private Date parseLong(String text) {
        Date result;
        try {
            result = longFormat.parse(text);
        } catch (ParseException ex) {
            return null;
        }
        return result;
    }

    private Date parseCurrentMonthDay(String text) {
        calendar.setTime(new Date());
        calendar.clear(Calendar.HOUR);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);

        try {
            calendar.set(Calendar.DATE, Integer.parseInt(text));
        } catch(NumberFormatException ex) {
            return null;
        }
        return calendar.getTime();
    }
}
