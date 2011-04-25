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
import java.util.List;

/**
 * Interfaz del servicio GlobalAddressBook
 *
 * Desacoplando las dependencias entre detalles de implementación usando
 * las interfaces como abstracciones intermedias.
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public interface GlobalAddressBook {

    /**
     * Añade un nuevo contacto devolviendo el id generado.
     *
     * @param contact Datos del contacto a añadir
     * @return Devuelve el id asignado al contacto
     */
    String addContact(Contact contact);

    /**
     * Obtiene el contacto asociado al id entregado.
     *
     * @param contactId id del contacto
     * @return Devuelve el contacto
     */
    Contact getContact(String contactId);

     /**
     * Obtiene todos los contactos del sistema.
     *
     * @return
     */
    public List<Contact> getAll();
}

