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
import com.programmingchronicles.tdd.testing.web.FakeHttpServletRequest;
import com.programmingchronicles.tdd.testing.web.FakeHttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import javax.servlet.ServletException;
import org.junit.*;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Tests del controlador para el formulario de añadir contacto.
 *
 * <p>
 * Esta implementacion usa los nuevos Fakes de HttpServletRequest y
 * HttpServletResponse que a su vez permiten verificar por estado
 * las redirecciones y los forwards.</p>
 *
 * <p>
 * Ahora el código de los tests disminuye y se hace más claro, al poder
 * realizar validación por estado.</p>
 *
 * <p>
 * <b>Refactorización:</b> También se han eliminado algunos tests y se han
 * agrupado otros que no eran necesarios y que tan solo estaban
 * por que en lugar de modificarlos se ha ido usando mucho copy & paste.</p>
 *
 * <p>
 *  <b>Ej:</b> testDoPostGetParameters ya no es necesario solo hacia tests de
 *             interacción y sus tests ya quedan verificados en otros tests.</p>
 *
 * <p>
 * <b>NOTAS RESTRICCIONES:</b><br/>
 *   En este ejemplo ya empezamos a usar el request para obtener
 *   el dispatcher. Que era una restriccion que se había impuesto
 *   solo para mostrar ejemplos de Spy del Object Under Test.</p>
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

    // Object Under Tests
    private AddContactController controller;

    @Before
    public void setUp() {
        // Ya no construimos el Spy del Controlador, en este ejercicio se ha
        // eliminado la restricción que se había impuesto, y podemos
        // usar el request para obtener el requestDispatcher.
        controller = new AddContactController();        

        // Mock de GlobalAddressBook
        mockAddressbook = mock(GlobalAddressBook.class);

        // Se configura por Direct Injection el addressbook usado
        // por el controlador.
        controller.setAddressBook(mockAddressbook);

        // Las vistas ahora se configuran en el setup.
        controller.setFormView("formViewPath");
        controller.setSuccessedSubmitRedirect("redirectPath");
    }

    /**
     * Test del HTTP Get que muestra el formulario.
     *
     * @throws ServletException
     * @throws IOException
     */
    @Test
    public void testDoGetShowForm() throws ServletException, IOException {
       // Fakes de los parametros del doGet y doPost que amplian la interfaz
       // estandar permitiendo verificaciones sencillas por estado.
       FakeHttpServletRequest fakeRequest = new FakeHttpServletRequest();
       FakeHttpServletResponse fakeResponse = new FakeHttpServletResponse();

       // Ejecución del test.
       controller.doGet(fakeRequest, fakeResponse);

       // Verifica que el forward se realiza a la vista correcta, usando
       // las facilidades del Fake que permiten obtener el path del forward,
       // en lugar de realizar una validación por interacción.
       assertEquals("formViewPath", fakeResponse.getForwardLocation());
    }

    /**
     * Test de HttpPost: Verifica que el contacto se añade a la agenda
     * usando el servicio de address book.
     *
     * <p><b>REFACTOFING:</b>
     *       El testDoPostGetParameters ya no era necesario, se prueba
     *       lo mismo en testDoPostAddContact. </p>
     *
     * <p>
     * El testDoPostAddContactRedirect también se ha fusionado aqui ya que tiene
     * más sentido comprobar la funcionalidad completa.</p>
     */
    @Test
    public void testDoPostAddContact() throws ServletException, IOException, ParseException {
       // Fakes de los parametros del doGet y doPost que amplian la interfaz
       // estandar permitiendo verificaciones sencillas por estado.
       FakeHttpServletRequest fakeRequest = new FakeHttpServletRequest();
       FakeHttpServletResponse fakeResponse = new FakeHttpServletResponse();

       // Ahora los fackes nos permiten configurar los parametros de entrada
       // del método testeado. Esto no lo permitían los mocks del contenedor
       // ya que HttpServletRequest no tiene un "setParameters".
       fakeRequest.setParameter("firstName", "Pedro");
       fakeRequest.setParameter("surname", "Ballesteros");
       fakeRequest.setParameter("birthday", "8/1/1974");
       fakeRequest.setParameter("phone", "69696969");

       // Ejecución del test.
       controller.doPost(fakeRequest, fakeResponse);

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
       
       // Verifica con el fake que no se ha intentado crear el modelo de errores
       assertNull(fakeRequest.getAttribute("errors"));
       
       // El Fake del HttpServletResponse ahora nos permite verificar por estado
       // la respuesta del doPost.

       // Verifica que se entrega un redirect de navegador al response, ahora
       // el fake nos permite acceder a datos de la respuesta.
       assertEquals("redirectPath", fakeResponse.getSendRedirectLocation());
    }

    /**
     * Test Http Post: Verifica un post con todos los parametros vacíos.
     *
     * @throws ServletException
     * @throws IOException
     */
    @Test
    public void testDoPostNullParams() throws ServletException, IOException {
       // Fakes de los parametros del doGet y doPost que amplian la interfaz
       // estandar permitiendo verificaciones sencillas por estado.
       FakeHttpServletRequest fakeRequest = new FakeHttpServletRequest();
       FakeHttpServletResponse fakeResponse = new FakeHttpServletResponse();
     
       // Ejecución del test para comprobar que se establece el modelo
       // en este caso los errores.
       controller.doPost(fakeRequest, fakeResponse);

       // Verifica que no se ha intentado añadir el contacto ya que
       // el nombre es obligatorio.
       verify(mockAddressbook, never()).addContact(any(Contact.class));

       // Verifica que se ha creado un modelo con los errores con un
       // setAtribute. Decidimos que se introducirán en un Map<String, String>.

       // Ahora se puede verificar por estado, porque sabemos que nuestro fake
       // devuelve valores reales establecidos con el setAttribute.
       Map<String, String> errors = (Map<String, String>)fakeRequest.getAttribute("errors");
       assertTrue(errors.containsKey("firstName"));
       assertFalse(errors.containsKey("surname"));
       assertFalse(errors.containsKey("birthday"));
       assertFalse(errors.containsKey("phone"));

       // IMPORTANTE:
       //   No se han comprobado los textos de error, en un test unitario no se
       //   deben probar la IU. Dichas pruebas estarían en tests de IU (test de sistema).

       // En caso de error la petición se debe dirigir a la vista del formulario
       // de nuevo, que ahora mostrará los campos de entrada erroneos.
       
       // Ahora se puede verificar por estado ya que el fake del response nos
       // permite acceder al path con el que se hizo el forward.
       assertEquals("formViewPath", fakeResponse.getForwardLocation());
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
       // Fakes de los parametros del doGet y doPost que amplian la interfaz
       // estandar permitiendo verificaciones sencillas por estado.
       FakeHttpServletRequest fakeRequest = new FakeHttpServletRequest();
       FakeHttpServletResponse fakeResponse = new FakeHttpServletResponse();

       // El request solo tendrá el parametros del nombre
       fakeRequest.setParameter("firstName", "Pedro");

       // Ejecución del test.
       controller.doPost(fakeRequest, fakeResponse);

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
       assertNull(fakeRequest.getAttribute("errors"));

       // Verifica que se llama al redirect del response con el parametro correcto.
       assertEquals("redirectPath", fakeResponse.getSendRedirectLocation());
    }
}