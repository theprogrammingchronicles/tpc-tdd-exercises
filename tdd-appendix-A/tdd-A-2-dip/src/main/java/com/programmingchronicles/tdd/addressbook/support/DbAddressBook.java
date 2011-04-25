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

import com.programmingchronicles.tdd.addressbook.GlobalAddressBook;
import com.programmingchronicles.tdd.domain.Contact;

/**
 * Implementación incompleta del servicio de agenda global.
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class DbAddressBook implements GlobalAddressBook {

    @Override
    public String addContact(Contact contact) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Contact getContact(String contactId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
