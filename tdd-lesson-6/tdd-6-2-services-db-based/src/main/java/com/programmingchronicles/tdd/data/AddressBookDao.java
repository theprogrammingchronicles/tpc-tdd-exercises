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

package com.programmingchronicles.tdd.data;

import com.programmingchronicles.tdd.domain.Contact;
import java.util.List;

/**
 * Interfaz del AddressBook DAO para la manipulación de contactos en
 * una base de datos.
 *
 * <p>
 * La implementación dará un acceso directo a los datos sin depender
 * de restricciones de integridad, comprobación de repetidos, etc.</p>
 *
 * <p>
 * Esta es una decisión de diseño marcada por las necesidades de tutoría de
 * los ejercicios, no extensible a cualquier DAO. Se quiere mantener algo de
 * lógica en la capa de negocio para mostrar ejemplos TDD.</p>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public interface AddressBookDao {

    /**
     * Añade un nuevo contacto devolviendo el id generado.
     *
     * @param contact Datos del contacto a añadir
     * @return Devuelve el id asignado al contacto
     */
    String addContact(Contact contact);

    /**
     * Elimina el contacto con el id entregado.
     *
     * @param id
     */
    void deleteContact(String id);

    /**
     * Obtiene todos los contactos del sistema.
     *
     * @return
     */
    List<Contact> getAll();

    /**
     * Obtiene el contacto asociado al id entregado.
     *
     * @param contactId id del contacto
     * @return Devuelve el contacto
     */
    Contact getContact(String contactId);
}
