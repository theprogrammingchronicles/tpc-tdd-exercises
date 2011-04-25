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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controlador que procesa la petición y devuelve la vista con los
 * contactos del sistema.
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class ShowContactsController extends HttpServlet {

    // Acceso al servicio de GlobalAddressBook, se configurará
    // mediante Direct Injection (IoC).
    private GlobalAddressBook addressBook;

    // REFACTORIZACION: Ahora la vista es configurable.
    private String viewPath;

    /**
     * Handles the HTTP <code>GET</code> method.
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

    /**
     * Configuración de la vista del controlador.
     *
     * @param string
     */
    void setViewPath(String viewPath) {
        this.viewPath = viewPath;
    }
}