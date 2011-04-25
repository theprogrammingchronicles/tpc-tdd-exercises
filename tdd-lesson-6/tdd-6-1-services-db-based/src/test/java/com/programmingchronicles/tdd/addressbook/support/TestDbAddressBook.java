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
import com.programmingchronicles.tdd.data.AddressBookDao;
import com.programmingchronicles.tdd.domain.Contact;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test de DbAddressBook.
 *
 * <p>Como la implementación de DbAddressBook es una copia exacta de MemAddressBook,
 * se utilizará TDD para comenzar su refactorización</p>
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
 *
 * En una validación por interacción el "verify" debe ser el equivalente al
 * "assert". Un "assert" comprueba los resultados obtenidos, y un "verify"
 * puede ser su equivalente, en este caso el resultado es una interacción.<p>
 *
 * <p><b>
 * La excesiva verificación de interacción o verificación de ausencia de
 * interacción hacen muchas validaciones redundantes, los tests inflexibles y
 * poco mantenibles</b></p>
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


    @Test
    public void testAddContact() {
        // Fixture
        expectedContact.setFirstName("Pedro");
        when(mockAddressBookDao.addContact(expectedContact)).thenReturn("newId");


        // Test
        String contactId = addressBook.addContact(expectedContact);

        // Verify
        assertEquals("newId", contactId);



      //  verify(mockAddressBookDao, times(1)).addContact(expectedContact);


        //verify(generatorMock, times(1)).newId();
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


    @Test
    public void testAddWithoutFirstName() {
        expectedContact.setSurname("Ballesteros");

        try {
             addressBook.addContact(expectedContact);
             fail("InvalidContactException expected.");
        } catch(InvalidContactException ex) {

        }
    }
}
