 ------------------------------------------------------
 ${project.description}
 ------------------------------------------------------
 Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 ------------------------------------------------------

${project.name} - ${project.description}

  En este ejemplo surge la necesidad de crear mocks más complejos; mocks
  que devuelven mocks, ya que nos lo exige el framework del contenedor.

  * En un ejercicio posterior veremos que en este caso los tests se <<pueden
    simplificar codificandolos manualmente>> sin usar una librería de creación
    automática de mocks.

    <<Corolario>>: Las librerías de ayuda al testing facilitan el trabajo,
                   pero no hay que empeñarse con su uso cuando no es
                   necesario, siempre buscamos la simplicidad.

  * Ejemplo de Spy (Mock parcial del Objetct Under Test).
    No recomendado para unit testing, síntoma de <"code smell">, suele indicar
    un diseño pobre, o que no está diseñado con testing en mente.

    Pero es útil usando librerías de terceros, que no controlamos,
    como el contenedor de servlets.

  * Codificación Manual de un Spy para el Object Under Tests.


* RESTRICCIONES PARA EL EJERCICIO

    El requestDispatcher normalmente se obtiene a través del request, y no
    del servletContext. Para mostrar ejemplos de la técnica de spy, se
    va a restingir a que solo podemos obtener el requestDispatcher a través
    de los métodos heredados de HttpServlet.