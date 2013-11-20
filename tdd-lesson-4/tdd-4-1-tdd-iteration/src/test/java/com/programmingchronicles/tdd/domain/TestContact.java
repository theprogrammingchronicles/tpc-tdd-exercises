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

package com.programmingchronicles.tdd.domain;

import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests para la clase Contact. Se completan el resto de las propiedades
 * del contacto.
 *
 * <p>
 * Este ejemplo muestra que para no romper la encapsulación "private"
 * de las propiedades basadas en atributo, se deben probar todas en un
 * solo método de test. </p>
 *
 * <p>
 * El método {@link #testId() } muestra un test sin errores, bien implementado,
 * pero que no es capaz de detectar el fallo existente en esta propiedad.</p>
 *
 * <p>
 * <b>PERO:</b><br/>
 *    ¿Es necesario crear tests para los getters y setters de un JavaBean o
 *     de métodos tan sencillos como estos?<br/><br/>
 *    En este mismo proyecto se puede observar como la implementación erronea
 *    de este tipo de objetos hace saltar rápidamente los tests de las clases
 *    que usan los objetos del dominio de datos.</p>
 *
 * <p>
 * <b>RESPUESTA:</b><br/>
 *    Depende de...</p>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class TestContact {

    /**
     * Test de la propiedad id.
     *
     * <p>
     * Este test está dando ok, además no tiene errores, esta bien
     * implementado, pero a un así no detecta que la propiedad
     * id tiene realmente errores de implementación.</p>
     */
    @Test
    public void testId() {
        Contact contact = new Contact();

        contact.setId("ID");

        // INTERROGANTE:
        // ¿Sería correcto probar un setter mediante su getter?
        // ¿Y cuando el JavaBean tiene más de una propiedad?
        assertEquals("ID", contact.getId());
    }

    @Test
    public void testFirstName() {
        Contact contact = new Contact();
        contact.setFirstName("Pedro");

        assertEquals("Pedro", contact.getFirstName());
    }

    /**
     * Test de todos los setters y getters de las propiedades.
     *
     * <p>
     * El problema anterior se soluciona probando todas las propiedades
     * en un mismo test, para no tener que acceder a atributos privados.</p>
     *
     * <p>
     * En este caso no es útil la propiedad de independencia de FIRST.</p>
     */
    @Test
    public void testAllProperties() {
        // Inicialización del test object
        Contact contact = new Contact();
        contact.setId("id");
        contact.setFirstName("firstName");
        contact.setSurname("surname");
        contact.setPhone("phone");
        Date expectedDate = new Date();
        contact.setBirthday(expectedDate);


        // Aunque no conviente tener mas de un assert en cada test, en este
        // caso es necesario.

        // Es la solución al fallo no detectado en "testId"

        // No tenemos acceso a los atributos, por ser private, y no queremos
        // romper la encapsulación que proporciona OOP.

        // Se establecen todas las propiedades en la misma instancia y se prueban
        // que tienen el valor correcto usando los getters.
        assertEquals("id", contact.getId());
        assertEquals("firstName", contact.getFirstName());
        assertEquals("surname", contact.getSurname());
        assertEquals("phone", contact.getPhone());
        assertEquals(expectedDate, contact.getBirthday());
    }
}