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

package com.programmingchronicles.tdd.testing.web;

import com.programmingchronicles.tdd.domain.Contact;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.junit.matchers.TypeSafeMatcher;

/**
 * Implementación de un custom matcher de contact que permite realizar
 * comparaciones ignorando o no el id.
 *
 * <p>El matcher se puede usar con el <b>assertThat</b> de JUnit o para el
 * argument matching de los mocks de mockito <b>argThat</b></p>
 *
 * <p>Aunque se podría hacer lo mismo implementando el método <i>Contact#equals</i>,
 * de esta forma podemos hacer comparaciones no ligadas a la lógica, como por
 * ejemplo ignorar el id en las comparaciones.</p>
 *
 * <p>Permite reducir el código de los tests, sobre todo cuando hay que
 * verificar listas y colecciones.</p>
 */
public class IsContactEqual extends TypeSafeMatcher<Contact> {
    private final Contact c2;
    private final boolean ignoreId;

    public IsContactEqual(Contact contact, boolean ignoreId) {
        this.c2 = contact;
        this.ignoreId = ignoreId;
    }

    public IsContactEqual(Contact contact) {
        this.c2 = contact;
        this.ignoreId = false;
    }

    /**
     * Se sobrescribe para definir cuando dos contactos son el mismo.
     *
     * @param c1
     * @return
     */
    @Override
    public boolean matchesSafely(Contact c1) {
        boolean result;
        result = equals(c1.getFirstName(), c2.getFirstName())  &&
                 equals(c1.getSurname(), c2.getSurname()) &&
                 equals(c1.getBirthday(), c2.getBirthday()) &&
                 equals(c1.getPhone(), c2.getPhone());

        if(!ignoreId) {
            result = result && equals(c1.getId(), c2.getId());
        }
        return result;
    }

    /**
     * Los hamcrest matchers dan información muy precisa de la causa del
     * fallo. Sobrescribiendo este método se puede mostrar el expected
     * object de forma detallada.
     *
     * <p>
     * Aunque se debería implementar el toString del Contact para mostrar
     * información completa.</p>
     *
     * @param description
     */
    @Override
    public void describeTo(Description description) {
        description.appendText("a contact with ")
                   .appendValueList("[", ", ", "]",
                          c2.getId(), c2.getFirstName(), c2.getSurname(),
                          c2.getBirthday(), c2.getPhone());
    }

    /**
     * Matcher que compara dos contactos completos.
     *
     * @param contact
     * @return
     */
    @Factory
    public static Matcher<Contact> contactEq(Contact contact) {
        return new IsContactEqual(contact);
    }

    /**
     * Matcher que compara dos contactos ignorando los ids.
     *
     * @param contact
     * @return
     */
    @Factory
    public static Matcher<Contact> contactNoIdEq(Contact contact) {
        return new IsContactEqual(contact, true);
    }

    private static boolean equals(Object o1, Object o2) {
        return o1 == o2 || (o1 != null && o1.equals(o2));
    }
}