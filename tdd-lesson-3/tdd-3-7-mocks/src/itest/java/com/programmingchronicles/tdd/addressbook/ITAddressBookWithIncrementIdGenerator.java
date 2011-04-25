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
import com.programmingchronicles.tdd.addressbook.support.IncrementIdGenerator;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Ejemplo de tests de GlobalAddressBook integrado con la implementación
 * IncrementIdGenerator de IdGenerator.
 *
 * <p>
 * Este ejemplo muestra como mantener tests de integración en una batería de tests
 * independientes dentro del mismo proyecto. Se ejecutan en la fase final "verify"
 * para probar sobre el paquete ya generado.</p>
 * 
 * <p>
 * Esta división por baterías también se puede aplicar a tests que se vuelven
 * demasiado pesados y solo se ejecutan en cada commit, y no en cada iteración
 * del bucle TDD. Aunque este tipo de tests conviene evitarlos.</p>
 * 
 * <p>
 * <b>Proyectos para Test de Integración</b><br/>
 *    En un ejemplo posterior se verá que los tests de integración más complejos
 *    se pueden o se deben mantener en un proyecto independiente, ya que los tests
 *    quedan más separados de los detalles de implementación. Las pruebas se realizan
 *    sin tener acceso directo al código fuente.</p>
 * 
 * <p>
 * <b>NOTA</b><br/>
 * En este caso no sería necesario mantener estos tests independientes, ya que
 * al ser una integración a pequeña escala se pueden considerar tests válidos
 * para el desarrollo TDD.</p>
 *
 * <p>
 * Sabemos que IncrementIdGenerator es correcto ya que tiene sus propios
 * tests unitarios, por tanto no es tan problematico usar la implementación
 * real en lugar del mock.</p>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class ITAddressBookWithIncrementIdGenerator {

    GlobalAddressBook addressBook;

    @Before
    public void setUp() {
        // Principio de Independencia:
        //   Cada test tiene su propia instancia del Object Under Test y del Stub.
        addressBook = new GlobalAddressBook();

        // Se usa una implementación real del generador de ids.
        addressBook.setIdGenerator(new IncrementIdGenerator());
    }

    /**
     * Test que añade un contacto y se verifica obteniendo el contenido
     * actual de la agenda.
     */
    @Test
    public void testAddContact() {
        Contact expectedContact = new Contact();
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
        Contact expectedContact = new Contact();
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
}
