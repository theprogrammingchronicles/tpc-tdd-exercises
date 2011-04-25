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

package com.programmingchronicles.tdd.addressbook.support;

import com.programmingchronicles.tdd.addressbook.support.MemAddressBook;
import com.programmingchronicles.tdd.addressbook.GlobalAddressBook;
import com.programmingchronicles.tdd.addressbook.IdGenerator;
import com.programmingchronicles.tdd.addressbook.InvalidContactException;
import com.programmingchronicles.tdd.addressbook.InvalidIdException;
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
 * Test de MemAddressBook.
 *
 * <p>
 * <b>Refactorizacion:</b> Legibilidad<br/>
 *  Es un test específico de una implementación no de una API generica. Por eso
 *  el nombre de test debe hacer referencia a la implementación que se está
 *  testeando, y se ha movido al mismo paquete que el Object Under Test. </p>
 *
 *  <p>
 *  Si todos los tests implementados son de caja negra, se podrían dejar como
 *  tests de cualquier implementación de GlobalAddressBook. Pero no adelantamos
 *  cosas, cuando aparezcan otras implementaciones decidimos refactorizar como
 *  se hizo para IdGenerator.</p>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class TestMemAddressBook {

    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Contact expectedContact;
    private GlobalAddressBook addressBook;
    private IdGenerator generatorMock;

    @Before
    public void setUp() {
        expectedContact = new Contact();
        addressBook = new MemAddressBook();

        // Se crea el mock automaticamente usando mockito
        generatorMock = mock(IdGenerator.class);

        // Se programa una respuesta por defecto para nextId. Se configuran
        // varias respuestas ya que algunos tests necesitan más de un contacto.
        // Cada llamada devolverá la siguiente respuesta.
        when(generatorMock.newId()).thenReturn("0", "1", "2", "3", "4");

        ((MemAddressBook)addressBook).setIdGenerator(generatorMock);
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

    @Test(expected = InvalidContactException.class)
    public void testAddWithoutFirstName() {
        expectedContact.setSurname("Ballesteros");
        addressBook.addContact(expectedContact);
    }

    @Test(expected = InvalidContactException.class)
    public void testAddBlankFirstName() {
        expectedContact.setFirstName("   ");
        expectedContact.setSurname("Ballesteros");
        addressBook.addContact(expectedContact);
    }

    @Test(expected = InvalidContactException.class)
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
        } catch (InvalidContactException ex) {
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
        } catch (InvalidContactException ex) {
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
        } catch (InvalidContactException ex) {
            List<Contact> contacts = addressBook.getAll();
            assertEquals(1, contacts.size());

            // Decidimos que mejor almacenar los nombres sin espacios
            // y con esto también queda testeado.
            assertEquals("Pedro", contacts.get(0).getFirstName());
        }
    }

    /**
     * La implementación de esta funcionalidad nos muestra mas casos de prueba
     * que no se vieron durante la elaboración de los tests de aceptación.
     * <p/>
     *
     * <pre>
     * Dos indicadores que nos dicen que habría que implementar un test
     * en el que se entrega null en el segundo surname para detectar duplicados.
     *
     *  - Mientras implementamos la funcionalidad nos damos cuenta que sería
     *    conveniente comprobar si surname se entrega a null.
     *    Por tanto deberíamos parar y codificar ese test primero.
     *
     *  - No obstante los tests unitarios existentes ya empiezan a fallar
     *    con excepciones NullPointerException, y esto también nos muestra
     *    que fallaba este caso de test.
     * </pre>
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
        } catch (InvalidContactException ex) {
            List<Contact> contacts = addressBook.getAll();
            assertEquals(1, contacts.size());
            assertEquals("Pedro", contacts.get(0).getFirstName());
            assertEquals("Ballesteros", contacts.get(0).getSurname());
        }
    }

    /**
     * Este test lo hemos descubierto mientras desarrollabamos la funcionalidad
     * que exigia el test anterior "testAddDuplicateSurname".
     */
    @Test
    public void testAddDuplicateNameWithNullSurname() {
        Contact c1 = new Contact();
        c1.setFirstName("Pedro");
        c1.setSurname("Ballesteros");

        Contact c2 = new Contact();
        c2.setFirstName("Pedro");
        c2.setSurname(null);

        String actualId1 = addressBook.addContact(c1);
        String actualId2 = addressBook.addContact(c2);

        // Se comprueba que se han añadido los dos.
        // NOTA: Esto ya lanza un error, el mock que tenemos para crear ids no
        //       nos sirve, hay que cambiarlo ya que siempre genera el mismo.
        // Ahora el mock se inicializa así:
        //       when(generatorMock.newId()).thenReturn("0", "1", "2", "3", "4");
        List<Contact> contacts = addressBook.getAll();
        assertEquals(2, contacts.size());

        // Se verifican los contactos recibidos
          //  assertEquals("Pedro", contacts.get(1).getFirstName());
          //  assertEquals("Ballesteros", contacts.get(1).getSurname());
          //  assertEquals("Pedro", contacts.get(0).getFirstName());
          //  assertNull(contacts.get(0).getSurname());

        // ANTIPATRON:
        //  Con los asserts anteriores el test se hace fragil, se está fijando
        //  un orden en la respuesta.
        //  Ningun requisito indica que getAll debe devolver algún orden
        //  determinado. Y deberiamos hacer el test independiente de que
        //  como se implemente la construcción de la lista de respuesta.

        // En este caso particular se está implementando con un HashMap, por lo
        // que el orden incluso será muy indeterminado.

        // En este caso el assertEquals(2, contacts.size()) podría ser más que
        // suficiente ya el método addContact ya ha probado que añade bien
        // todos los campos en tests anteriores.

        // Pero si nos queremos asegurarnos hay dos opciones:
        //
        //   - Implementar el assert usando el método getContact para
        //     buscar el contacto que queremos verificar.
        //
        //   - O implementar una busqueda del contacto dentro del
        //     List recibido.

        // Usamos la primera opción, el getContact ya está testeado.
        Contact expectedC1 = addressBook.getContact(actualId1);
        assertEquals("Pedro", expectedC1.getFirstName());
        assertEquals("Ballesteros", expectedC1.getSurname());

        Contact expectedC2 = addressBook.getContact(actualId2);
        assertEquals("Pedro", expectedC2.getFirstName());
        assertNull(expectedC2.getSurname());
    }

    /**
     * Este test es el complementario del anterior, en este caso es el contacto
     * existente el que tiene el apellido a null, no el nuevo.
     */
    @Test
    public void testAddDuplicateNameFirstContactWithoutSurname() {
        Contact c1 = new Contact();
        c1.setFirstName("Pedro");
        c1.setSurname(null);

        Contact c2 = new Contact();
        c2.setFirstName("Pedro");
        c2.setSurname("Ballesteros");

        String actualId1 = addressBook.addContact(c1);
        String actualId2 = addressBook.addContact(c2);

        List<Contact> contacts = addressBook.getAll();
        assertEquals(2, contacts.size());

        Contact expectedC1 = addressBook.getContact(actualId1);
        assertEquals("Pedro", expectedC1.getFirstName());
        assertNull(expectedC1.getSurname());


        Contact expectedC2 = addressBook.getContact(actualId2);
        assertEquals("Pedro", expectedC2.getFirstName());
        assertEquals("Ballesteros", expectedC2.getSurname());
    }

    @Test
    public void testAddDuplicateNameDifferentSurname() {
        Contact c1 = new Contact();
        c1.setFirstName("Pedro");
        c1.setSurname("Herranz");

        Contact c2 = new Contact();
        c2.setFirstName("Pedro");
        c2.setSurname("Ballesteros");

        String actualId1 = addressBook.addContact(c1);
        String actualId2 = addressBook.addContact(c2);

        List<Contact> contacts = addressBook.getAll();
        assertEquals(2, contacts.size());

        Contact expectedC1 = addressBook.getContact(actualId1);
        assertEquals("Pedro", expectedC1.getFirstName());
        assertEquals("Herranz", expectedC1.getSurname());

        Contact expectedC2 = addressBook.getContact(actualId2);
        assertEquals("Pedro", expectedC2.getFirstName());
        assertEquals("Ballesteros", expectedC2.getSurname());
    }

    @Test
    public void testAddDuplicateSurnameCapitalLetters() {
        Contact c1 = new Contact();
        c1.setFirstName("Pedro");
        c1.setSurname("Ballesteros");

        Contact c2 = new Contact();
        c2.setFirstName("Pedro");
        c2.setSurname("BALLESTEROS");

        addressBook.addContact(c1);
        try {
            addressBook.addContact(c2);
            fail("Expected InvalidContactException");
        } catch (InvalidContactException ex) {
            List<Contact> contacts = addressBook.getAll();
            assertEquals(1, contacts.size());
            assertEquals("Pedro", contacts.get(0).getFirstName());
            assertEquals("Ballesteros", contacts.get(0).getSurname());
        }
    }

    @Test
    public void testAddDuplicateSurnameWithBlanks() {
        Contact c1 = new Contact();
        c1.setFirstName("Pedro");
        c1.setSurname(" Ballesteros "); // Un espacio

        Contact c2 = new Contact();
        c2.setFirstName("Pedro");
        c2.setSurname("   Ballesteros   "); // Varios espacios

        addressBook.addContact(c1);
        try {
            addressBook.addContact(c2);
            fail("Expected InvalidContactException");
        } catch (InvalidContactException ex) {
            List<Contact> contacts = addressBook.getAll();
            assertEquals(1, contacts.size());
            assertEquals("Pedro", contacts.get(0).getFirstName());
            assertEquals("Ballesteros", contacts.get(0).getSurname());
        }
    }

    @Test
    public void testsAddDifferentNameSameData() throws ParseException {
        Date expedtedBirthday = dateFormat.parse("8/1/1974");

        Contact c1 = new Contact();
        c1.setFirstName("Pedro");
        c1.setSurname("Ballesteros");
        c1.setBirthday(expedtedBirthday);
        c1.setPhone("610101010");

        Contact c2 = new Contact();
        c2.setFirstName("Eduardo");
        c2.setSurname("Ballesteros");
        c2.setBirthday(expedtedBirthday);
        c2.setPhone("610101010");

        String actualId1 = addressBook.addContact(c1);
        String actualId2 = addressBook.addContact(c2);

        List<Contact> contacts = addressBook.getAll();
        assertEquals(2, contacts.size());

        Contact expectedC1 = addressBook.getContact(actualId1);
        assertEquals("Pedro", expectedC1.getFirstName());
        assertEquals("Ballesteros", expectedC1.getSurname());
        assertEquals(expedtedBirthday, expectedC1.getBirthday());
        assertEquals("610101010", expectedC1.getPhone());

        Contact expectedC2 = addressBook.getContact(actualId2);
        assertEquals("Eduardo", expectedC2.getFirstName());
        assertEquals("Ballesteros", expectedC2.getSurname());
        assertEquals(expedtedBirthday, expectedC2.getBirthday());
        assertEquals("610101010", expectedC2.getPhone());
    }

    @Test
    public void testDeleteContact()  {
        Contact c1 = new Contact();
        c1.setFirstName("Pedro");

        Contact c2 = new Contact();
        c2.setFirstName("Eduardo");

        // Una agenda con un solo contacto no asegura que solo se elimine
        // ese contacto en lugar de eliminar toda la agenda.
        String idDeleted = addressBook.addContact(c1);
        addressBook.addContact(c2);

        addressBook.deleteContact(idDeleted);

        // Solo queda uno
        assertEquals(1, addressBook.getAll().size());

        // Y se ha eliminado el que se quería. Queda mas claro aqui
        // que si se usa el @Test(expected=....)
        try {
          addressBook.getContact(idDeleted);
          fail("InvalidIdException expected");
        }
        catch(InvalidIdException ex) {
          assertTrue(true);
        }
    }
}
