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

package com.programmingchronicles.tdd.it;

import com.programmingchronicles.tdd.addressbook.InvalidContactException;
import com.programmingchronicles.tdd.addressbook.InvalidIdException;
import com.programmingchronicles.tdd.addressbook.support.DbAddressBook;
import com.programmingchronicles.tdd.data.support.JdbcAddressBookDao;
import com.programmingchronicles.tdd.domain.Contact;
import java.sql.SQLException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import org.junit.*;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;
import static com.programmingchronicles.tdd.it.utils.IsContactEqual.*;
import static com.programmingchronicles.tdd.it.utils.DatabaseTestSupport.*;

/**
 * Test de DbAddressBook.
 *
 * IMPORTANTTE: SON TEST DE INTEGRACIOÇN el Beforeclass y affet desaparecen
 * ya no hay que crear la base de datos.
 *
 * <p>Como la implementación de DbAddressBook es una copia exacta de MemAddressBook,
 * usaremos TDD para comenzar su refactorización.</p>
 *
 * <p>La mayor parte de los test de TestMemAddressBook siguen siendo válidos,
 * por tanto se irán copiando incrementalmente, refactorizando antes de pasar
 * al siguiente test. Identificando durante la refactorización si la nueva
 * implementación requiere test particulares.</p>
 *
 * <p><b>VALIDACION POR ESTADO vs INTERACCION</b><br/>
 * Para independizar los test de la base de datos se deben utilizar stubs o mocks,
 * la duda es si se puede validar usando stubbing (respuestas programadas), o
 * mocking (interacción).</p>
 *
 * <p>
 * Se aplicarán los criterios vistos en la teoría, intentando evitar lo máximo
 * posible validaciones por interacción, y en los casos que no sea posible, la
 * interacción verificada será la mínima requerida para satisfacer el test.</p>
 *
 * <p>
 * No se debe atar la implementación al test, validando cada interacción o cada
 * ausencia de interacción.<br/>
 * En una validación por interacción el "verify" debe ser el equivalente al
 * "assert". Un "assert" comprueba los resultados obtenidos, y un "verify"
 * puede ser su equivalente, en este caso el resultado es una interacción.<p>
 *
 * <p><b>
 * La excesiva verificación de interacción o verificación de ausencia de
 * interacción hacen muchas validaciones redundantes, los tests inflexibles y
 * poco mantenibles.</b></p>
 *
 * <p>
 * <b> Ver: </b>
 * <a href="http://monkeyisland.pl/2008/04/26/asking-and-telling/"></a>
 * <a href="http://codebetter.com/blogs/aaron.jensen/archive/2008/04/03/separate-stub-and-verify-duplicate-code-necessarily.aspx"></a>
 * </p>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class TestDbAddressBook {
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    // Object Under Test
    private DbAddressBook addressBook;

    // Contacto por defecto usado en las pruebas.
    private Contact expectedContact;

    // Conexión utilizada en los tests, se utiliza una conexión por test
    // para hacer la prueba de integración más realista.
    private Connection connection;

    @BeforeClass
    public static void setUpClass() throws SQLException {
        createTables();
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        dropTables();
    }

    @Before
    public void setUp() throws SQLException {
        connection = newConnection();

        // Se utiliza un datasource que devuelve siempre la misma conexión
        // y que ignora la gestión de transacciones y cierre de conexiones,
        // para que los tests puedan realizar el rollback de las modificaciones.
        DataSource dataSource = newSingleNonTransactionalDataSource(connection);

        // Inicializa el servicio de AddressBook para acceso a la BD de pruebas.
        JdbcAddressBookDao dao = new JdbcAddressBookDao();
        dao.setDataSource(dataSource);

        addressBook = new DbAddressBook();
        addressBook.setAddressBookDao(dao);

        // Contacto por defecto usado en las pruebas.
        expectedContact = new Contact();

        // Al inicio de cada test se asegura la gestión manual de transacciones.
        connection.setAutoCommit(false);
    }

    @After
    public void tearDown() throws SQLException {
        // Despues de cada test se realiza un rollback para conservar
        // la independencia entre los tests.
        try {
            connection.rollback();
        } finally {
            // Como se crea una conexión en cada test se debe realizar el
            // close de la conexión.
            connection.close();
        }
    }

    @Test
    public void testAddSimpleContact() throws SQLException, InterruptedException {
        // Test Object
        expectedContact.setFirstName("Pedro");

        // Test
        connection.setAutoCommit(false);
        String contactId = addressBook.addContact(expectedContact);

        // Verifica el contenido de la agenda.
        List<Contact> contacts = addressBook.getAll();
        assertEquals(1, contacts.size());
        assertEquals(contactId, contacts.get(0).getId());
        assertEquals("Pedro", contacts.get(0).getFirstName());
    }

    @Test
    public void testAddAndGetFullContact() throws ParseException, SQLException {
        // Contacto Completo de Ejemplo.
        expectedContact.setFirstName("Pedro");
        expectedContact.setSurname("Ballesteros");
        Date expectedBirthday = dateFormat.parse("8/1/1974");
        expectedContact.setBirthday(expectedBirthday);
        expectedContact.setPhone("610101010");

        // Test
        String expectedId = addressBook.addContact(expectedContact);
        Contact contact = addressBook.getContact(expectedId);

        // Verificación
        expectedContact.setId(expectedId);
        assertThat(contact, contactEq(expectedContact));
    }

    @Test
    public void testGetContactInvalidId() {
        // La agenda está vacía.
        try {
            addressBook.getContact("INVALID");
            fail("InvalidIdException expected");
        } catch (InvalidIdException ex) {
            assertTrue(true);
        }
    }

    @Test
    public void testAddWithoutFirstName() {
        expectedContact.setSurname("Ballesteros");

        try {
            addressBook.addContact(expectedContact);
            fail("InvalidContactException expected");
        } catch (InvalidContactException ex) {
            assertTrue(true);
        }
    }

    @Test
    public void testAddBlankFirstName() {
        expectedContact.setFirstName("   ");
        expectedContact.setSurname("Ballesteros");

        try {
            addressBook.addContact(expectedContact);
            fail("InvalidContactException expected");
        } catch (InvalidContactException ex) {
            assertTrue(true);
        }
    }

    @Test
    public void testAddEmptyFirstName() {
        expectedContact.setFirstName("");
        expectedContact.setSurname("Ballesteros");

        try {
            addressBook.addContact(expectedContact);
            fail("InvalidContactException expected");
        } catch (InvalidContactException ex) {
           assertTrue(true);
        }
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
            assertThat(contacts, hasItem(contactNoIdEq(c1)));
        }
    }

    @Test
    public void testAddDuplicateNameWithNullSurname() {
        Contact c1 = new Contact();
        c1.setFirstName("Pedro");
        c1.setSurname("Ballesteros");

        Contact c2 = new Contact();
        c2.setFirstName("Pedro");
        c2.setSurname(null);

        addressBook.addContact(c1);
        addressBook.addContact(c2);

        // Test
        List<Contact> contacts = addressBook.getAll();

        // Verify
        assertEquals(2, contacts.size());
        assertThat(contacts, hasItems(contactNoIdEq(c2), contactNoIdEq(c1)));
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

        addressBook.addContact(c1);
        addressBook.addContact(c2);

        List<Contact> contacts = addressBook.getAll();

        // Verify
        assertEquals(2, contacts.size());
        assertThat(contacts, hasItems(contactNoIdEq(c2), contactNoIdEq(c1)));
    }

    @Test
    public void testAddDuplicateNameDifferentSurname() {
        Contact c1 = new Contact();
        c1.setFirstName("Pedro");
        c1.setSurname("Herranz");

        Contact c2 = new Contact();
        c2.setFirstName("Pedro");
        c2.setSurname("Ballesteros");

        addressBook.addContact(c1);
        addressBook.addContact(c2);

        List<Contact> contacts = addressBook.getAll();

         // Verify
        assertEquals(2, contacts.size());
        assertThat(contacts, hasItems(contactNoIdEq(c2), contactNoIdEq(c1)));
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
            assertThat(contacts, hasItems(contactNoIdEq(c1)));
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
            assertThat(contacts, hasItems(contactNoIdEq(c1)));
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

        addressBook.addContact(c1);
        addressBook.addContact(c2);

        List<Contact> contacts = addressBook.getAll();

        assertEquals(2, contacts.size());
        assertThat(contacts, hasItems(contactNoIdEq(c2), contactNoIdEq(c1)));
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
        // Se elimina el primer contacto añadido.
        addressBook.deleteContact(idDeleted);

        // Verifica que ya solo queda un contacto.
        List<Contact> contacts = addressBook.getAll();
        assertEquals(1, contacts.size());
        assertThat(contacts, hasItems(contactNoIdEq(c2)));

        // Test: Se intenta borrar de nuevo el contacto, por lo que
        // debe lanzar la excepción definida en la interfaz.
        try {
          addressBook.getContact(idDeleted);
          fail("InvalidIdException expected");
        }
        catch(InvalidIdException ex) {
          assertTrue(true);
        }
    }
}

