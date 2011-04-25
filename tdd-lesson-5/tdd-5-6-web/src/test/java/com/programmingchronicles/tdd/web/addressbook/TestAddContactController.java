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
import java.util.Date;
import com.programmingchronicles.tdd.addressbook.GlobalAddressBook;
import com.programmingchronicles.tdd.domain.Contact;
import com.programmingchronicles.tdd.testing.web.ServletSpyFactory;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.*;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static com.programmingchronicles.tdd.testing.web.IsContactEqual.*;

/**
 * Tests del controlador para el formulario de añadir contacto.
 *
 * <p><pre>
 *  - Se refactoriza código comun de los tests en la clase ServletSpyFactory,
 *    que podría publicarse en una librería de utilidades de testing, ya que
 *    sirve para tests de servlets.
 *
 *  - Se refactoriza la funcionalidad de validación y verificación de parametros
 *    extrayendola en una clase propia según el principio SRP.
 * </pre></p>
 *
 * <p>
 * <b>NOTAS REFACTORIZACION:</b><br/>
 *   Depues de unas cuantas iteraciones vemos que hay mucho código de testing,
 *   en este caso se ha producido por ir añadiendo más tests sin sustuir o
 *   refinar los tests anteriores.</p>
 *
 * <p>
 *   Sería conveniente revisarlos ya que muchos tests anteriores podrían no
 *   ser ya necesarios. Si hubieramos ido refactorizando más nos habriamos
 *   encontrado con menos código de testing.</p>
 *
 * <p>
 *   Por otro lado la extrancción de la responsabilidad de validar y convertir
 *   parametros también puede reducir los tests necesarios ya que esta funcionalidad
 *   ahora tiene sus propios tests, que solucionan funcionalidad que antes era
 *   responsabilidad del controlador.</p>
 *
 * <p><b>DISEÑO:</b><br/>
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

    // Object Under Tests
    private AddContactController controller;

    @Before
    public void setUp() {
        // El Spy del controlador se obtiene ya preparado desde la
        // clase de utilidad de testing que se ha creado.
        ServletSpyFactory<AddContactController> servletSpy = new ServletSpyFactory(new AddContactController());
        controller = servletSpy.getMockController();
        mockContext = servletSpy.getMockContext();      

        // Mock de GlobalAddressBook
        mockAddressbook = mock(GlobalAddressBook.class);

        // Se configura por Direct Injection el addressbook usado
        // por el controlador.
        controller.setAddressBook(mockAddressbook);

        // Las vistas ahora se configuran en el setup.
        controller.setFormView("formViewPath");
        controller.setSuccessedSubmitRedirect("redirectPath");

        // Se programa el stub del context para devolver el dispatcher
        // solo al invocar la vista adecuada.
        mockDispather = mock(RequestDispatcher.class);
        when(mockContext.getRequestDispatcher(eq("formViewPath"))).thenReturn(mockDispather);
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
     * <p>Uso de <b>argThat</b> en los parámetros del <b>verify</b> de
     * mockito para usar un Hamcrest Custom Matcher.
     */
    @Test
    public void testDoPostAddContact() throws ServletException, IOException, ParseException {
       // Se construyen mocks de los parametros del "doGet", que es el
       // método que se quiere testear.
       HttpServletRequest mockRequest = mock(HttpServletRequest.class);
       HttpServletResponse mockResponse = mock(HttpServletResponse.class);

       // Datos del contacto de ejemplo que se añadirá en el POST.
       Contact expectedContact = new Contact();
       String fomattedBirthday = "8/1/1974";
       expectedContact.setFirstName("Pedro");
       expectedContact.setSurname("Ballesteros");
       Date expectedBirthday = dateFormat.parse(fomattedBirthday);
       expectedContact.setBirthday(expectedBirthday);
       expectedContact.setPhone("610101010");

       // Se codifican respuestas del "getParameter" con los datos del
       // contacto de ejemplo a añadir.
       when(mockRequest.getParameter("firstName")).thenReturn(expectedContact.getFirstName());
       when(mockRequest.getParameter("surname")).thenReturn(expectedContact.getSurname());
       when(mockRequest.getParameter("birthday")).thenReturn(fomattedBirthday);
       when(mockRequest.getParameter("phone")).thenReturn(expectedContact.getPhone());

       // Ejecución del test.
       controller.doPost(mockRequest, mockResponse);

       // Ya no es necesario configurar un ArgumentCaptor.
       //
       // Se puede realizar una verificación por interación usando el
       // IsContactEqual Hamcrest Custom Matcher. El verify comprueba que
       // el método se ha invocado con el argumento esperado.
       verify(mockAddressbook).addContact(argThat(contactNoIdEq(expectedContact)));

       // NOTA:
       //  Las comprobaciones se simplificacan enormemente, sobre todo cuando
       //  se deben comprobar tipos de datos muy extensos ya que las operaciones
       //  de matching se centralizan sin necesidad de tocar el código funcional
       //  implementando un "equals" adaptado a los tests.

       /*
        Versión anterior basasa en ArgumentCaptor:

           ArgumentCaptor<Contact> argument = ArgumentCaptor.forClass(Contact.class);
           verify(mockAddressbook).addContact(argument.capture());

           Contact contact = argument.getValue();
           assertEquals("Pedro", contact.getFirstName());
           assertEquals("Ballesteros", contact.getSurname());
           assertEquals(dateFormat.parse("8/1/1974"), contact.getBirthday());
           assertEquals("69696969", contact.getPhone());
       */

       // Verifica que no se ha intentado crear el modelo de errores
       verify(mockRequest, never()).setAttribute(eq("errors"), any());
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

    /**
     * Test Http Post: Implementa un test con todos los parametros vacíos.
     *
     * <p>
     * <b>Refactorizacion:</b><br/>
     *   Mientras implementamos la funcionalidad que hace pasar a este test,
     *   nos damos cuenta que se puede aplicar el principio SRP:<br/>
     *   La responsabilidad de validar y convertir los parametros se puede
     *   extraer del controlador.</p>
     *
     * <p>
     * Dejamos pendiente este test y su funcionalidad y nos ponemos
     * con la refactorización, que implica nuevas clases y por tanto
     * los tests de dichas clases.</p>
     *
     * <p>
     * <b>NOTA:</b> Este es el estado del test despues de varias iteraciones, en
     *              este caso no se han ido añadiendo mas tests, si no que se ha
     *              ido actualizando este.</p>
     *
     * @throws ServletException
     * @throws IOException
     */
    @Test
    public void testDoPostNullParams() throws ServletException, IOException {
       // Mock del Response
       HttpServletResponse mockResponse = mock(HttpServletResponse.class);

       // Mock del Request que devuelve null para todos los parametros.
       HttpServletRequest mockRequest = mock(HttpServletRequest.class);
       when(mockRequest.getParameter(anyString())).thenReturn(null);

       // Ejecución del test.
       controller.doPost(mockRequest, mockResponse);

       // Verifica que no se ha intentado añadir el contacto ya que
       // el nombre es obligatorio.
       verify(mockAddressbook, never()).addContact(any(Contact.class));

       // Verifica que se ha creado un modelo con los errores con un
       // setAtribute. Decidimos que se introducirán en un Map<String, String>.
       ArgumentCaptor<Map> argument = ArgumentCaptor.forClass(Map.class);
       verify(mockRequest).setAttribute(eq("errors"), argument.capture());

       // En el verify capturamos los parametros del setAttribute que el
       // controlador usa sobre el response, para comprobar que se
       // construye bien el modelo de los errores.
       Map<String,String> errors = argument.getValue();

       // REFACTORIZACION:
       //   Despues de la refactorizacion SRP que hemos realizado creando
       //   el ContactConvertConverter (que tiene sus propios tests), ya no
       //   sería tan importante comprobar aqui los errores, con verificar
       //   que el modelo se introduce en el request podría ser suficiente.
       assertTrue(errors.containsKey("firstName"));
       assertFalse(errors.containsKey("surname"));
       assertFalse(errors.containsKey("birthday"));
       assertFalse(errors.containsKey("phone"));

       // IMPORTANTE:
       //   No se han comprobado los textos de error, en un test unitario no se
       //   deben probar la IU. Dichas pruebas estarían en tests de IU (test de sistema).

       // En caso de error la petición se debe dirigir a la vista del formulario
       // de nuevo, que ahora mostrará los campos de entrda erroneos.
       verify(mockDispather, times(1)).forward(mockRequest, mockResponse);
    }

    /**
     * Test Http Post: Verifica que se puede añadir un contacto con
     * solo el nombre.
     *
     * @throws ServletException
     * @throws IOException
     */
    @Test
    public void testDoPostOnlyName() throws ServletException, IOException {
       // Mock del Response
       HttpServletResponse mockResponse = mock(HttpServletResponse.class);

       // Mock del Request que devuelve null para todos los parametros
       // excepto para el nombre.
       HttpServletRequest mockRequest = mock(HttpServletRequest.class);
       when(mockRequest.getParameter("firstName")).thenReturn("Pedro");

       // Ejecución del test.
       controller.doPost(mockRequest, mockResponse);

       // Verifica que se llama al addContact del mock del addressbook
       ArgumentCaptor<Contact> argument = ArgumentCaptor.forClass(Contact.class);
       verify(mockAddressbook).addContact(argument.capture());

       // En el verify anterior capturamos el argumento con el que se llama
       // a la agenda, y podemos validar que se añade el contacto correcto.
       Contact contact = argument.getValue();
       assertEquals("Pedro", contact.getFirstName());
       assertNull(contact.getSurname());
       assertNull(contact.getBirthday());
       assertNull(contact.getPhone());

       // Verifica que no se ha creado el modelo de errores.
       verify(mockRequest, never()).setAttribute(eq("errors"), any());

       // Verifica que se llama al redirect del response con el parametro correcto.
       verify(mockResponse).sendRedirect(eq("redirectPath"));
    }
}