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

import com.programmingchronicles.tdd.addressbook.support.UUIDIdGenerator;
import com.programmingchronicles.tdd.addressbook.IdGenerator;
import com.programmingchronicles.tdd.addressbook.TestIdGenerator;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * Test que dirige la implementación de un IdGenerator basado en
 * la generación de UUIDs.
 *
 * <p>
 * <b>Refactorizado:</b><br/>
 *   Hereda los tests genericos de IdGenerator y además puede
 *   añadir nuevos tests.
 * </p>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class TestUUIDIdGenerator extends TestIdGenerator {

    @Override
    protected IdGenerator newIdGenerator() {
        return new UUIDIdGenerator();
    }

    /**
     * En esta clase se pueden definir tests específicos de esta implementación,
     * y que no son genericos de la interfaz.
     */
    @Test
    public void testNewIdRealUUID() {
        fail("not implemented");
    }
}
