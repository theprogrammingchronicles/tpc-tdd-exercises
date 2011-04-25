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
import com.programmingchronicles.tdd.addressbook.GlobalAddressBook;
import com.programmingchronicles.tdd.domain.Contact;
import com.programmingchronicles.tdd.testing.web.ServletSpyFactory;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.*;
import static org.mockito.Mockito.*;

/**
 * Tests del controlador de obtención de contactos.
 *
 * <p>
 *   Se refactoriza código comun de los tests en la clase ServletSpyFactory,
 *   que podría publicarse en una librería de utilidades de testing, ya que
 *   sirve para tests de servlets.</p>
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

    // Mocks
    private GlobalAddressBook mockAddressbook;
    private List<Contact> contactList;

    // Servlet Mocks
    private RequestDispatcher mockDispather;
    private ServletContext mockContext;

    // Object Under Tests
    private ShowContactsController controller;

    @Before
    public void setUp() {       
        // El Spy del controlador se obtiene ya preparado desde la
        // clase de utilidad de testing que se ha creado.
        ServletSpyFactory<ShowContactsController> servletSpy = new ServletSpyFactory(new ShowContactsController());
        controller = servletSpy.getMockController();
        mockContext = servletSpy.getMockContext();

        // Fixtures: Contactos de ejemplo.
        Contact c1 = new Contact();
        c1.setId("Id1");
        Contact c2 = new Contact();
        c2.setId("Id2");

        // Lista lista de contactos de ejemplo que devuelve la agenda.
        contactList = Arrays.asList(c1, c2);

        // Mock de GlobalAddressBook que devuelve los contactos de ejemplo.
        mockAddressbook = mock(GlobalAddressBook.class);
        when(mockAddressbook.getAll()).thenReturn(contactList);

        // Se configura por Direct Injection el addressbook usado
        // por el controlador.
        controller.setAddressBook(mockAddressbook);

        // La vista ahora es configurable
        controller.setViewPath("viewPath");

        // Se programa el stub del context para devolver el dispatcher
        // solo al invocar la vista adecuada.
        mockDispather = mock(RequestDispatcher.class);
        when(mockContext.getRequestDispatcher(eq("viewPath"))).thenReturn(mockDispather);
    }

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
       verify(mockRequest, times(1)).setAttribute(eq("contacts"), eq(contactList));

       // Verificación por interacción de que el servlet hace un forward a la
       // vista que muestra los contactos obtenidos.
       verify(mockDispather, times(1)).forward(mockRequest, mockResponse);
    }
}