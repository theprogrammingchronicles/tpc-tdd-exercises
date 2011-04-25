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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controlador para mostrar y procesar el formulario de añadir contactos.
 *
 * <p>
 * Permitir la configuración de las vistas que utilizará cada controlador.</p>
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
public class AddContactController extends HttpServlet {

    // Despues de la codificación de ShowContactController, nos damos cuenta
    // de que es mejor diseño dejar las vistas configurables.
    // Luego refactorizaremos ShowContactsContrller con la misma idea.
    private String successedSubmitRedirect;
    private String formView;

    // Acceso al servicio de GlobalAddressBook, se configurará
    // mediante Direct Injection (IoC).
    private GlobalAddressBook addressBook;
   
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
        getServletContext().getRequestDispatcher(formView).forward(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
       String firstName = request.getParameter("firstName");
       String surname = request.getParameter("surname");
       String birthday = request.getParameter("birthday");
       String phone = request.getParameter("phone");

       DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

       // Se convierten los parametros al domain object que necesita
       // el servicio address book.
       Contact contact = new Contact();
       contact.setFirstName(firstName);
       contact.setSurname(surname);
       try {
           // Ya vemos que esto fallará si no se entrega el birthday, pero
           // no lo implementamos todavía, ya que en principio no tenemos
           // un test de prueba de parametros erroneos.
           contact.setBirthday(dateFormat.parse(birthday));
       } catch(ParseException ex) {
            throw new ServletException("Wrong Birthday", ex);
       }
       contact.setPhone(phone);

       // Se inserta el contacto en la agenda
       addressBook.addContact(contact);

       // Envia una respuesta de redirección al navegador (evitando el doble post).
       response.sendRedirect(successedSubmitRedirect);
    }

    /**
     * Obtiene el servicio de agenda configurado.
     *
     * @return
     */
    public GlobalAddressBook getAddressBook() {
        return addressBook;
    }

    /**
     * Configura el servicio de agenda.
     *
     * @param addressBook
     */
    public void setAddressBook(GlobalAddressBook addressBook) {
        this.addressBook = addressBook;
    }   

    /**
     * Obtiene el path de la redirección de submit correcto.
     *
     * @return
     */
    public String getSuccessedSubmitRedirect() {
        return successedSubmitRedirect;
    }

    /**
     * Configura el path de la redirección de submit correcto.
     * 
     * @param successedSubmitRedirect
     */
    public void setSuccessedSubmitRedirect(String successedSubmitRedirect) {
        this.successedSubmitRedirect = successedSubmitRedirect;
    }

    /**
     * Obtiene el path de la vista del formulario.
     *
     * @return
     */
    public String getFormView() {
        return formView;
    }

    /**
     * Configura el path de la vista del formulario.
     *
     * @param formView
     */
    public void setFormView(String formView) {
        this.formView = formView;
    }
}
