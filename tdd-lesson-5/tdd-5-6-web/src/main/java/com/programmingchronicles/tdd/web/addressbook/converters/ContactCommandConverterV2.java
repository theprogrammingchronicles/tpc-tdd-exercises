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
 * <b>REFACTORIZANDO</b>: Refactorización de ContactCommandConverter</p>
 *
 * <p>
 * Vemos que todos los metodos privados de ContactCommandConverter tienen
 * practicamente lo mismo, se ha usado mucho copy & paste, seguro que se
 * puede eliminar código repetido.</p>
 *
 * <p>
 * Esta clase es un ejemplo de refactorización de ContactCommandConverter, se
 * deja en una clase aparte para que en el ejemplo se puedan comparar.</p>
 *
 * <p>
 * Se ha realizado una refactorización muy leve, se podría eliminar más
 * codigo repetido con otras tecnicas java.</p>
 *
 * @see ContactCommandConverter
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class ContactCommandConverterV2 {
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
     *
     * <p>
     * Si un campo es erroneo el resto se siguen comprobando.</p>
     *
     * <p>
     * <b>NOTA:</b> Usando reflection o una utilidad de acceso a propiedades
     * JavaBean se podría incluso simplificar más.</p>
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
     * @return
     */
    public Map<String, String> getErrors() {
        return errors;
    }
}
