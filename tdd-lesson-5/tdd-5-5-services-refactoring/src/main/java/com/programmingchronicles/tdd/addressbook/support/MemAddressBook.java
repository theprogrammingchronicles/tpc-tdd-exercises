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

import com.programmingchronicles.tdd.addressbook.GlobalAddressBook;
import com.programmingchronicles.tdd.addressbook.IdGenerator;
import com.programmingchronicles.tdd.addressbook.InvalidContactException;
import com.programmingchronicles.tdd.addressbook.InvalidIdException;
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
 * <b>Refactorización:</b><br/>
 *   Se ha extraido un interface y ahora esta es una implementación del servicio
 *   GlobalAddressBook basado en el almacenamiento en memoria.</p>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class MemAddressBook implements GlobalAddressBook {

    private Map<String, Contact> addressBookMap = new HashMap();

    // Acceso al servicio de generación de ids, que se configurará
    // mediante Direct Injection (una forma de IoC).
    private IdGenerator idGenerator;

    /**
     * Añade un nuevo contacto devolviendo el id generado.
     *
     * @param contact Datos del contacto a añadir
     * @return Devuelve el id asignado al contacto
     */
    @Override
    public String addContact(Contact contact) {
        if(contact.getFirstName() == null || contact.getFirstName().trim().length() < 1) {
           throw new InvalidContactException();
        }       
             
        contact.setFirstName(contact.getFirstName().trim());
        if(contact.getSurname() != null) {
           contact.setSurname(contact.getSurname().trim());
        }
           
        if(checkDuplicate(contact)) {
           throw new InvalidContactException();
        }

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
    @Override
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
    @Override
    public Contact getContact(String contactId) {
        Contact result = addressBookMap.get(contactId);
        if(result == null) {
           throw new InvalidIdException();
        }
        return result;
    }       

    @Override
    public void deleteContact(String id) {
        addressBookMap.remove(id);
    }

    private boolean checkDuplicate(Contact checkedContact) {
        String checkedFirstName = checkedContact.getFirstName();
        String checkedSurname = checkedContact.getSurname();

        for(Contact contact: addressBookMap.values()) {
            String firstName = contact.getFirstName();
            String surname = contact.getSurname();

            if(checkedFirstName.equalsIgnoreCase(firstName)) {
                if(checkedSurname != null) {
                   return checkedSurname.equalsIgnoreCase(surname);
                } else if(surname == null) {
                   return true;
                }
            }
        }
        return false;
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

