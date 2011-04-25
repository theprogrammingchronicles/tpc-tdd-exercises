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
import com.programmingchronicles.tdd.addressbook.IdGenerator;
import com.programmingchronicles.tdd.addressbook.InvalidIdException;
import com.programmingchronicles.tdd.domain.Contact;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test de GlobalAddressBook.
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class TestGlobalAddressBook {

    private GlobalAddressBook addressBook;
    private IdGenerator generatorMock;
    private Contact expectedContact;

    @Before
    public void setUp() {
        expectedContact = new Contact();
        // Principio de Independencia:
        //   Cada test tiene su propia instancia del Object Under Test y del Stub.
        addressBook = new GlobalAddressBook();

        // Se crea el mock automaticamente usando mockito
        generatorMock = mock(IdGenerator.class);

        // Se programan varias respuestas por defecto para nextId.
        when(generatorMock.newId()).thenReturn("1","2","3","4");

        // Configura el addressBook con el mock generado.
        addressBook.setIdGenerator(generatorMock);
    }

    /**
     * Test que añade un contacto y se verifica obteniendo el contenido
     * actual de la agenda.
     */
    @Test
    public void testAddContact() {
        expectedContact.setFirstName("Pedro");

        // Se puede sobreescribir la programación del stub con una
        // nueva respuesta si el test lo necesita.
        when(generatorMock.newId()).thenReturn("newId");

        // Test.
        addressBook.addContact(expectedContact);

        // Verifica que la agenda contiene el contacto que se acaba de añadir.
        List<Contact> contacts = addressBook.getAll();
        assertEquals(1, contacts.size());
        assertEquals("Pedro", contacts.get(0).getFirstName());

        // También se puede hacer una validación de interacción, comprobando si
        // realmente se ha llamado a la interfaz IdGenerator.
        // En este caso no sería necesaria esta validación, e incluso sería
        // conveniente no realizarla. Aunque garantiza que el id se obtiene
        // mediante el IdGenerator, hace el test más dependiente de la
        // implementación.
        verify(generatorMock, times(1)).newId();
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
        verify(generatorMock, times(1)).newId();
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