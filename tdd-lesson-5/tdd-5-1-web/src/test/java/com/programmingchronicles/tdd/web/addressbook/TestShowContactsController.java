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
import java.io.IOException;
import javax.servlet.ServletException;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Tests del Controlador de obtención de contactos.
 *
 * <p>
 * <b>DISEÑO:</b> Arquitectura MVC usando tan sólo tecnologías del J2EE:<br/>
 *     <li>- Modelo: JavaBeans (Java-Old Plain Objects).
 *     <li>- Controlador: Un Servlet para cada petición web.
 *     <li>- Vista: JSP (sin nada de lógica, solo JSTL + EL).
 * </p>
 *
 * <p>
 * <b>PROBLEMATICA:</b>
 * <p>
 *      El framework es el que decide los métodos que se deben implementar
 *      en el servlet. No podemos decidir cuales ni cómo serán los
 *      parámetros del método a implementar.
 * </p>
 * <p>
 *      El framework establece que el doGet tiene como parámetro de entrada
 *      una interfaz HttpServletRequest y como parámetro de salida la
 *      interfaz HttpServletResponse.
 * </p>
 * <p>
 *      El problema es que no proporciona una implementación de dichas interfaces
 *      que se pueda utilizar para construir parámetros de ejemplo en el test.
 *      Por tanto se deben utilizar stubs o fakes.
 * </p>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class TestShowContactsController {

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
        // Object Under Test
        ShowContactsController controller = new ShowContactsController();

        // Test: ¿Como se entregan los parametros requeridos?
        controller.doGet(null, null);

        // Verificación
        fail("Not implemented yet.");
    }
}