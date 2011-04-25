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
import com.programmingchronicles.tdd.addressbook.IdGenerator;
import com.programmingchronicles.tdd.domain.Contact;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Se aplica un refactoring a los tests para hacerlos más mantenibles y
 * legibles eliminado el código duplicado de los fixtures.
 *
 * <p>JUnit crea una instancia de TestGlobalAddressBook por cada uno
 * de los tests.</p>
 *
 * <p>Se crean todas las instancias antes de ejecutar un solo test.</p>
 *
 * <p>Antes de ejecutar cada test se ejecuta el método anotado como @Before
 * que permite inicialización común del fixture.</p>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class TestGlobalAddressBook {

    private GlobalAddressBook addressBook;
    private Contact expectedContact;

    /**
     * Inicialización del fixture de cada test ejecutado sobre una nueva
     * instancia de la clase.
     */
    @Before
    public void setUp() {
        // Principio de Independencia:
        //   Cada test tiene su propia instancia del Object Under Test y del Stub.
        addressBook = new GlobalAddressBook();
        addressBook.setIdGenerator(new GeneratorIdStub());

        expectedContact = new Contact();
    }

    /**
     * Implementación falsa de la interfaz IdGenerator.
     */
    protected static class GeneratorIdStub implements IdGenerator {
        private String nextId;

        /**
         * Construye un stub con una respuesta por defecto.
         */
        public GeneratorIdStub() {
            this.nextId = "defaultId";
        }

        /**
         * El stub se puede construir configurando la respuesta esperada
         * que nos permite dirigir los tests.
         *
         * @param nextId
         */
        public GeneratorIdStub(String nextId) {
            this.nextId = nextId;
        }

        public void setNextId(String nextId) {
            this.nextId = nextId;
        }

        @Override
        public String newId() {
            return nextId;
        }
    }

    /**
     * Test que añade un contacto y se verifica obteniendo el contenido
     * actual de la agenda.
     */
    @Test
    public void testAddContact() {
        expectedContact.setFirstName("Pedro");

        // Test.
        addressBook.addContact(expectedContact);

        // Verifica que la agenda contiene el contacto que se acaba de añadir.
        List<Contact> contacts = addressBook.getAll();
        assertEquals(1, contacts.size());
        assertEquals("Pedro", contacts.get(0).getFirstName());
    }

    /**
     * Para verificar el método de obtener un sólo contacto se debe
     * hacer uso del id generado al añadirlo.
     */
    @Test
    public void testGetContact() {
        expectedContact.setFirstName("Pedro");
        // Inicialización de la agenda con los datos de prueba.
        String expectedId = addressBook.addContact(expectedContact);

        // Ejecución del test
        Contact actual = addressBook.getContact(expectedId);

        // Verificación
        assertEquals(expectedId, actual.getId());
        assertEquals("Pedro", actual.getFirstName());
    }

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
}
