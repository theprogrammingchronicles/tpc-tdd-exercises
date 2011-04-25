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
 * Tests para la clase Calculator sin unitilización de JUnit.
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class TestCalculator {

    public void testAdd() {
        Calculator calculator = new Calculator();
        int result = calculator.add(50, 20);

        // Ejemplo de notificación de fallo usando excepciones.
        if(result != 70) {
           System.out.println("testAdd failed");
           throw new AssertionError("testAdd failed");
        }
    }

    public void testSubtract() {
        Calculator calculator = new Calculator();
        int result = calculator.subtract(50, 20);

        // Ejemplo de notificación de fallo usando asserts nativos
        // de java que requiren la ejecución con asserts activos "java -ea"
        assert result == 30 : "testSubtract failed";
    }

    /**
     * Método main para lanzar la batería de tests.
     * <p>
     * Cada test se lanza en una instancia diferente siguiendo el principio
     * <b>"Independent"</b> de las propiedades FIRST.</p>
     *
     * @param args
     */
    public static void main(String [] args) {       
       new TestCalculator().testAdd();
       new TestCalculator().testSubtract();
    }
}
