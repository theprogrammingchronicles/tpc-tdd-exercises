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
 * Test que dirige la implementación de un IdGenerator basado en
 * la generación de UUIDs.
 *
 * <p/>
 * <pre>
 * ¿Se debería refactorizar esta clase de tests?
 *
 *   Las implementaciones de TestIncrementalIdGenerator y TestUUIDIdGenerator
 *   son exactamente iguales.
 *
 *   ¿No sería bueno crear una unica clase de test que pruebe la interface
 *    IdGenerator con cualquier implementación?.
 * </pre>
 *
 * <p>
 * Se debe tener en cuenta que cada implementación podría requerir tests
 * diferentes, aunque sean la misma interface.</p>
 *
 * <pre>
 *  - Un ejemplo son tests de caja de blanca que deben forzar la ejecución de
 *    todas las lineas de código de la implementación.
 *
 *  - Otro ejemplo es si se convierte en requisito que UUIDIdGenerator genere
 *    UUIDs válidos. Se deberían crear tests especificos de esta implementación
 *    para comprobar que los id generados son realmente UUIDs.
 * </pre>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class TestUUIDIdGenerator {

    @Test
    public void testNewId() {
        IdGenerator idGenerator = new UUIDIdGenerator();
        String oldId = idGenerator.newId();

        for (int i = 0; i < 10; i++) {
            String newId = idGenerator.newId();
            assertFalse(oldId.equals(newId));
            oldId = newId;
        }
    }

    @Test
    public void testNewIdDifferentInstances() {
        String oldId = new UUIDIdGenerator().newId();

        for (int i = 0; i < 10; i++) {
            String newId = new UUIDIdGenerator().newId();
            assertFalse(oldId.equals(newId));
            oldId = newId;
        }
    }
}
