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

import com.programmingchronicles.tdd.web.addressbook.ServicesConfigurationContextListener;
import com.programmingchronicles.tdd.addressbook.GlobalAddressBook;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 * Lamentablemente en los objetos del contenedor no podemos usar
 * Direct Injection para configurar las depencias de los controladores
 * ya que al ser servlets las instanciará el contenedor.
 *
 * Se pierde el control de la instanciación de los controladores, por tanto
 * tienen que buscar sus dependencias. No se puede configurar desde el cliente.
 *
 * Se pierde parte de IoC, pero se usa un sistema para que los controladores
 * sigan siendo independientes de la implementación.
 *
 * Los controladores no sabrán ni que existe MemAddressBook, solo conocen
 * la interfaz del serivio GlobalAddressBook.
 *
 * La configuración e instanciación de dependencias se realiza en
 * un listener del contexto.
 *
 * FUTURA REFACTORIZACION:
 *    Implementar una arquitectura MVC de Controlador Frontal de forma
 *    que los controladores no tengan que ser servlets.
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class TestServicesConfigurationContextListener {     

    @Test
    public void testContextInitialized() {
        ServicesConfigurationContextListener listener = new ServicesConfigurationContextListener();

        // En este caso es sencillo usar mocks generados.
        ServletContextEvent sce = mock(ServletContextEvent.class);
        ServletContext context = mock(ServletContext.class);
        when(sce.getServletContext()).thenReturn(context);

        // Ejecución del test.
        listener.contextInitialized(sce);

        // Verifica que se crean en el servletContext los servicios con el
        // nombre correcto. Se verifica que es una instancia que implementa
        // la API GloblalAddressBook.
        verify(context).setAttribute(eq("globalAddressBook"), isA(GlobalAddressBook.class));

        // PREGUNTA:
        //   ¿Deberiamos verificar que el GlobalAddressBook es en realidad una
        //    instancia de MemAddressBook?

        //   Ver las notas en la implementación de ContextInicialized. Si verifica,
        //   estamos realizando un test de caja blanca y atando el funcionamiento
        //   del tests a un detalle de configuración y despliegue.

        //   En este caso la configuración se hace por código en el listener,
        //   pero se podría intentar extraer. Actualmente sería un test
        //   de caja blanca, que tampoco es muy problematico usando TDD ya que
        //   si se cambia la implementación se cambia el test y ya está.

        //   Como en este caso la configuración se hace por código (en el
        //   listener), se podrían estar enmascarando errores (como en el ejemplo).

        //   No obstante son errores que se localizarán facilmente en un
        //   test de integración o de sistema.
    } 
}