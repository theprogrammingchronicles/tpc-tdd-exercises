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

package com.programmingchronicles.tdd.web.addressbook.converters;

import com.programmingchronicles.tdd.web.addressbook.converters.ContactCommandConverter;
import com.programmingchronicles.tdd.domain.Contact;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.junit.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Tests de ContactCommandConverter.
 *
 * <p>
 * Extrae los parametros de un contacto desde un HTTP Request realizando
 * la conversión y la validación.</p>
 *
 * <p>
 * Esta clase también eliminará espacios en los parametros y detectará
 * cadenas vacías.</p>
 *
 * <p>
 * <b>Metodos privados:</b> Se testean de forma indirecta.</p>
 *
 * <p>
 * <b>Refactorización SRP:</b><br/>
 *   Se ha extraído esta responsabilidad del controlador, cuando empezabamos
 *   a codificar la funcionalidad del <i>testDoPostFormError</i>.</p>
 *
 * <p>
 * <b>CREACION DE FRAMEWORKS:</b><br/>
 *   En futuras refactorizaciones podríamos diseñar un mecanismo más
 *   generico para operaciones de conversión y verificación de parametros,
 *   por ejemplo usando reflection para acceder propiedades del javabean.</p>
 *
 * <p>
 *   Está comenzando a aparecer un framework en nuestra arquitectura:<br/>
 *   En TDD nunca nos ponemos por adelantado a diseñar un framework
 *   generico en detalle que se pueda reutilizar en un futuro.</p>
 *
 * <p>
 *   <i>"Se programa para el ahora y nunca para el futuro"</i></p>
 *
 * <p>
 *   Pero en las clases converter empezamos a vislumbrar que mediante
 *   la refactorización iterativa estamos dando los primeros pasos
 *   hacia nuestro propio framework, sin haberlo diseñado por adelantado.</p>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class TestContactCommandConverter {

    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private ContactCommandConverter converter = new ContactCommandConverter();

    /**
     * Verifica que funciona correctamente con un contacto completo.
     *
     * @throws ParseException
     */
    @Test
    public void testConvertSuccessed() throws ParseException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        // Mock del request que devuelve todos los campos del formulario.
        when(request.getParameter("firstName")).thenReturn("Pedro");
        when(request.getParameter("surname")).thenReturn("Ballesteros");
        when(request.getParameter("birthday")).thenReturn("8/1/1974");
        when(request.getParameter("phone")).thenReturn("69696969");

        boolean result = converter.verifyAndConvert(request);
        // Verifica que no falla.
        assertTrue(result);

        // Ningún error
        assertEquals(converter.getErrors(), Collections.emptyMap());

        // Se obtiene el contacto completo
        Contact contact = converter.getCommand();
        assertEquals("Pedro", contact.getFirstName());
        assertEquals("Ballesteros", contact.getSurname());
        assertEquals(dateFormat.parse("8/1/1974"), contact.getBirthday());
        assertEquals("69696969", contact.getPhone());
    }

    /**
     * Requisito de implementación: Decidimos que es mejor que elconversor se
     * encargue también de eliminar espacios al principio y al final, sobre todo
     * para evitar la creación de campos en blanco.
     *
     * @throws ParseException
     */
    @Test
    public void testConvertSuccessedWithBlanks() throws ParseException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("firstName")).thenReturn("   Pedro   ");
        when(request.getParameter("surname")).thenReturn("   Ballesteros   ");
        when(request.getParameter("birthday")).thenReturn("   8/1/1974   ");
        when(request.getParameter("phone")).thenReturn("   69696969   ");

        boolean result = converter.verifyAndConvert(request);
        // Verifica que no falla.
        assertTrue(result);

        // Ningún error
        assertEquals(converter.getErrors(), Collections.emptyMap());

        // Se obtiene un contacto completo y sin espacios en blanco.
        Contact contact = converter.getCommand();
        assertEquals("Pedro", contact.getFirstName());
        assertEquals("Ballesteros", contact.getSurname());
        assertEquals(dateFormat.parse("8/1/1974"), contact.getBirthday());
        assertEquals("69696969", contact.getPhone());
    }

    /**
     * Verifica el comportamiento cuando los parametros no existen.
     *
     * @throws ParseException
     */
    @Test
    public void testConvertNullParams() throws ParseException {
        // Mock que siempre devuelve null en el getParameter.
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter(anyString())).thenReturn(null);

        boolean result = converter.verifyAndConvert(request);
        // Verifica que falla ya que el nombre es obligatorio.
        assertFalse(result);

        // Verifica el mapa de textos de error. El valor de los textos
        // no se debe verificar aquí, el test unitario sería muy fragil.
        // Los textos se deben verificar en tests de UI (tests de sistema).
        Map<String, String> errors = converter.getErrors();
        // FirstName es obligatorio.
        assertNotNull(errors.get("firstName"));
        // El resto son opcionales.
        assertNull(errors.get("surname"));
        assertNull(errors.get("birthday"));
        assertNull(errors.get("phone"));

        // El contacto obtenido debe tener todo a null.
        Contact contact = converter.getCommand();
        assertNull(contact.getFirstName());
        assertNull(contact.getSurname());
        assertNull(contact.getBirthday());
        assertNull(contact.getPhone());
    }

    /**
     * Verifica el comportamiento cuando se entregan parametros en blanco.
     *
     * @throws ParseException
     */
    @Test
    public void testConvertBlankParams() throws ParseException {
        // Mock que siempre devuelve espacios en blanco en el getParameter.
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter(anyString())).thenReturn("     ");

        boolean result = converter.verifyAndConvert(request);
        // Verifica que falla ya que el nombre es obligatorio.
        assertFalse(result);

        // Verifica el mapa de textos de error. El valor de los textos
        // no se debe verificar aquí, el test unitario sería muy fragil.
        // Los textos se deben verificar en tests de UI (tests de sistema).
        Map<String, String> errors = converter.getErrors();
        // FirstName es obligatorio.
        assertNotNull(errors.get("firstName"));
        // El resto son opcionales.
        assertNull(errors.get("surname"));
        // Birthday no debe haber dado un error de conversión ya que
        // cuando es una cadena en blanco o vacío no se intenta convertir.
        assertNull(errors.get("birthday"));
        assertNull(errors.get("phone"));

        // El contacto obtenido debe tener todo null en lugar de
        // cadenas en blanco o vacías.
        Contact contact = converter.getCommand();
        assertNull(contact.getFirstName());
        assertNull(contact.getSurname());
        assertNull(contact.getBirthday());
        assertNull(contact.getPhone());
    }

    /**
     * Test de error de la fecha del cumpleaños.
     */
    @Test
    public void testConvertBirthDayError() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("firstName")).thenReturn("Pedro");
        when(request.getParameter("surname")).thenReturn("Ballesteros");
        when(request.getParameter("phone")).thenReturn("69696969");

        // El campo de la fecha se envía con errores.
        when(request.getParameter("birthday")).thenReturn("fechainvalida");

        boolean result = converter.verifyAndConvert(request);
        // Verifica que falla
        assertFalse(result);

        // Solo birthday contiene errores
        Map<String, String> errors = converter.getErrors();
        assertNotNull(errors.get("birthday"));
        assertNull(errors.get("firstName"));
        assertNull(errors.get("surname"));
        assertNull(errors.get("phone"));

        // Como el birthday era erroneo se queda a null.
        Contact contact = converter.getCommand();
        assertNull(contact.getBirthday());
        assertEquals("Pedro", contact.getFirstName());
        assertEquals("Ballesteros", contact.getSurname());
        assertEquals("69696969", contact.getPhone());
    }
}
