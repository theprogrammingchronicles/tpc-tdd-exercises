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

package com.programmingchronicles.addressbook.converters;

import com.programmingchronicles.tdd.domain.Contact;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * Extrae los parametros de un contacto desde un HTTP Request realizando
 * la conversión y la validación.
 *
 * Esta clase también eliminará espacios en los parametros y detectará
 * cadenas vacías.
 *
 *  - Metodos privados: Se testean de forma indirecta.
 *
 * CREACION DE FRAMEWORKS:
 *   En futuras refactorizaciones podríamos diseñar un mecanismo más
 *   generico para operaciones de conversión y verificación de parametros,
 *   por ejemplo usando reflexion para usar propiedades del javabean.
 *
 *   Está comenzando a aparecer un framework en nuestra arquitectura:
 *   En TDD nunca nos ponemos por adelantado a diseñar un framework
 *   generico que se pueda reutilizar en un futuro.
 *
 *   "Se programa para el ahora y nunca para el futuro"
 *
 *   Pero en las clases Converter empezamos a vislumbrar que mediante
 *   la refactorización iterativa estamos dando los primeros pasos
 *   hacia nuestro propio framework, sin haberlo diseñado por adelantado.
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class ContactCommandConverter {
     // Las fechas se deben entregar en este formato
     private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

     // Se centralizan los textos de error que habría que extraer a un
     // sistema de internacionalización.
     private static String NOTFOUND_FIRSTNAME = "El nombre es obligatorio.";
     private static String CONVERT_BIRTHDAY =  "El formato de la fecha debe ser dd/mm/yyyy";

     private Contact contact = new Contact();
     private Map<String, String> errors = new HashMap();

    /**
     * Realiza la conversión de los parametros de un request en
     * un Contact, actualizando los errores y el contact del converter.
     * Si un campo es erroneo el resto se siguen comprobando.
     *
     * NOTA: Usando reflection o una utilidad de acceso a propiedades
     * JavaBean se podría incluso simplificar más.
     *
     * @param request
     * @param errors
     * @return true si la conversión no ha dado errores.
     */
    public boolean verifyAndConvert(HttpServletRequest request) {

        convertFirstName(request);
        convertBirthday(request);
        contact.setSurname(convertParam(request, "surname"));
        contact.setPhone(convertParam(request, "phone"));

        // Si la colección de errores está vacía se devuelve true.
        return errors.isEmpty();
    }

    // Devuelve el parametro entregado eliminado los espacios en blanco
    // y devolviendo null si es cadena vacía o solo blancos.
    private String convertParam(HttpServletRequest request, String paramName) {
        String value = request.getParameter(paramName);

        // Se eliminan espacios en blanco y devuelve null si el parametro
        // está en blanco.
        if(value != null) {
           value = value.trim();
           if(value.length() == 0) {
              value = null;
           }
        }
        return value;
    }

    // Realiza la conversión y actualización del nombre, actualizando
    // los posibles errores.
    private void convertFirstName(HttpServletRequest request) {
        String firstName = convertParam(request, "firstName");
        contact.setFirstName(firstName);

        // El nombre es obligatorio.
        if(firstName == null) {
           errors.put("firstName", NOTFOUND_FIRSTNAME);
        }
    }

     // Realiza la conversión y actualización del cumpleaños, actualizando
     // los posibles errores.
     private void convertBirthday(HttpServletRequest request) {
        String birthday = convertParam(request, "birthday");

        // Solo se intenta convertir si no era null o vacío.
        if(birthday != null) {
            try {
               contact.setBirthday(dateFormat.parse(birthday));
            } catch(ParseException ex) {
               errors.put("birthday", CONVERT_BIRTHDAY);
            }
        }
    }

    /**
     * Devuelve el Contact relleno con los campos que se han podido convertir.
     *
     * @return
     */
    public Contact getCommand() {
        return contact;
    }

    /**
     * Devuelve un Map con el nombre de las propiedades de un contact
     * mapeadas en textos de error.
     *
     */
    public Map<String, String> getErrors() {
        return errors;
    }
}
