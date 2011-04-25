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
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests de GlobalAddressBook
 *
 * En el "testAddContactRepeatedId" vemos los inconvenientes que presenta
 * el fuerte acoplamiento o la violación de DIP para probar ciertas
 * partes de GlobalAddressBook.
 *
 * ¿Como provocamos la generación de dos ids iguales para testear el
 *  código de GlobalAddressBook que procesa esta condición.
 *
 *     - Podríamos codificar una versión para pruebas con el siguiente código:
 *
 *       <code>private MockIdGenerator idGenerator = new MockIdGenerator()</code>
 *
 *       Pero tendríamos que acordarnos de volverlo a modificar de nuevo
 *       antes de pasar a producción.
 *
 *     - O podríamos codificar la solución aquí mostrada, que presenta
 *       grandes deficiencias.
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class TestGlobalAddressBook {

    GlobalAddressBook addressBook = new GlobalAddressBook();

    /**
     * Necesitamos un test que verifique si el addContact se comporta
     * como lo hemos diseñado al recibir ids repetidos.
     */
    @Test(expected = InvalidIdException.class)
    public void testAddContactRepeatedId() {

        // Es una clase difícil de testear no tenemos control,
        // para provocar la generación de un id repetido.

        // Para no modificar la clase GlobalAddressBook, lo que se nos
        // ocurre es añadir un número elevado de contactos hasta que por
        // casualidad se produzca un id repetido.

        // MUY MALA SOLUCIÓN

        // Tenemos un tiempo de ejecución bastante indeterminado, que podría ser
        // muy alto. Nada nos asegura que el test produzca repetidos en todas
        // las ocasiones. Tampoco nos asegura que cuando el test es ok se debe solo
        // a ids repetidos, o que no se ha saltado algún repetido.
        for(int i=0 ; i<32000; i++) {
            Contact contact = new Contact();
            addressBook.addContact(contact);
        }
    }



    @Test
    public void testAddContact() {
        // Creación de datos de prueba: "Test Object"
        // Diseño: Decidimos que una clase Contact tendrá los datos de los contactos.
        Contact contact = new Contact();

        // Ejecución del Test
        String contactId = addressBook.addContact(contact);

        // Se verifica el addContact. Por ahora lo unico que se puede es
        // asegurar que no lanza excepciones y que devuelve un id válido.
        assertNotNull(contactId);
        assertTrue(contactId.trim().length() > 0);
    }

    @Test
    public void testGetContact() {
        // Creación de datos de prueba: "Test Object" e inicialización
        // del entorno de tests: Fixtures.
        Contact expected = new Contact();
        String expectedId = addressBook.addContact(expected);

        // Ejecución del test
        Contact actual = addressBook.getContact(expectedId);

        // Verificación
        // ¿Como probamos que el contacto recibido es el mismo que se ha añadido?
        assertEquals(expectedId, actual.getId());
    }

    @Test(expected = InvalidIdException.class)
    public void testGetContactInvalidId() {
        addressBook.getContact("INVALID");
    }
}
