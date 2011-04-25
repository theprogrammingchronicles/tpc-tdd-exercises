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

package com.programmingchronicles.addressbook.support;

import com.programmingchronicles.tdd.addressbook.support.IncrementIdGenerator;
import com.programmingchronicles.tdd.addressbook.TestIdGenerator;
import com.programmingchronicles.tdd.addressbook.IdGenerator;

/**
 * Test que dirigirá la implementación de un IdGenerator basado en
 * en el incremento de un atributo estatico.
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class TestIncrementIdGenerator extends TestIdGenerator {

    @Override
    protected IdGenerator newIdGenerator() {
        return new IncrementIdGenerator();
    }
}
