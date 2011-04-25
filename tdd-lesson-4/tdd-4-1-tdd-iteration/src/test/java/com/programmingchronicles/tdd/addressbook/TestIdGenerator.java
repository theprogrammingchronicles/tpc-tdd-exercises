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

package com.programmingchronicles.tdd.addressbook;

import com.programmingchronicles.tdd.addressbook.IdGenerator;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Clase Abstracta para test de implementaciones de IdGenerator.
 *
 * <p>
 * <b>Refactorización:</b><br/>
 *   Clase base con tests que ejecutan las pruebas comunes de cualquier
 *   implementación de IdGenerator.</p>
 *
 * <p>
 * Como es una clase abstracta no se lanzará ninguno de sus tests, pero
 * las clases derivadas heredarán estos tests y se lanzarán todos para cada una.
 * </p>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public abstract class TestIdGenerator {

    /**
     * Se debe sobreescribir para obtener la implementación de IdGenerator
     * que se quiere testear.
     *
     * @return Implementación de IdGenerator
     */
    protected abstract IdGenerator newIdGenerator();

    @Test
    public void testNewId() {
        IdGenerator idGenerator = newIdGenerator();

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
        // La API IdGenerator especifica que todas las instancias
        // deben devolver ids diferentes.
        String oldId = newIdGenerator().newId();

        // Se prueba que siempre se genera un id nuevo
        // ¿cuantas veces se prueba?
        for(int i=0; i < 10; i++) {
            String newId = newIdGenerator().newId();
            assertFalse(oldId.equals(newId));
            oldId = newId;
        }
    }
}
