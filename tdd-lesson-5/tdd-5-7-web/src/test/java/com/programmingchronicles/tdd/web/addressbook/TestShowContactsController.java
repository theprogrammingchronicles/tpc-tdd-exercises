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
import com.programmingchronicles.tdd.testing.web.FakeHttpServletRequest;
import com.programmingchronicles.tdd.testing.web.FakeHttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests del controlador de obtención de contactos.
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
 * <b>NOTAS RESTRICCIONES:</b><br/>
 *   En este ejemplo ya empezamos a usar el request para obtener
 *   el dispatcher. Que era una restriccion que se había impuesto
 *   solo para mostrar ejemplos de Spy del Object Under Test.</p>
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

    // Object Under Test
    private ShowContactsController controller;

    @Before
    public void setUp() {
        // Ya no construimos el Spy del Controlador, en este ejercicio se ha
        // eliminado la restricción que se había impuesto, y podemos
        // usar el request para obtener el requestDispatcher.
        controller = new ShowContactsController();

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

       // Fakes de los parametros del doGet y doPost que amplian la interfaz
       // estandar permitiendo verificaciones sencillas por estado.
       FakeHttpServletRequest fakeRequest = new FakeHttpServletRequest();
       FakeHttpServletResponse fakeResponse = new FakeHttpServletResponse();

       // Ejecución del test.
       controller.doGet(fakeRequest, fakeResponse);

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

       // El request y response son los parametros de entrada y salida
       // del método testado, con los fakes ahora podemos hacer verificación
       // por estado en lugar de interacción.

       // Verifica que el modelo se a creado correctamente. Ahora se puede
       // verificar por estado, porque sabemos que nuestro fake devuelve
       // valores reales establecidos con el setAttribute.
       assertSame(fakeRequest.getAttribute("contacts"), contactList);

       // Verifica que el forward se realiza a la vista correcta, usando
       // las facilidades del Fake que permiten obtener el path del forward,
       // en lugar de realizar una validación por interacción.
       assertEquals("viewPath", fakeResponse.getForwardLocation());
    }
}