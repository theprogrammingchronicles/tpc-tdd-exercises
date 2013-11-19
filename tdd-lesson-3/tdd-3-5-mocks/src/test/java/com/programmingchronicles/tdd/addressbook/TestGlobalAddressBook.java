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
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Verificación por interacción. Ejemplo de programacion manual de
 * un mock que permite verificar la colaboración entre dependencias
 * mediante validación por interacción.
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class TestGlobalAddressBook {

    private GlobalAddressBook addressBook;
    private GeneratorIdMock generatorMock;
    private Contact expectedContact;

    @Before
    public void setUp() {
        // Principio de Independencia:
        //   Cada test tiene su propia instancia del Object Under Test y del Stub.
        addressBook = new GlobalAddressBook();
        generatorMock = new GeneratorIdMock();
        addressBook.setIdGenerator(generatorMock);

        expectedContact = new Contact();
    }

    /**
     * Implementación falsa de la interfaz IdGenerator, que permite
     * verificar las llamadas al método newId (mock).
     *
     * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
     */
    protected static class GeneratorIdMock implements IdGenerator {
        private String nextId;

        // Contador que pemite verificar el número de llamadas.
        private int timesCalledNextId = 0;

        /**
         * Verifica si nextId se ha llamado el número de veces indicado.
         *
         * @param times
         */
        public boolean verifyNextId(int times) {
            return timesCalledNextId == times;
        }

        /**
         * Crea un mock con una respuesta predeterminada.
         */
        public GeneratorIdMock() {
            this.nextId = "defaultId";
        }

        /**
         * El mock se puede construir configurando la respuesta esperada
         * que nos permite dirigir los tests.
         *
         * @param nextId
         */
        public GeneratorIdMock(String nextId) {
            this.nextId = nextId;
        }

        public void setNextId(String nextId) {
            this.nextId = nextId;
        }

        @Override
        public String newId() {
            timesCalledNextId++;
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

        // Validación por interacción de que el id se está generando
        // el id invocando exactamente una vez la interfaz IdGenerator.
        assertTrue(generatorMock.verifyNextId(1));
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

        // Validación por interacción de que el id se está generando
        // el id invocando exactamente una vez la interfaz IdGenerator.
        assertTrue(generatorMock.verifyNextId(1));
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
