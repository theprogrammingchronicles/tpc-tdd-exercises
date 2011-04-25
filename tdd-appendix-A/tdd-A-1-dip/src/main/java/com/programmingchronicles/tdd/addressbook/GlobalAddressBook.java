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
import java.util.HashMap;
import java.util.Map;

/**
 * Implementación de un servicio Agenda de Contactos Global basado en el
 * almacenamiento en memoria.
 *
 * NOTA: Ver "addContact"
 * Como estamos usando un generador de ids que puede proporcionar ids
 * repetidos vamos a poner un código que se recupere de esta problemática.
 *
 * VIOLACIÓN DE DEPENDENCY INVERSION PRINCIPLE
 *   - La clase GlobalAddressBook depende directamente de las clases de
 *     bajo nivel (dependencias) --> RandomIdGenerator.
 *
 *   - Depende directamente de los detalles, con lo que se dificulta la
 *     tarea de ¿qué pasa si queremos cambiar RandomIdGenerator por una
 *     implementación basada en ficheros o base de datos?.
 *
 *   - Es una clase difícil de testear. Imaginemos que RandomIdGenerator
 *     es una clase muy complicada llena de funcionalidad, por tanto nos
 *     interesa poder probar GlobalAddressBook sin su dependencia real,
 *     probarla de forma aislada para no complicar el descubrimiento de
 *     bugs en esta clase. RandomIdGenerator ya tiene sus propios tests
 *     y sabemos que es válida, pero aquí queremos validar única y
 *     exclusivamente GlobalAddressBook, y no queremos que futuros fallos en
 *     RandomIdGenerator nos dificulten los test de esta clase.
 *     Ver: TestGlobalAddressBook
 *
 *   - Tenemos que cambiar GlobalAddressBook ("que es muy compleja y mantenida
 *     por varios desarrolladores"). La funcionalidad actual podría verse
 *     afectada por ese cambio. Probablemente tengamos que rehacer los tests.
 *
 * Deberíamos hacer que GlobalAddressBook dependa de una abstracción es decir,
 * de una interfaz, o abstracción equivalente en otros lenguajes.
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class GlobalAddressBook {

    private Map<String, Contact> addressBookMap = new HashMap();

    // Acceso un servicio de generación de ids aleatorios
    private RandomIdGenerator idGenerator = new RandomIdGenerator();
   
    /**
     * Añade un nuevo contacto devolviendo el id generado.
     *
     * @param contact Datos del contacto a añadir
     * @return Devuelve el id asignado al contacto
     */
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
    public Contact getContact(String contactId) {
        Contact result = addressBookMap.get(contactId);
        if(result == null) {
           throw new InvalidIdException();
        } 
        return result;
    }   
}
