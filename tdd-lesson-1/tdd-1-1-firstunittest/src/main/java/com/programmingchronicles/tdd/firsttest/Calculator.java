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

package com.programmingchronicles.tdd.firsttest;

/**
 * Implementación de una calculadora con operaciones sencillas.
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com> <pedro@theprogrammingchronicles.com>
 */
public class Calculator {

    /**
     * Suma de dos números.<br/>
     *
     * <p>
     * La implementación es erronea para observar los fallos de los tests.</p>
     *
     * @param number1
     * @param number2
     * @return
     */
    public int add(int number1, int number2) {
        return number1 - number2;        
    }

    /**
     * Resta de dos números.<br/>
     *
     * <p>
     * La implementación es erronea para observar los fallos de los tests.</p>
     *
     * @param number1
     * @param number2
     * @return
     */
    public int subtract(int number1, int number2) {
        return number1 + number2;
    }
}
