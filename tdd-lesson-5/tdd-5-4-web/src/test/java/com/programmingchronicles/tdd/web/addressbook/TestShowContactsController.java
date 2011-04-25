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
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
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
 * Ejemplo de Spy (Mock parcial del Objetct Under Test).<br/>
 * No recomendado para unit testing, sintoma de "code smell", suele indicar
 * un diseño pobre, o que no está diseñado con testing en mente.</p>
 *
 * <p>
 * Pero es util usando librerías de terceros, que no controlamos,
 * como el contenedor de servlets.<p/>
 *
 *   Ver: {@link MockShowContactsController}
 *
 * <p>
 * <b>RESTRICCIONES PARA EL EJERCICIO:</b><br/>
 *   El requestDispatcher normalmente se obtiene a través del request, y no
 *   del servletContext. Para mostrar ejemplos de la tecnica de spying, se
 *   va a restingir a que solo podemos obtener el requestDispatcher a través
 *   de los métodos heredados de HttpServlet.<p/>
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

    // Object Under Test
    private ShowContactsController controller;

    @Before
    public void setUp() {
        mockDispather = mock(RequestDispatcher.class);
        mockContext = mock(ServletContext.class);
        // Spying del Object Under Test:
        // Mock parcial de ShowContactsController para devolver mocks
        // del objectos del contenedor de servlets.
        controller = new MockShowContactsController();

        // IMPORTANTE:
        // Para evitar el spying podemos usar el init(ServletConfig) del
        // servlet, pero al final es casi lo mismo ya que hay que hacer
        // mocks que devuelven mocks, y además tendríamos que saber más de
        // la implementación interna de los servlets.

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
    }

    /**
     *  Spy del Object Under Test.
     *
     *  <p>
     *  Mock parcial de ShowContactsController que devuelve mocks del contenedor
     *  de servlets, como el como el requestDispatcher.</p>
     *
     *  <p>
     *  Codificación Manual de un Spy:<br/>
     *  Se sobreescriben métodos del Object Under Test. Los métodos
     *  getServletConfig y getServletContext deben devolver los mocks
     *  que necesitamos para el test.</p>
     *
     *  <p>
     *  En ejemplos posteriores vemos que esto también se puede realizar
     *  usando mockito.</p>
     */
    protected class MockShowContactsController extends ShowContactsController {
        /**
         * Defecto YAGNI:<br/>
         * ¿Por que nos adelantamos proporcionando dos formas diferentes de
         *  obtener el mock?
         *
         * <p>
         * El código del controlador obtendrá el request dispatcher usando
         * uno de los dos métodos. Haciendo un tests de caja blanca no
         * tenemos que adelantar funcionalidad que puede que no utilicemos.</p>
         */
        @Override
        public ServletConfig getServletConfig() {
            ServletConfig servletConfig = mock(ServletConfig.class);
            ServletContext servletContext = getServletContext();

            when(servletConfig.getServletContext()).thenReturn(servletContext);
            return servletConfig;
        }

        @Override
        public ServletContext getServletContext() {
            // El test debe programar el stub del context ya que es donde se
            // decide la vista que se va a solicitar.
            return mockContext;
        }
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

       // Programa el stub del context para que devuelva el dispatcher
       // cuando se solicita la vista adecuada.
       when(mockContext
               .getRequestDispatcher(eq("/WEB-INF/views/showcontacts.jsp")))
               .thenReturn(mockDispather);

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

       // Minimizar verificaciones redundantes. Sería redundante la siguiente
       // verificación:
          // verify(mockContext, times(1)).getRequestDispatcher(eq("/WEB-INF/views/showcontacts.jsp"));
       // Es una repetición de la programación del stub y por tanto se está
       // probando indirectamente ya que el forward fallaría.
    }
}