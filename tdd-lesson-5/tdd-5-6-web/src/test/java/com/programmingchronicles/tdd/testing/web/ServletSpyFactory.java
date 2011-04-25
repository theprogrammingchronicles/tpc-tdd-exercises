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

package com.programmingchronicles.tdd.testing.web;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import static org.mockito.Mockito.*;

/**
 * Factory para la creación de spies del controlador entregado, clase
 * de utilidades para el teting.
 *
 * <p>
 * <b>REFACTORIZACION:</b></p>
 *
 * <p>
 * Esta clase permite agrupar y reutilizar la funcionalidad que vemos
 * repetida en los tests de los controladores.</p>
 *
 * <p>
 * En este caso es una utilidad que permite crear un spy del controlador 
 * entregando (un servlet) creando mocks para el ServletContext</p>
 *
 * <p>
 * Esta clase se podrían extraer en un jar o librería de utilidades que
 * se podrian añadir con scope test en los proyectos que quieran realizar
 * este tipo de tests en los servlets.</p>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class ServletSpyFactory<T extends HttpServlet> {

     private T mockController;
     private ServletContext mockContext;

    /**
     * Crea un un spy o mock parcial del servlet entregado codificando
     * un stub del método getServletContext para que devuelva un un mock
     * que a su vez devuelve un mock de requestDispatcher.
     *
     * <p>
     * Los mocks que devuelven mocks no son muy recomendables, suelen ser
     * indicar fallos de diseño, o como en este caso que estamos usamos un
     * framework de terceros que no está preparado para el testing
     * (ver documentación de <a href="http://www.mockito.com">mockito</a>).</p>
     *
     * @param <T> Tipo del controlador. Debe heredar de HttpServlet.
     * @param controller Instancia del controlador sobre el que se crea el Spy.
     */
    public ServletSpyFactory(T controller) {
        // Mocks del context del servlet.
        mockContext = mock(ServletContext.class);

        // Crea un spy del controlador para que devuelva el mock del context
        // en el método getServletContext.
        mockController = spy(controller);
        doReturn(mockContext).when(mockController).getServletContext();
    }

    /**
     * Devuelve el Spy creado para el controlador entregado.
     *
     */
    public T getMockController() {
        return mockController;
    }

    /**
     * Devuelve el Mock creado para el ServletContext.
     *
     */
    public ServletContext getMockContext() {
        return mockContext;
    }    
}
