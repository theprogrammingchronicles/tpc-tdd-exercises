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

import com.programmingchronicles.tdd.addressbook.GlobalAddressBook;
import com.programmingchronicles.tdd.domain.Contact;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Resultado despues de iterar varias veces el bucle TDD.
 *
 * <p>Se obtiene una primera aproximación que posiblemente se tenga
 * que refinar refactorizando.</p>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class TestGlobalAddressBook {

    /**
     * Test que añade un contacto y se verifica obteniendo el contenido
     * actual de la agenda.
     */
    @Test
    public void testAddContact() {
        // Creación del "Object Under Test"
        GlobalAddressBook addressBook = new GlobalAddressBook();

        // Fixture: Creación de datos de prueba o "Test Objects"
        // Diseño: Decidimos que una clase Contact tendrá los datos de los contactos.
        Contact contact = new Contact();
        contact.setFirstName("Pedro");

        // Test.
        addressBook.addContact(contact);

        // Verifica que la agenda contiene el contacto que se acaba de añadir.
        // Pregunta: ¿Es correcto usar métodos del Object Under Test para
        //            realizar la verificación?.
        List<Contact> contacts = addressBook.getAll();
        assertEquals(1, contacts.size());
        assertEquals("Pedro", contacts.get(0).getFirstName());
    }

    /**
     * Durante la implementación de la funcionalidad se detecta que es
     * necesario que el método de añadir devuelva identificadores.
     */
    @Test
    public void testAddContactVerifyId() {
        // Creación del "Object Under Test"
        GlobalAddressBook addressBook = new GlobalAddressBook();

        // Fixture
        Contact contact = new Contact();

        // Ejecución del Test
        String contactId = addressBook.addContact(contact);

        // Por ahora lo unico que se puede es asegurar que no lanza
        // excepciones y que devuelve un id válido.
        assertNotNull(contactId);
        assertTrue(contactId.trim().length() > 0);
    }

    /**
     * Para verificar el método de obtener un sólo contacto se debe
     * hacer uso del id generado al añadirlo. Este test por tanto
     * mejora el test anterior (que ya puede ser eliminado).
     */
    @Test
    public void testGetContact() {
        // Creación del "Object Under Test"
        GlobalAddressBook addressBook = new GlobalAddressBook();

        // Fixture: Creación de datos de prueba e inicialización del entorno
        //          de tests (se necesita una agenda con algún contacto presente).
        //
        // Pregunta: ¿Es correcto usar métodos del "Object Under Test" para
        //            inicializar el entorno de la prueba?
        Contact expected = new Contact();
        expected.setFirstName("Pedro");
        String expectedId = addressBook.addContact(expected);

        // Ejecución del test
        Contact actual = addressBook.getContact(expectedId);

        // Verificación    
        assertEquals(expectedId, actual.getId());
        assertEquals("Pedro", actual.getFirstName());
    }
}