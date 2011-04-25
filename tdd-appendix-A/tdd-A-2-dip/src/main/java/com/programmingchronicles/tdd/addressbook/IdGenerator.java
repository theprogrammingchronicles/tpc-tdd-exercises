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

/**
 * Interfaz del servicio de generación de ids.
 *
 * Desacoplando las dependencias entre detalles de implementación usando
 * las interfaces como abstracciones intermedias.
 *
 * Para exponer mejor los beneficios de usar IoC, en esta interfaz especificamos
 * en la semantica o contrato, la condición de que "newId" puede devolver
 * identificadores repetidos.
 *
 * Ver: newId
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public interface IdGenerator {

    /**
     * Devuelve un nuevo identificador único cada vez que se invoca, sin
     * garantizar que no se puedan devolver identificadores ya utilizados.
     *
     * @return Identificador generado, la interfaz no garantiza que los
     *         ids generados sean globalmente únicos, podrían obtenerse
     *         identificadores repetidos.
     */
    public String newId();
}
