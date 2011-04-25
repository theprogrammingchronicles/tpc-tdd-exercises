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
import java.util.HashMap;
import java.util.Map;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


/**
 * Implementación de un servicio Agenda de Contactos Global basado en el
 * almacenamiento en memoria.
 *
 * Desacoplando la clase de la instanciación de los detalles de implementación es
 * decir, de la instanciación de clases que implementan la interfaz de la
 * que dependenden.
 *
 * Solución basada en la busqueda de instancias en un servicio de
 * directorio de nombres (Ej: JNDI).
 *
 * AUN SE PUEDE DESACOPLAR MAS
 *
 * Ahora no dependemos en absoluto de RandomIdGenerator, pero nos estamos
 * acoplando al servicio de nombres, hemos cambiado una dependencia por
 * otra.
 *
 * Aun se puede desacoplar más.
 *
 * Configuramos en el contenedor JNDI una factoria de instancias o
 * un singleton, que implementa IdGenerator. Si queremos cambiar
 * una implementación por otra cambiamos la configuración del
 * contenedor de JNDI.
 *
 * ¿Pero y si necesitamos un sistema con dos MemAddressBooks y
 *  cada uno con un IdGenerator diferente?
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class MemAddressBook implements GlobalAddressBook {

    private Map<String, Contact> addressBookMap = new HashMap();

    // Acceso un servicio de generación de ids 
    private IdGenerator idGenerator;

    // Ejemplo de busqueda usando JNDI
    public MemAddressBook() throws NamingException {
       // Buscamos la instacia que quieremos en un servicio de nombres
       Context ctx = new InitialContext();

       // Configuramos en el contenedor JNDI una factoria de instancias o
       // un singleton, que implementa IdGenerator. Si queremos cambiar
       // una implementación por otra cambiamos la configuración del
       // contenedor de JNDI.
       //
       // ¿Pero y si necesitamos un sistema con dos MemAddressBooks y
       //  cada uno con un IdGenerator diferente?
       idGenerator = (IdGenerator)ctx.lookup("java:comp/env/DefaultIdGenerator");
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
}
