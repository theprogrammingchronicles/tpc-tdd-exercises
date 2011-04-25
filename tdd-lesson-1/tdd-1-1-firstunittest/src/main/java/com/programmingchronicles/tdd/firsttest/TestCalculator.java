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
 * TODO:
 * <p>
 * Implementar tests para la clase Calculator sin utilizar JUnit.</p>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com> <pedro@theprogrammingchronicles.com>
 */
public class TestCalculator {

    /**
     * TODO: Implementar el test del método add.
     */
    public void testAdd() {
        // Ejemplo de notificación de fallo usando excepciones.
        throw new AssertionError("testAdd failed");
    }

    /**
     * TODO: Implementar el test del método subtract.
     */
    public void testSubtract() {
        // Ejemplo de notificación de fallo usando asserts nativos
        // de java que requiren la ejecución con asserts activos "java -ea"
        assert false : "testSubtract failed";
    }

    /**
     * Método main para lanzar la batería de tests.
     * <p>
     * Cada test se lanza en una instancia diferente siguiendo el principio
     * <b>"Independent"</b> de las propiedades FIRST.</p>
     *
     * @param args
     */
    public static void main(String[] args) {
        new TestCalculator().testAdd();
        new TestCalculator().testSubtract();
    }
}
