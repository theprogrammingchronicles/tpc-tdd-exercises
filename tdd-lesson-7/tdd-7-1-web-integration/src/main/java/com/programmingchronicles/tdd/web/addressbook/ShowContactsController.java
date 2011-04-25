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

import com.programmingchronicles.tdd.addressbook.GlobalAddressBook;
import com.programmingchronicles.tdd.domain.Contact;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controlador que procesa la petición y devuelve la vista con los
 * contactos del sistema.
 *
 * Se implementa el código de inicialización que no puede estar basado
 * en Direct Injection, aunque se sigue manteniendo configurable.
 *
 * Ver también: getViewPath()
 *
 * FUTURA REFACTORIZACION:
 *    Implementar una arquitectura MVC de Controlador Frontal de forma
 *    que los controladores no tengan que ser servlets.
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class ShowContactsController extends HttpServlet {

    // Acceso al servicio de GlobalAddressBook, se configurará
    // mediante Direct Injection (IoC).
    private GlobalAddressBook addressBook;

    private String viewPath;

    /**
     * En los objetos instanciados por el contenedor no se puede usar Direct
     * Injection, para configurar las dependencias de los controladores.
     *
     * Al implementarse como servlets las instancia el contenedor.
     *
     * Se pierde parte de IoC, pero aun así se implementa un mecanismo que
     * que sigue manteniendo los controladores independientes de la
     * implementación concreta de los servicios.
     *
     * FUTURA REFACTORIZACION:
     *   Implementar una arquitectura MVC de Controlador Frontal de forma
     *   que los controladores no tengan que ser servlets.
     *
     * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        addressBook = (GlobalAddressBook) config.getServletContext().getAttribute("globalAddressBook");
        viewPath = config.getInitParameter("view");
    }


    /**
     * La petición se procesa en un HTTP GET.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        // Obtiene los contactos usando los servicios de la
        // capa de negocio.
        List<Contact> contacts = addressBook.getAll();

        // El modelo se entrega a las vistas como un atributo del request.
        request.setAttribute("contacts", contacts);

        // Forward de la petición hacia la vista que renderiza la UI.
        request.getRequestDispatcher(viewPath).forward(request, response);
    }

    /**
     * Devuelve el servicio agenda configurado en el controlador.
     */
    public GlobalAddressBook getAddressBook() {
        return addressBook;
    }

    /**
     * Configura el servicio de agenda usando por el controlador.
     *
     * @param addressBook
     */
    public void setAddressBook(GlobalAddressBook addressBook) {
        this.addressBook = addressBook;
    }

    void setFormView(String string) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Configuración de la vista del controlador.
     *
     * @param string
     */
    void setViewPath(String viewPath) {
        this.viewPath = viewPath;
    }

    /**
     * Devuelve la vista del controlador
     *
     * NOTA: Hasta que no se han empezado a implementar las necesidades
     *       de integración no se ha visto que la propiedad viewPath
     *       era de solo escritura al no tener un "getViewPath".
     *       No hay tests unitarios para esto.
     *
     * @return
     */
    public String getViewPath() {
        return viewPath;
    }
}
