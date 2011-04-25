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
import com.programmingchronicles.tdd.addressbook.InvalidIdException;
import com.programmingchronicles.tdd.domain.Contact;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Introdución al uso de mocks y/o stubs para programación de
 * tests unitarios aislados.
 *
 * <p>
 * <b>Refactorización:</b><br/>
 *    Por un lado se decide que la generación de ids es una responsabilidad
 *    que no pertenece a GlobalAddressBook.<br/>
 *    Por otro lado extrayendo la funcionalidad se permite su reutilización.
 * </p>
 *
 * <p>
 * <b>Mocks/Stubs:</b><br/>
 *    Suponemos que la nueva funcionalidad va a ser implementada por
 *    otro desarrollador.<br/>
 *    Podemos esperar a que la tenga acabada, pero para ver un ejemplo de
 *    stubs/mocks vamos a refactorizar sin disponer de la implementación.
 * </p>
 *
 * <p>
 * <b>TO DO:</b><br/>
 *    Modificar los tests para que puedan funcionar sin disponer aun
 *    de la clasa IdGenerator.<br/>
 *    ¿Como se puede hacer que GlobalAddressBook utilice durante los tests un
 *     Mock de IdGenerator sin incluir el Mock en la implementación del código
 *     funcional?
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

    /**
     * Para verificar el método de obtener un sólo contacto se debe
     * hacer uso del id generado al añadirlo.
     */
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
