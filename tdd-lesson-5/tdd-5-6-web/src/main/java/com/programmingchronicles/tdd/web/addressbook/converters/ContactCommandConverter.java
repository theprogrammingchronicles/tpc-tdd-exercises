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
 * <p>
 * <b>NOTA:</b> Este código representa el estado de la clase despues de varias
 *              iteraciones.</b>
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
public class ContactCommandConverter {
     // Las fechas se deben entregar en este formato
     private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

     private Contact contact = new Contact();
     private Map<String, String> errors = new HashMap();

    /**
     * Realiza la conversión de los parametros de un request en
     * un Contact, actualizando los errores y el contact del converter.
     *
     * <p>
     * Si un campo es erroneo el resto se siguen comprobando.</p>
     *
     * @param request
     * @param errors
     * @return true si la conversión no ha dado errores.
     */
    public boolean verifyAndConvert(HttpServletRequest request) {
        boolean result = true;        

        result &= convertFirstName(request);
        result &= convertBirthday(request);
        result &= convertPhone(request);
        result &= convertSurname(request);
        return result;        
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

    private boolean convertFirstName(HttpServletRequest request) {
        String firstName = request.getParameter("firstName");
        
        // Se eliminan espacios en blanco, considerandolo null si 
        // al final es una cadena vacía.
        if(firstName != null) {
           firstName = firstName.trim();
           if(firstName.length() == 0) {
              firstName = null;
           }
        }

        // Comprueba que es un nombre válido ya que es obligatorio.
        if(firstName == null) {
           // Habría que tener una internacionalización de textos
           errors.put("firstName", "El nombre es obligatorio.");
           return false;
        }

        contact.setFirstName(firstName);
        return true;
    }

    private boolean convertSurname(HttpServletRequest request) {
        String surname = request.getParameter("surname");

        // Se eliminan espacios en blanco, considerandolo null si
        // al final es una cadena vacía.
        if(surname != null) {
           surname = surname.trim();
           if(surname.length() == 0) {
              surname = null;
           }
        }
        contact.setSurname(surname);
        return true;
    }

    private boolean convertPhone(HttpServletRequest request) {
        String phone = request.getParameter("phone");

        // Se eliminan espacios en blanco, considerandolo null si
        // al final es una cadena vacía.
        if(phone != null) {
           phone = phone.trim();
           if(phone.length() == 0) {
              phone = null;
           }
        }
        contact.setPhone(phone);
        return true;
    }

    private boolean convertBirthday(HttpServletRequest request) {
        String birthday = request.getParameter("birthday");

        // Se eliminan espacios en blanco, considerandolo null si
        // al final es una cadena vacía.
        if(birthday != null) {
           birthday = birthday.trim();
           if(birthday.length() == 0) {
              birthday = null;
           }
        }

        // Si el contacto no es null, ni blancos, ni cadena vacía, se
        // intenta la conversión.
        if(birthday != null) {
            try {
               contact.setBirthday(dateFormat.parse(birthday));
            } catch(ParseException ex) {
               errors.put("birthday", "El formato de la fecha debe ser dd/mm/yyyy");
               return false;
            }
        }
        return true;
    }
}
