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

import com.programmingchronicles.tdd.web.addressbook.AddContactController;
import com.programmingchronicles.tdd.addressbook.GlobalAddressBook;
import com.programmingchronicles.tdd.domain.Contact;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.*;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Tests del controlador para el formulario de añadir contacto.
 *
 * <p>
 *   <b>Spy</b> con "mockito": Mock Parcial del Object Under Test (permite
 *   evitar la codificación de MockAddContactsController.</p>
 *
 * <p>
 * <b>RECORDATORIO:</b><br/>
 *     El Spy no se recomienda en unit testing, sintoma de "code smell", suele indicar
 *     un diseño pobre, o que no está diseñado con testing en mente.</p>
 *
 *     <p>
 *     Pero es util usando librerías de terceros, que no controlamos,
 *     como el contenedor de servlets. </p>
 *
 *     <p>
 *     Ejemplo de como la codificación de un nuevo test y la nueva
 *     funcionalidad del Object Under Test produce el fallo de tests
 *     anteriores, se hace evidente la codificación de más funcionalidad.
 *     {@link #testDoPostGetParameters() }, {@link #testDoPostAddContact() }. </p>
 *
 * <p>
 * <b>RESTRICCIONES PARA EL EJERCICIO:</b><br/>
 *   El requestDispatcher normalmente se obtiene a través del request, y no
 *   del servletContext. Para mostrar ejemplos de la tecnica de spying, se
 *   va a restingir a que solo podemos obtener el requestDispatcher a través
 *   de los métodos heredados de HttpServlet.</p>
 *
 * <p>
 * <b>DISEÑO:</b><br/>
 * Se usa un patrón clásico de procesamiento de formularios
 * <ul>
 *    <li>El HTTP GET devuelve el formulario.
 *    <li>El HTTP POST procesa el formulario.
 *       <ul>
 *          <li> Si OK devuelve un redirect al navegador, evitando el doble POST.
 *          <li> Si Error devuelve de nuevo el formulario indicando los campos erroneos.
 *       </ul>
 * </ul>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class TestAddContactController {

    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    // Mocks
    private GlobalAddressBook mockAddressbook;

    // Servlet Mocks
    private RequestDispatcher mockDispather;
    private ServletContext mockContext;

    // Object Under Test
    private AddContactController controller;

    @Before
    public void setUp() {
        mockDispather = mock(RequestDispatcher.class);
        mockContext = mock(ServletContext.class);          

        // Spying del Object Under Test: Usando "mockito"
        // Mock parcial de ShowContactsController para devolver mocks
        // del objectos del contenedor de servlets.
        controller = spy(new AddContactController());
        doReturn(mockContext).when(controller).getServletContext();        

        // Mock de GlobalAddressBook
        mockAddressbook = mock(GlobalAddressBook.class);

        // Se configura por Direct Injection el addressbook usado
        // por el controlador.
        controller.setAddressBook(mockAddressbook);
    }

    /**
     * Test del HTTP Get que muestra el formulario.
     *
     * @throws ServletException
     * @throws IOException
     */
    @Test
    public void testDoGetShowForm() throws ServletException, IOException {
       // Se construyen mocks de los parametros del "doGet", que es el
       // método que se quiere testear.
       HttpServletRequest mockRequest = mock(HttpServletRequest.class);
       HttpServletResponse mockResponse = mock(HttpServletResponse.class);

       // En este controlador decidimos que es mejor dejar configurables
       // las vistas (luego se refactorizará el ShowContactsController).
       controller.setFormView("formView");

       // Se programa el stub del context para que devuelva el dispatcher para
       // la vista que se debe invocar. 
       when(mockContext.getRequestDispatcher(eq("formView"))).thenReturn(mockDispather);

       // Ejecución del test.
       controller.doGet(mockRequest, mockResponse);

       // Verificación por interacción de que el servlet hace un forward a la
       // vista que muestra los contactos obtenidos.  
       verify(mockDispather, times(1)).forward(mockRequest, mockResponse);
    }

    /**
     * Test de Http Post: Verifica que es capaz de obtener los parametros
     * del contacto que se quiere añadir.
     *
     * @throws ServletException
     * @throws IOException
     */
    @Test
    public void testDoPostGetParameters() throws ServletException, IOException {
       // Se construyen mocks de los parametros del "doGet", que es el
       // método que se quiere testear.
       HttpServletRequest mockRequest = mock(HttpServletRequest.class);
       HttpServletResponse mockResponse = mock(HttpServletResponse.class);

       // Ejecución del test.
       controller.doPost(mockRequest, mockResponse);

       // Verifica por interacción que se extraen los parametros del nuevo
       // contacto usando el nombre correcto en el "getParameter"
       verify(mockRequest, times(1)).getParameter(eq("firstName"));
       verify(mockRequest, times(1)).getParameter(eq("surname"));
       verify(mockRequest, times(1)).getParameter(eq("birthday"));
       verify(mockRequest, times(1)).getParameter(eq("phone"));
    }

    /**
     * Test de HttpPost: Verifica que el contacto se añade a la agenda
     * usando el servicio de address book.
     *
     * <p>
     * Ejemplo de Broken Tests:<br/>
     *   Ejemplo de como la codificación de la funcionalidad que requiere
     *   este test, provoca que comience a fallar el test anterior. Esto
     *   es un indicador de que necesitamos funcionalidad de validación
     *   de los parametros entregados en el form, informando al usuario.
     * </p>
     *
     * <p>
     *   Si no se entrega el birthday, el código implementado lanza una
     *   excepción, pero eso no lo prueba este test, si no {@link #testDoPostGetParameters() }.
     * </p>
     *
     * @throws ServletException
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testDoPostAddContact() throws ServletException, IOException, ParseException {
       // Se construyen mocks de los parametros del "doGet", que es el
       // método que se quiere testear.
       HttpServletRequest mockRequest = mock(HttpServletRequest.class);
       HttpServletResponse mockResponse = mock(HttpServletResponse.class);

       // Se codifican respuestas del "getParameter" con los datos del
       // contacto de ejemplo a añadir.
       when(mockRequest.getParameter("firstName")).thenReturn("Pedro");
       when(mockRequest.getParameter("surname")).thenReturn("Ballesteros");
       when(mockRequest.getParameter("birthday")).thenReturn("8/1/1974");
       when(mockRequest.getParameter("phone")).thenReturn("69696969");

       // Ejecución del test.
       controller.doPost(mockRequest, mockResponse);

       // Verifica que se llama al addContact del mock del addressbook
       ArgumentCaptor<Contact> argument = ArgumentCaptor.forClass(Contact.class);
       verify(mockAddressbook).addContact(argument.capture());

       // En el verify anterior capturamos el argumento con el que se llama
       // a la agenda, y podemos validar que se añade el contacto correcto.
       Contact contact = argument.getValue();    
       assertEquals("Pedro", contact.getFirstName());
       assertEquals("Ballesteros", contact.getSurname());
       assertEquals(dateFormat.parse("8/1/1974"), contact.getBirthday());
       assertEquals("69696969", contact.getPhone());
    }

    /**
     * Test de HttpPost: Verifica que se envía el redirect al navegador,
     * cuando añade un contacto nuevo (evitamos el doble POST).
     *
     * <p>
     * <b>Pregunta:</b><br/>
     *    ¿seguro que "testDoPostAddContact" y "testDoPostAddContactRedirect"
     *     deben codificarse por separado?
     *    ¿no sería mejor comprobar el flujo completo en testDoPostAddContact?
     * </p>
     *
     * @throws ServletException
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testDoPostAddContactRedirect() throws ServletException, IOException, ParseException {
       // Se construyen mocks de los parametros del "doGet", que es el
       // método que se quiere testear.
       HttpServletRequest mockRequest = mock(HttpServletRequest.class);
       HttpServletResponse mockResponse = mock(HttpServletResponse.class);

       // Ahora se necesita y se puede configurar el path del redirect.
       controller.setSuccessedSubmitRedirect("redirectPath");

       // Se codifican respuestas del "getParameter" con los datos del
       // contacto de ejemplo a añadir.
       when(mockRequest.getParameter("firstName")).thenReturn("Pedro");
       when(mockRequest.getParameter("surname")).thenReturn("Ballesteros");
       when(mockRequest.getParameter("birthday")).thenReturn("8/1/1974");
       when(mockRequest.getParameter("phone")).thenReturn("69696969");

       // Ejecución del test.
       controller.doPost(mockRequest, mockResponse);

       // Verifica que se llama al redirect del response con el parametro correcto.
       verify(mockResponse).sendRedirect(eq("redirectPath"));
    }
}