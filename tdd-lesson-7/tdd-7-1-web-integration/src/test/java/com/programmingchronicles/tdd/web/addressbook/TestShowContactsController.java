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
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests del controlador de obtención de contactos.
 *
 * Se implementa el test del init para validar que el servlet
 * inicializa bien su configuración.
 *
 * FUTURA REFACTORIZACION:
 *    Implementar una arquitectura MVC de Controlador Frontal de forma
 *    que los controladores no tengan que ser servlets.
 *
 * DISEÑO:
 *    Arquitectura MVC usando sólo tecnologías nativas del J2EE:
 *       - Modelo: JavaBeans (Java-Old Plain Objects).
 *       - Controlador: Servlets.
 *       - Vista: JSP (sin lógica: solo JSTL + EL).
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

    // El controlador necesita buscar los servicios en el init, ya que
    // no se pueden configurar por Direct Injection. Es el contenedor de
    // servlet el que crea los controladores.
    @Test
    public void testInit() throws ServletException {
        ServletConfig config = mock(ServletConfig.class);

        // Se codifica una respuesta para los parametros de configuración del
        // servlet. Se configuran las vistas en el web.xml y el init debe
        // inicializar su configuración. No hay posibilidad de IoC.
        when(config.getInitParameter("view")).thenReturn("viewPathValue");

        // Se configura un mock del contexto que devuelve una instancia
        // de GlobalAddressBook como un mock. Solo hay que verificar
        // que el init configura bien el controlador, por tanto no hace
        // falta una implementación real de GlobalAddressBook.
        ServletContext context = mock(ServletContext.class);
        when(config.getServletContext()).thenReturn(context);     
        when(context.getAttribute("globalAddressBook")).thenReturn(mockAddressbook);

        // Se instancia un controlador no inicializado.
        ShowContactsController notInitController = new ShowContactsController();
        notInitController.init(config);

        // Se verifica la configuración

        // Se comprueba que el addressbook se ha configurado con lo que devuelve
        // el contexto. Se comprueba que es instancia de GlobalAddressBook, no
        // se liga a implementaciones específicas.
        assertTrue("init addressbook failed", notInitController.getAddressBook() instanceof GlobalAddressBook);

        // Verifica que las vistas se configuran con el valor devuelto por
        // getInitParameter del servlet. Los valores reales de las vistas
        // no se deben testear ya que son configuraciones de despliegue.
        assertEquals("viewPathValue", notInitController.getViewPath());
    }

    /**
     * Al ser una arquitectura MVC, lo unico que hay que testear es:
     *      - El controlador recibe los parametros.
     *      - Construye el modelo de forma adecuada.
     *      - Se redirige la petición a la vista adecuada.
     *
     * El controlador se implementará como un Servlet, y la logica se
     * procesará en un HTTP GET.
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