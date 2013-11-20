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

package com.programmingchronicles.tdd.addressbook.support;

import com.programmingchronicles.tdd.addressbook.IdGenerator;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Test que dirigirá la implementación de un IdGenerator basado en
 * en el incremento de un atributo estatico.
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class TestIncrementIdGenerator {

    @Test
    public void testNewId() {
        IdGenerator idGenerator = new IncrementIdGenerator();

        String oldId = idGenerator.newId();
        // Se prueba que siempre se genera un id nuevo
        // ¿cuantas veces se prueba?
        for(int i=0; i < 10; i++) {
            String newId = idGenerator.newId();
            assertFalse(oldId.equals(newId));
            oldId = newId;
        }
    }

    @Test
    public void testNewIdDifferentInstances() {
        // Test de caja blanca:
        //   Estamos diseñando un generador basado en el incremento de una
        //   variable de clase.
        //   La especificación indicará que esta clase siempre genera una
        //   id diferente aunque se utilicen diferentes instancias.
        String oldId = new IncrementIdGenerator().newId();

        // Se prueba que siempre se genera un id nuevo
        // ¿cuantas veces se prueba?
        for(int i=0; i < 10; i++) {
            String newId = new IncrementIdGenerator().newId();
            assertFalse(oldId.equals(newId));
            oldId = newId;
        }
    }
}
