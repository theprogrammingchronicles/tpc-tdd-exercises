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
import com.programmingchronicles.tdd.addressbook.InvalidIdException;
import com.programmingchronicles.tdd.domain.Contact;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementación de un servicio Agenda de Contactos Global basado en el
 * almacenamiento en memoria.
 *
 * Desacoplando la instanciación mediante Direct Injection
 *
 * La responsabilidad de crear y configurar las dependencias se eleva a
 * un nivel superior. El cliente de GlobalAddressBook decide que dependencias
 * utilizará la instancia que necesita, es decir, configura parte de su
 * comportamiento en función de las caracteristicas deseadas.
 *
 * En este entorno la clase MemGlobalAddressBook o FakeGlobalAddressBook son
 * completamente independientes de cualquier implementación concreta de
 * IdGenerator, solo saben que se usará un IdGenerator, cualquier implementación
 * existente le servirá, y con cualquiera funcionará correctamente, es el
 * cliente el que decide que le conviene.
 *
 * MUY IMPORTANTE
 * La configuración de dependencias es un detalle de implementación, MemGlobalAddressBook
 * necesita un IdGenerator, pero DBGlobalAddressBook no lo necesita. Nunca debemos
 * meter métodos de configuración en la interfaz GlobalAddressBook, sería meter
 * detalles de implementación. Solo metemos lo generico y lo que es parte real
 * de la API y no de la implementación.
 *
 * TESTEABLE
 * Ahora MemAddressBook está mejor preparada para el unit testing, podemos
 * aislar los tests de las dependencias, no los basamos ni en RandomIdGenerator
 * ni en IncrementIdGenerator, solo queremos probar que el código de esta clase
 * es válido sin importarnos el de otras clases.
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class MemAddressBook implements GlobalAddressBook {

    private Map<String, Contact> addressBookMap = new HashMap();

    // Acceso un servicio de generación de ids
    private IdGenerator idGenerator;

    // Constructor por defecto sin IdGenerator, que se puede configurar
    // modificando accediendo a setIdGenerator.
    public MemAddressBook() {      
    }

    // Direct Injection basado en constructor, se debe entregar
    // una implementación de la interfaz IdGenerator.
    public MemAddressBook(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    // Direct Injection basado en propiedad. Permite modificar
    // el IdGenerator usado por MemAddressBook
    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    // Acceso al IdGenerator configurado actualmente
    public IdGenerator getIdGenerator() {
        return idGenerator;
    }
   
    /**
     * Añade un nuevo contacto devolviendo el id generado.
     *
     * @param contact Datos del contacto a añadir
     * @return Devuelve el id asignado al contacto
     */
    @Override
    public String addContact(Contact contact) {
        String id = idGenerator.newId();
        
        // Como el servicio de generación de ids puede proporcionar
        // ids repetidos, se debe comprobar si el id ya fue asignado.

        // Se podría implementar una lógica de reintentos, pero finalmente
        // decidimos que es mejor informar del error y que sean los
        // clientes los encargados de decidir la politica de reintentos.
        if(addressBookMap.get(id) != null) {
            throw new InvalidIdException();
        }

        contact.setId(id);                
        addressBookMap.put(id, contact);
        return id;
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

    /**
     * Obtiene todos los contactos del sistema.
     *
     * @return
     */
    @Override
    public List<Contact> getAll() {
        // TESTS: En este proyecto no tiene tests, pero es que ha sido copiado
        //        de una versión más avanzada de los ejercicios de TDD.
        List<Contact> contacts = new ArrayList(addressBookMap.size());
        contacts.addAll(addressBookMap.values());
        return contacts;
    }
}
