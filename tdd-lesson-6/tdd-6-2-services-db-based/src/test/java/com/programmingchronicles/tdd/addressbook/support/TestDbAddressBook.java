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

import com.programmingchronicles.tdd.addressbook.support.DbAddressBook;
import com.programmingchronicles.tdd.addressbook.InvalidContactException;
import com.programmingchronicles.tdd.addressbook.InvalidIdException;
import com.programmingchronicles.tdd.data.AddressBookDao;
import com.programmingchronicles.tdd.domain.Contact;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.*;
import org.mockito.ArgumentCaptor;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test de DbAddressBook.
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

    // Mocks & Test Objects
    private Contact expectedContact;
    private AddressBookDao mockAddressBookDao;

    @Before
    public void setUp() {
        // Object Under Test
        addressBook = new DbAddressBook();

        // Contacto por defecto usado en las pruebas.
        expectedContact = new Contact();

        // Crea un mock del DAO que se usará para verificación por estado
        // (stubbing) o interacción (mock) según las necesidades.
        mockAddressBookDao = mock(AddressBookDao.class);

        // Se configura la agenda con un stub/mock del DAO que programaremos
        // según el código a ejercitar en cada test.
        addressBook.setAddressBookDao(mockAddressBookDao);
    }

    /**
     * Valida que se añade correctamente un contacto simple.
     *
     * <p>
     * Parece que la única forma de validar que se añade bien un contacto es
     * realizar un "verify" de {@link AddressBookDao#addContact(Contact) }.</p>
     *
     * <p>
     * En este caso estamos ejercitando un método del DAO que devuelve datos
     * que el Object Under Test utiliza, por tanto es más eficiente crear un
     * stub de {@link AddressBookDao#addContact(Contact) } que devuelva datos
     * útiles, la interacción se está por tanto comprobando de forma indirecta
     * y el verify sería redundante.</p>
     *
     * <p>
     * El resultado final de este método es guardar el contacto en la base de
     * datos, y la forma de comprobar esto es asegurar que llama al DAO
     * encargado de realizar esta tarea. Pero ¿que aportaría validar tan solo
     * que se ha realizado la llamada al DAO?</p>
     *
     * <p>
     * Mediante stubs probamos ambos casos usando validación por estado.</p>
     *
     * <p>
     * La única forma de evitar la validación por interacción de forma completa
     * sería disponer de un Fake de DAO, que permitiera añadir y recuperar
     * datos, o usar la dependencia real.</p>
     */
    @Test
    public void testAddSimpleContact() {
        // Test Object
        expectedContact.setFirstName("Pedro");

        // Se crea un stub para addContact del DAO, el método a probar necesita
        // una respuesta adecuada.

        // ¿Por qué usamos any() como matcher?
        // Se profundizará sobre esto en los siguientes tests.
        when(mockAddressBookDao.addContact(any(Contact.class))).thenReturn("expectedId");

        // Test
        String contactId = addressBook.addContact(expectedContact);

        // Verificación de que se obtiene el id proporcionado por el DAO. Esta
        // verificación prueba indirectamente que se está obteniendo mediante
        // una llamada a "addContact", ya que se obtiene el id programado.
        assertEquals("expectedId", contactId);

        // Ahora un verify sería redundante, sería repetir exactamente lo mismo
        // que en el when. La interacción está probada ya que se obtiene la
        // la respuesta programada en el stub.

        // verify(mockAddressBookDao).addContact(eq(expectedContact));

        // El verify es prácticamente una copia del when.
    }

    /**
     * Probar que se devuelve correctamente un contacto de la base de datos.
     *
     * <p>
     * Al no disponer de base de datos se simula su contenido programando
     * respuesas del DAO (stub). Las respuesas del stub del DAO son nuestra
     * base de datos, programar respuestas es como simular la base de datos.</p>
     */
    @Test
    public void testGetSimpleContact() {
        // Se programa el contacto a devolver por el DAO.
        expectedContact.setFirstName("Pedro");
        when(mockAddressBookDao.getContact("idcontact")).thenReturn(expectedContact);

        // Test
        Contact actualContact = addressBook.getContact("idcontact");

        // IMPORTANTE:

        // Aunque el stub se ha programado para devolver la instancia
        // "expectedContact", no podemos probar que se devuelve exactamente
        // esta instancia, la lógica de negocio podría hacer por ejemplo una
        // copia para devolverla, y es mejor no fijar en el test que addContact
        // devuelve exactamente la misma instancia.
        assertEquals("Pedro", actualContact.getFirstName());
    }

    @Test
    public void testGetContactInvalidId() {
        // Si el stub del DAO no se programa con alguna respuesta es como tener
        // una base de datos vacía, el contacto no existirá.
        try {
          addressBook.getContact("INVALID");
          fail("InvalidIdException expected");
        } catch(InvalidIdException ex) {
          assertTrue(true);
        }
    }

    /**
     * Prueba que se puede añadir un contacto completo.
     *
     * <p>
     * Ahora el test {@link #testAddSimpleContact} es redundante ya que prueba
     * prácticamente lo mismo que este test. Es mejor mantener este testen lugar
     * de {@link #testAddSimpleContact} ya que aquí probamos además que no
     * hay problemas cuando el contacto es correcto (no lanza excepciones).</p>
     *
     *
     * @throws ParseException
     */
    @Test
    public void testAddFullContact() throws ParseException {

        // Contacto Completo de Ejemplo.
        expectedContact.setFirstName("Pedro");
        expectedContact.setSurname("Ballesteros");
        Date expectedBirthday = dateFormat.parse("8/1/1974");
        expectedContact.setBirthday(expectedBirthday);
        expectedContact.setPhone("610101010");

        // Programación del stub para verificar que se obtiene un ID.
        // NOTA:
        //   Si solo hicieramos verificación por interacción, no haría falta
        //   programar el stub.
        when(mockAddressBookDao.addContact(any(Contact.class))).thenReturn("fullContactId");

        // Test
        String id = addressBook.addContact(expectedContact);

        // Verificación de que se devuelve el id correcto.
        assertEquals("fullContactId", id);

        // Ahora sí conviene asegurarse que el contacto se añade bien a la base
        // de datos. Y esto se realiza mediante validación por interacción.

        // NO se va a verificar que se llama al DAO con la instancia "expectedContact"
        // ya que la lógica de negocio no tendría porque entregar al DAO exactamente
        // el mismo objeto, podría hacer una copia.

        // Como la implementación la realizamos con TDD nosotros implementamos
        // lo que establezcamos con el test, nosotros podemos decidir que
        // la implementación use exactamente la misma instancia (lo que es muy
        // habitual). Pero esto hace el test más inflexible. Y así se aprovecha
        // el ejercicio para ver ejemplos más avanzados.

        // Verificación capturando los argumentos.
        ArgumentCaptor<Contact> argument = ArgumentCaptor.forClass(Contact.class);
        verify(mockAddressBookDao).addContact(argument.capture());
        assertEquals("Pedro", argument.getValue().getFirstName());
        assertEquals("Ballesteros", argument.getValue().getSurname());
        assertEquals(expectedBirthday, argument.getValue().getBirthday());
        assertEquals("610101010", argument.getValue().getPhone());


        // Verificación usando el eq() matcher, que compara los objetos
        // utilizando el método "equals".

        // PELIGRO:
        //  Por defecto el equals siempre hace una comparación de instancias.
        //  Si no se ha sobrescrito el Contact#equals, no estaremos haciendo
        //  la misma verificación que en el ejemplo anterior.

        //  TO DO: Se debería implementar el equals si se quiere hacer
        //         el matching por valor en lugar de referncia usando eq().
        //
        //  En un ejercicio posterior se verán ejemplos de custom matchers como
        //  alternativa a la implementación de equals.

        verify(mockAddressBookDao).addContact(eq(expectedContact));


        // La verificación del eq() matcher es la verificación por defecto si
        // no se explicita un matcher.

        // Esto es por tanto exactamente lo mismo que lo anterior.
        verify(mockAddressBookDao).addContact(expectedContact);
    }

    /**
     * Cuando solo se hace verificación por interacción (mock), no hace falta
     * programar respuestas (stub).
     *
     */
    @Test
    public void testAddWithoutFirstName() {
        expectedContact.setSurname("Ballesteros");

        try {
            addressBook.addContact(expectedContact);
            fail("InvalidContactException expected");
        } catch(InvalidContactException ex) {
            // En este caso si es muy adecuado verificar la no interacción
            // ya que nos aseguramos que realmente no se ha escrito el
            // contacto en la base de datos.
            verify(mockAddressBookDao, never()).addContact(any(Contact.class));
        }
    }

    @Test
    public void testAddBlankFirstName() {
        expectedContact.setFirstName("   ");
        expectedContact.setSurname("Ballesteros");

        try {
            addressBook.addContact(expectedContact);
            fail("InvalidContactException expected");
        } catch(InvalidContactException ex) {
            // En este caso si es muy adecuado verificar la no interacción
            // ya que nos aseguramos que realmente no se ha escrito el
            // contacto en la base de datos.
            verify(mockAddressBookDao, never()).addContact(any(Contact.class));
        }
    }

    @Test
    public void testAddEmptyFirstName() {
        expectedContact.setFirstName("");
        expectedContact.setSurname("Ballesteros");

        try {
            addressBook.addContact(expectedContact);
            fail("InvalidContactException expected");
        } catch(InvalidContactException ex) {
            verify(mockAddressBookDao, never()).addContact(any(Contact.class));
        }
    }

    /**
     * Simulación de datos existentes en la base de datos.
     *
     * <p>
     * Ahora la forma de provocar un contacto repetido es programar
     * el stub para que devuelve el contacto que se va a añadir, ya
     * que usar el addContact(c1) no sirve para nada.</p>
     *
     */
    @Test
    public void testAddDuplicateFirstName() {
        Contact c1 = new Contact();
        c1.setFirstName("Pedro");

        Contact c2 = new Contact();
        c2.setFirstName("Pedro");

        // Ahora la forma de provocar un contacto repetido es programar
        // el stub para que devuelve el contacto que se va a añadir, ya
        // que usar el addContact(c1) no sirve para nada.
        //
        //   addressBook.addContact(c1);
        //
        when(mockAddressBookDao.getAll()).thenReturn(Arrays.asList(c1));

        try {
            addressBook.addContact(c2);
            fail("Expected InvalidContactException");
        } catch (InvalidContactException ex) {
            // En este caso si es muy adecuado verificar la no interacción
            // ya que nos aseguramos que realmente no se ha escrito el
            // contacto en la base de datos.
            verify(mockAddressBookDao, never()).addContact(any(Contact.class));
        }
    }

    @Test
    public void testAddDuplicateNameWithCapital() {
        Contact c1 = new Contact();
        c1.setFirstName("Pedro");

        Contact c2 = new Contact();
        c2.setFirstName("PEDRO");

        when(mockAddressBookDao.getAll()).thenReturn(Arrays.asList(c1));
        try {
            addressBook.addContact(c2);
            fail("Expected InvalidContactException");
        } catch (InvalidContactException ex) {
            // En este caso si es muy adecuado verificar la no interacción
            // ya que nos aseguramos que realmente no se ha escrito el
            // contacto en la base de datos.
            verify(mockAddressBookDao, never()).addContact(any(Contact.class));
        }
    }


    /**
     * Aparecen fallos de implementación.
     *
     * <p>
     * Este test hace aparecer fallos en la implementación anterior. El test
     * falla con la lógica actual ya que asume que los contactos siempre se
     * añaden con los espacios eliminados.</p>
     *
     * <p>
     * En la implementación anterior podría darse por válido, ya que
     * el mapa solo era accesible desde la implementación. Pero ahora la base
     * de datos podría ser manipulada por muchas aplicaciones, por tanto no se
     * puede hacer esta suposición.</p>
     *
     * <p>
     * Se debe refactorizar la lógica de negocio y adaptarla a esta
     * implementación basada en base de datos.</p>
     */
    @Test
    public void testAddDuplicateNameWithBlanks() {
        Contact c1 = new Contact();
        c1.setFirstName(" Pedro "); // Un espacio

        Contact c2 = new Contact();
        c2.setFirstName("    PEDRO    "); // Mas de un espacio

        when(mockAddressBookDao.getAll()).thenReturn(Arrays.asList(c1));
        try {
            addressBook.addContact(c2);
            fail("Expected InvalidContactException");
        } catch (InvalidContactException ex) {
            // En este caso si es muy adecuado verificar la no interacción
            // ya que nos aseguramos que realmente no se ha escrito el
            // contacto en la base de datos.
            verify(mockAddressBookDao, never()).addContact(any(Contact.class));
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

        when(mockAddressBookDao.getAll()).thenReturn(Arrays.asList(c1));
        try {
            addressBook.addContact(c2);
            fail("Expected InvalidContactException");
        } catch (InvalidContactException ex) {
            // En este caso si es muy adecuado verificar la no interacción
            // ya que nos aseguramos que realmente no se ha escrito el
            // contacto en la base de datos.
            verify(mockAddressBookDao, never()).addContact(any(Contact.class));
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

        // Ya existe en la base de datos un contacto con el mismo nombre y
        // con un apellido.
        when(mockAddressBookDao.getAll()).thenReturn(Arrays.asList(c1));

        // Se prueba a añadir correctamente un contacto con el mismo nombre
        // y sin apellido.
        addressBook.addContact(c2);

        // Verifica que se añade a la base de datos.
        verify(mockAddressBookDao).addContact(any(Contact.class));
    }

    @Test
    public void testAddDuplicateNameFirstContactWithoutSurname() {
        Contact c1 = new Contact();
        c1.setFirstName("Pedro");
        c1.setSurname(null);

        Contact c2 = new Contact();
        c2.setFirstName("Pedro");
        c2.setSurname("Ballesteros");

        when(mockAddressBookDao.getAll()).thenReturn(Arrays.asList(c1));

        addressBook.addContact(c2);

        verify(mockAddressBookDao).addContact(any(Contact.class));
    }

    @Test
    public void testAddDuplicateNameDifferentSurname() {
        Contact c1 = new Contact();
        c1.setFirstName("Pedro");
        c1.setSurname("Herranz");

        Contact c2 = new Contact();
        c2.setFirstName("Pedro");
        c2.setSurname("Ballesteros");

        when(mockAddressBookDao.getAll()).thenReturn(Arrays.asList(c1));

        addressBook.addContact(c2);

        verify(mockAddressBookDao).addContact(any(Contact.class));
    }

    @Test
    public void testAddDuplicateSurnameCapitalLetters() {
        Contact c1 = new Contact();
        c1.setFirstName("Pedro");
        c1.setSurname("Ballesteros");

        Contact c2 = new Contact();
        c2.setFirstName("Pedro");
        c2.setSurname("BALLESTEROS");

        when(mockAddressBookDao.getAll()).thenReturn(Arrays.asList(c1));
        try {
            addressBook.addContact(c2);
            fail("Expected InvalidContactException");
        } catch (InvalidContactException ex) {
            verify(mockAddressBookDao, never()).addContact(any(Contact.class));
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

        when(mockAddressBookDao.getAll()).thenReturn(Arrays.asList(c1));
        try {
            addressBook.addContact(c2);
            fail("Expected InvalidContactException");
        } catch (InvalidContactException ex) {
            verify(mockAddressBookDao, never()).addContact(any(Contact.class));
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

        // El contacto c1 ya existe en la base de datos
        when(mockAddressBookDao.getAll()).thenReturn(Arrays.asList(c1));

        // Se debe poder añadir el segundo contacto con exito
        addressBook.addContact(c2);

        // Verifica que se entrega al dao el contacto adecuado.
        ArgumentCaptor<Contact> argument = ArgumentCaptor.forClass(Contact.class);
        
        verify(mockAddressBookDao).addContact(argument.capture());
        Contact expectedC2 = argument.getValue();
        assertEquals("Eduardo", expectedC2.getFirstName());
        assertEquals("Ballesteros", expectedC2.getSurname());
        assertEquals(expedtedBirthday, expectedC2.getBirthday());
        assertEquals("610101010", expectedC2.getPhone());
    }

    /**
     * Este test sería prácticamente innecesario si tenemos una batería
     * de tests de integración.
     *
     * <p>
     * Es una delegación directa, no hay lógica de negocio. ¿Aporta  mucho
     * validar esto para dirigir los desarrollos?</p>
     */
    @Test
    public void testDeleteContact()  {        
        addressBook.deleteContact("idDeleted");
        verify(mockAddressBookDao).deleteContact("idDeleted");
    }

    /**
     * Este test sería prácticamente innecesario si tenemos una batería
     * de test de integración.
     *
     * <p>
     * Es una delegación directa de la dependencia. Aun así se implementa para
     * que sirva de ejercicio de ejemplo de comprobación de listas, e introducción
     * a los hamcrest matchers.</p>
     * 
     * @throws ParseException
     */
    @Test
    public void testGetAll() throws ParseException {
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

        // Simulamos que la agenda tiene ambos contactos.
        when(mockAddressBookDao.getAll()).thenReturn(Arrays.asList(c1, c2));

        // Test
        List<Contact> contacts = addressBook.getAll();

        // Comprueba que se devuelve una lista conteniendo únicamente
        // los dos elementos de prueba en cualquier orden.
        assertEquals(2, contacts.size());
        assertTrue(contacts.containsAll(Arrays.asList(c2, c1)));

        // La misma comprobación que "containsAll" pero usando los "hamcrest
        // matchers" que ofrecen una sintaxis más descriptiva y unos textos
        // de error más detallados en caso de fallo del test.
        assertThat(contacts, hasItems(c2, c1));

        // NOTA:
        //  Para que este test fuera más flexible y coherente con el resto
        //  de tests habria que implementar Contact#equals ya que ahora
        //  las comparaciones se están haciendo por instancia no por valor.

        // En un el ejercicio 6.4 se muestra como solucionar esta comparación
        // mediante custom matchers, lo que evita depender del equals.
    }
}
