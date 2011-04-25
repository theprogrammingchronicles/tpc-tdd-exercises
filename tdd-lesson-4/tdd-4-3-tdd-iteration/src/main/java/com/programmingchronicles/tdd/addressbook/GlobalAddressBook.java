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
 * Ejemplo de anticipación sin test o violación YAGNI en
 * {@link #addContact(com.programmingchronicles.tdd.domain.Contact) }.</p>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class GlobalAddressBook {

    private Map<String, Contact> addressBookMap = new HashMap();

    // Acceso al servicio de generación de ids, que se configurará
    // mediante Direct Injection (IoC).
    private IdGenerator idGenerator;

    /**
     * Añade un nuevo contacto devolviendo el id generado. El nombre
     * del contacto es obligatorio, si no se entrega se lanza una excepción.
     *
     * @param contact Datos del contacto a añadir
     * @return Devuelve el id asignado al contacto
     * @throws InvalidContactException
     */
    public String addContact(Contact contact) {

        // Nombre obligatorio: comprueba que si es null
        if(contact.getFirstName() == null) {
           throw new InvalidContactException();
        }

        // Nombre obligatorio: comprueba que es cadena vacía.
        // También se comprueba eliminado espacios antes y despues.
        // NOTA:
        //   Esta forma de implementarlo es muy tonta, pero sirve de ejemplo
        //   para ver como el adelantar funcionalidad se enmascara errores, tenemos
        //   una funcionalidad mal codificada, pero no hay test que la pruebe.
        // 
        // Error: Se necesitaba un || y se esta usando un &&.
        if(contact.getFirstName().length() < 1 && contact.getFirstName().trim().length() < 1) {
           throw new InvalidContactException();
        }
        // PROBLEMA: Violación del principio YAGNI o Tests First Development.

        // Estamos implementando el ejemplo de cuando se entrega "  PEDRO  ", ya que
        // al codificar nos damos cuenta de que esto sería bueno.

        // Pero este test no lo tenemos, nos estamos adelantando, no lo han pedido.
        // Estamos codificando más de lo que exigen los tests.

        // ¿Significa que esto que no hay que codificaro? NO

        // De los tests de aceptación pueden surgir más test unitarios. Y como
        // desarrolladores es nuestra misión detectar estos detalles de
        // implementación, aunque no estén ni vayan a estar en los tests
        // de aceptación.

        // Decidimos que esa comprobación es necesaria.

        // YAGNI aqui significa que:
        // Si queremos codificar esa funcionalidad antes debemos implementar
        // el test. De hecho como ahora no tenemos test no nos damos cuenta
        // de que está mal codificada.
        String id = idGenerator.newId();

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
        Contact result = addressBookMap.get(contactId);
        if(result == null) {
           throw new InvalidIdException();
        }
        return result;
    }       

    /**
     * Obtiene el generador de ids configurado.
     *
     * @return
     */
    public IdGenerator getIdGenerator() {
        return idGenerator;
    }

    /**
     * Configura el generador de ids utilizado.
     *
     * @param idGenerator
     */
    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }
}

