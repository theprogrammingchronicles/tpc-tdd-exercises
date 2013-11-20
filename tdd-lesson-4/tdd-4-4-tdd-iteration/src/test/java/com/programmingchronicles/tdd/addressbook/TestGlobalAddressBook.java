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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test de GlobalAddressBook.
 *
 * <p>
 * Ejemplo en {@link #testAddDuplicateSurname() }.</p>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class TestGlobalAddressBook {

    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private Contact expectedContact;
    private GlobalAddressBook addressBook;
    private IdGenerator generatorMock;

    @Before
    public void setUp() {
        expectedContact = new Contact();
        addressBook = new GlobalAddressBook();

        // Se crea el mock automaticamente usando mockito
        generatorMock = mock(IdGenerator.class);

        // Se programa una respuesta por defecto para nextId. Se configuran
        // varias respuestas ya que algunos tests necesitan más de un contacto.
        // Cada llamada devolverá la siguiente respuesta.
        when(generatorMock.newId()).thenReturn("0", "1", "2", "3", "4");

        addressBook.setIdGenerator(generatorMock);
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

    @Test
    public void testAddFullContact() throws ParseException {
        expectedContact.setFirstName("Pedro");
        expectedContact.setSurname("Ballesteros");
        Date actualBirthday = dateFormat.parse("8/1/1974");
        expectedContact.setBirthday(actualBirthday);
        expectedContact.setPhone("610101010");
        addressBook.addContact(expectedContact);

        List<Contact> contacts = addressBook.getAll();

        assertEquals(1, contacts.size());
        assertEquals("Pedro", contacts.get(0).getFirstName());
        assertEquals("Ballesteros", contacts.get(0).getSurname());
        assertEquals(actualBirthday, contacts.get(0).getBirthday());
        assertEquals("610101010", contacts.get(0).getPhone());
    }

    @Test(expected=InvalidContactException.class)
    public void testAddWithoutFirstName() {
        expectedContact.setSurname("Ballesteros");
        addressBook.addContact(expectedContact);
    }

    @Test(expected=InvalidContactException.class)
    public void testAddBlankFirstName() {
        expectedContact.setFirstName("   ");
        expectedContact.setSurname("Ballesteros");
        addressBook.addContact(expectedContact);
    }

    @Test(expected=InvalidContactException.class)
    public void testAddEmptyFirstName() {
        expectedContact.setFirstName("");
        expectedContact.setSurname("Ballesteros");
        addressBook.addContact(expectedContact);
    }

    @Test
    public void testAddDuplicateFirstName() {
        Contact c1 = new Contact();
        c1.setFirstName("Pedro");

        Contact c2 = new Contact();
        c2.setFirstName("Pedro");

        addressBook.addContact(c1);
        try {
            addressBook.addContact(c2);
            fail("Expected InvalidContactException");
        } catch(InvalidContactException ex) {
            List<Contact> contacts = addressBook.getAll();
            assertEquals(1, contacts.size());
            assertEquals("Pedro", contacts.get(0).getFirstName());
        }
    }

    @Test
    public void testAddDuplicateNameWithCapital() {
        Contact c1 = new Contact();
        c1.setFirstName("Pedro");

        Contact c2 = new Contact();
        c2.setFirstName("PEDRO");

        addressBook.addContact(c1);
        try {
            addressBook.addContact(c2);
            fail("Expected InvalidContactException");
        } catch(InvalidContactException ex) {
            List<Contact> contacts = addressBook.getAll();
            assertEquals(1, contacts.size());
            assertEquals("Pedro", contacts.get(0).getFirstName());
        }
    }

    @Test
    public void testAddDuplicateNameWithBlanks() {
        Contact c1 = new Contact();
        c1.setFirstName(" Pedro "); // Un espacio

        Contact c2 = new Contact();
        c2.setFirstName("    PEDRO    "); // Mas de un espacio

        addressBook.addContact(c1);
        try {
            addressBook.addContact(c2);
            fail("Expected InvalidContactException");
        } catch(InvalidContactException ex) {
            List<Contact> contacts = addressBook.getAll();
            assertEquals(1, contacts.size());

            // Decidimos que mejor almacenar los nombres sin espacios
            // y con esto también queda testeado.
            assertEquals("Pedro", contacts.get(0).getFirstName());
        }
    }

    /**
     * Se implementa el test que valida que un contacto con el mismo
     * nombre y el mismo apellido debe provocar un error.
     * <p/>
     *
     * Se observa que no se trata de un buen test unitario ya que no
     * podemos hacerlo fallar. Los test anteriores impiden que se pueda
     * añadir un contacto con el mismo nombre sin validar el apellido.
     *
     * <p/>
     * Para codificar esta funcionalidad se necesita por tanto añadir
     * el test opuesto, que un contacto con el mismo nombre y diferente
     * apellido si se puede añadir.
     */
    @Test
    public void testAddDuplicateSurname() {
        Contact c1 = new Contact();
        c1.setFirstName("Pedro");
        c1.setSurname("Ballesteros");

        Contact c2 = new Contact();
        c2.setFirstName("Pedro");
        c2.setSurname("Ballesteros");

        addressBook.addContact(c1);
        try {
            addressBook.addContact(c2);
            fail("Expected InvalidContactException");

        } catch(InvalidContactException ex) {
            List<Contact> contacts = addressBook.getAll();
            assertEquals(1, contacts.size());
            assertEquals("Pedro", contacts.get(0).getFirstName());
            assertEquals("Ballesteros", contacts.get(0).getSurname());
        }
    }

    /**
     * Test para validar que un contacto con el mismo nombre pero
     * diferente apellido si se puede añadir.
     * <p/>
     *
     * El test anterior no se puede hacer fallar por lo que no ayuda
     * a la codificación de la funcionalidad, no dirigue el desarrollo.
     *
     * <p/>
     * TODO: se debe completar este test, hacer fallar la funcionalidad
     * y completar el codigo que falta en el "addContact" para hacer
     * funcionar este test.
     */
    @Test
    public void testAddDuplicateNameDifferentSurname() {
        // Fixture
        Contact c1 = new Contact();
        c1.setFirstName("Pedro");
        c1.setSurname("Herranz");

        Contact c2 = new Contact();
        c2.setFirstName("Pedro");
        c2.setSurname("Ballesteros");

        String actualId1 = addressBook.addContact(c1);

        // Test
        String actualId2 = addressBook.addContact(c2);

        // Verify
        // fail("TODO");
    }
}
