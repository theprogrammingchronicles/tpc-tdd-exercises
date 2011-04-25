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

package com.programmingchronicles.tdd.web.addressbook;

import com.programmingchronicles.tdd.web.addressbook.ShowContactsController;
import com.programmingchronicles.tdd.domain.Contact;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.*;
import static org.mockito.Mockito.*;

/**
 * Tests del controlador de obtención de contactos.
 *
 * Este ejemplo aun falla ya que el controlador no tiene acceso
 * al servicio GlobalAddressBook.
 *
 * <p>
 * <b>DISEÑO:</b> Arquitectura MVC usando tan sólo tecnologías del J2EE:<br/>
 *     <li>- Modelo: JavaBeans (Java-Old Plain Objects).
 *     <li>- Controlador: Un Servlet para cada petición web.
 *     <li>- Vista: JSP (sin nada de lógica, solo JSTL + EL).
 * </p>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class TestShowContactsController {

    /**
     * Test del método doGet, que procesa la petición.
     *
     * <p>
     * Al ser una arquitectura MVC, lo unico que hay que testear es:<br/>
     * <ol>
     *    <li> El controlador recibe los parametros.
     *    <li> Construye el modelo de forma adecuada.
     *    <li> Se redirige la petición a la vista adecuada.
     * </ol>
     * </p>
     * <p>
     * El controlador se implementará como un Servlet, y la logica se
     * procesará en un HTTP GET.</p>
     */
    @Test
    public void testDoGet() throws ServletException, IOException {

       // Object Under Test
       ShowContactsController controller = new ShowContactsController();

       // Se construyen mocks de los parametros del "doGet", que es el
       // método que se quiere testear.
       HttpServletRequest mockRequest = mock(HttpServletRequest.class);
       HttpServletResponse mockResponse = mock(HttpServletResponse.class);

       // Ejecución del test.
       controller.doGet(mockRequest, mockResponse);

       // Diseño de nuestra arquitectura MVC:
       //   El servlet (controlador) construye el modelo y lo hace
       //   accesible a la vista, que será una JSP.
       //
       //   El modelo se crea como atributos del request (request.setAttribute).
       //
       //   En este caso aprobechamos los domain objects del sistema. Otros
       //   controladores podrían definir modelos propios. Las necesidades
       //   particulares de la logica de presentación no deben influir en la
       //   lógica de negocio. El modelo es lógica de presentación, no de
       //   negocio.

       // White Box Tests:
       // Se verifica que el controlador llama a setAttributes con el modelo
       // que será usado por las vistas (verificación por interacción).

       // Por ahora no nos preocupamos por el nombre del modelo, se comprueba
       // que se ha añadido una lista de Contacts.
       verify(mockRequest, times(1)).setAttribute(anyString(), anyListOf(Contact.class));
    }
}