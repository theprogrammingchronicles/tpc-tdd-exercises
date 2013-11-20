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

import com.programmingchronicles.tdd.addressbook.InvalidIdException;
import com.programmingchronicles.tdd.addressbook.GlobalAddressBook;
import com.programmingchronicles.tdd.domain.Contact;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Ejemplo de creación de un nuevo test sobre un método ya existente
 * para poder ampliar su funcionalidad.
 *
 * <p>
 * Ver: {@link #testGetContactInvalidId() }<br/>
 *     Ahora es más importante que nunca hacer fallar los tests antes de
 *     realizar la implementación, ya que se está ampliando la funcionalidad
 *     de métodos ya existentes.
 * </p>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class TestGlobalAddressBook {

    // Instancia del "Object Under Test" que se utilizará en cada test.
    // IMPORTANTE: FIRST: "Independent", cada test usa su propia instancia.
    private GlobalAddressBook addressBook = new GlobalAddressBook();

    /**
     * Test que añade un contacto y se verifica obteniendo el contenido
     * actual de la agenda.
     */
    @Test
    public void testAddContact() {
        Contact contact = new Contact();
        contact.setFirstName("Pedro");

        // Test.
        addressBook.addContact(contact);

        // Verifica que la agenda contiene el contacto que se acaba de añadir.
        List<Contact> contacts = addressBook.getAll();
        assertEquals(1, contacts.size());
        assertEquals("Pedro", contacts.get(0).getFirstName());
    }

    @Test
    public void testGetContact() {
        // Fixture: Creación de datos de prueba e inicialización del entorno
        //          de tests (se necesita una agenda con algún contacto presente).
        Contact expected = new Contact();
        expected.setFirstName("Pedro");
        String expectedId = addressBook.addContact(expected);

        // Ejecución del test
        Contact actual = addressBook.getContact(expectedId);

        // Verificación
        assertEquals(expectedId, actual.getId());
        assertEquals("Pedro", actual.getFirstName());
    }

    /**
     * Nueva funcionalidad para el método {@link GlobalAddressBook#getContact(java.lang.String) }.
     *
     * <p>
     * Se ha decidido que cuando se pida un contacto inexistente o no válido
     * el servicio debe devolver una excepción en lugar de null.</p>
     *
     * <pre>
     * 1. Se codifica el test
     * 2. Se ejecuta y se verifica que falla
     * 3. Se implementa
     * 4. Tests hasta que deja de fallar
     * 5. Refactorizar si es necesario
     * </pre>
     */
    @Test
    public void testGetContactInvalidId() {
        try {
            addressBook.getContact("INVALID");
            fail("testGetContactInvalidId: Expected Exception");
        } catch (InvalidIdException ex) {
            // No es necesario, pero ayuda a la legibilidad de test,
            // vemos el objetivo de este test.
            assertTrue(true);
        }
    }

    /**
     * El mismo ejemplo que {@link #testGetContactInvalidId} pero usando
     * algunas caracteristicas de JUnit 4.
     */
    @Test(expected = InvalidIdException.class)
    public void testGetContactInvalidId2() {
        addressBook.getContact("INVALID");
    }
}
