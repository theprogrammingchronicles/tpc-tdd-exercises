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

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Implementación de un small scaled test.
 *
 * <p>
 * En la primera iteracción el fallo es que ni siquiera compila. En siguientes
 * iteracciones la compilación no será el principal fallo ya que se continuarán
 * implementando métodos que ya existen y compilan.</p>
 *
 * <p>
 * Importancia de ejecutar el test antes de codificar la funcionalidad: en
 * siguientes ejercicios se veran ejemplos más evidentes. El fallo del test
 * muestra que lo estamos implementando bien, que realmente vamos a implementar
 * una corrección.</p>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class TestGlobalAddressBook {    

    @Test
    public void testAddContact() {
        // Creación del "Object Under Test"
        GlobalAddressBook addressBook = new GlobalAddressBook();

        // Fixture: Creación de datos de prueba o "Test Objects"
        // Diseño: Decidimos que una clase Contact tendrá los datos de los contactos.
        Contact contact = new Contact();
        contact.setFirstName("Pedro");

        // Test.
        addressBook.addContact(contact);

        // Verifica que la agenda contiene el contacto que se acaba de añadir.
        // Pregunta: ¿Es correcto usar métodos del Object Under Test para
        //            realizar la verificación?.
        List<Contact> contacts = addressBook.getAll();
        assertEquals(1, contacts.size());
        assertEquals("Pedro", contacts.get(0).getFirstName());
    }
}