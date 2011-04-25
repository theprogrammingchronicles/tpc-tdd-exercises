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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementación de un servicio Agenda de Contactos Global
 * basado en el almacenamiento en memoria.
 *
 * <p>
 * <b>Refactoring:</b><br/>
 *   Aplicando el Principio SRP (Single Responsibility Principle).<br/>
 *   Dos posibilidades: a nivel de método y a nivel de clase.</p>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class GlobalAddressBook {

    private Map<String, Contact> addressBookMap = new HashMap();

    // REFACTORIZACION:
    //   Nos podríamos plantear que la generación de ids ni siquiera es
    //   responsabilidad del AddressBook, o que es una funcionalidad tán
    //   generica que si se extrae se puede reutilizar.
    //
    // TODO: En un posterior ejemplo aplicaremos esta politica para
    //       empezar con el tema de los stubs/mocks.
    private static int nextId = 0;

    /**
     * REFACTORIZACION.
     * <br/>
     *
     * <pre> 
     * ¿Es realmente responsabilidad del addContact generar los ids?
     *  <i>Pregunta</i>: ¿Y si cambio la forma de generar ids?
     *
     *  Decidimos que si queremos cambiar el sistema de generación de ids
     *  no queremos cambiar el sistema de añadir contactos.
     * </pre>
     *
     * @param contact Datos del contacto a añadir
     * @return Devuelve el id asignado al contacto
     */
    public String addContact(Contact contact) {
        String id = createNewId();
        
        contact.setId(id);                
        addressBookMap.put(id, contact);
        return id;
    }

    /**
     * Obtiene todos los contactos del sistema.
     *
     * @return
     */
    public List<Contact> getAll() {
        List<Contact> contacts = new ArrayList(addressBookMap.size());
        contacts.addAll(addressBookMap.values());
        return contacts;
    }

    /**
     * Obtiene el contacto asociado al id entregado.
     *
     * @param contactId id del contacto
     * @return Devuelve el contacto
     */
    public Contact getContact(String contactId) {
        return addressBookMap.get(contactId);
    }

    // Método privado con la única responsabilidad de generar ids
    private String createNewId() {
        return Integer.toString(nextId++);
    }
}
