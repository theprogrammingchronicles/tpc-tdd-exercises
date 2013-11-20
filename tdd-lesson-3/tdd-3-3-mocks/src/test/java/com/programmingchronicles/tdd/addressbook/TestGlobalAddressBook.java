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

import com.programmingchronicles.tdd.domain.Contact;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Introdución al aislamiento de tests unitarios mediante tecnicas
 * de mocks y/o stubs.
 *
 * <p>
 *  Se modifican los tests para que puedan funcionar con un stubs, permitiendo
 *  aislar el Object Under Test de sus dependencias. Lo que permite crear
 *  un test unitarios completamente aislados.
 * </p>
 *
 * <p>
 *  Ejemplo de programación de respuestas en un stub para permitir la
 *  ejecución del código bajo test (validación por estado).
 * </p>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class TestGlobalAddressBook {

    private GlobalAddressBook addressBook = new GlobalAddressBook();

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
        // Fixture:
        // Creación de Test Objects
        Contact contact = new Contact();
        contact.setFirstName("Pedro");

        // Configura el addressBook con un stub programado para devolver
        // siempre el mismo id.
        addressBook.setIdGenerator(new GeneratorIdStub("newId"));

        // Test.
        addressBook.addContact(contact);

        // Verifica que la agenda contiene el contacto que se acaba de añadir.
        List<Contact> contacts = addressBook.getAll();
        assertEquals(1, contacts.size());
        assertEquals("Pedro", contacts.get(0).getFirstName());
    }

  
    @Test
    public void testGetContact() {
        // Fixture:
        // Creación de Test Objects y configuración del Stub
        Contact expected = new Contact();
        expected.setFirstName("Pedro");
        // Configura el addressBook con un stub programado para devolver
        // siempre el mismo id.
        addressBook.setIdGenerator(new GeneratorIdStub("newId"));
        // Inicialización de la agenda con los datos de prueba.
        String expectedId = addressBook.addContact(expected);

        // Ejecución del test
        Contact actual = addressBook.getContact(expectedId);

        // Verificación
        assertEquals(expectedId, actual.getId());
        assertEquals("Pedro", actual.getFirstName());
    }

    @Test
    public void testGetContactInvalidId() {
        // Configura el addressBook con un stub programado para devolver
        // siempre el mismo id.
        addressBook.setIdGenerator(new GeneratorIdStub("newId"));

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
