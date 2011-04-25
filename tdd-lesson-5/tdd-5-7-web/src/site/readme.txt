 ------------------------------------------------------
 ${project.description}
 ------------------------------------------------------
 Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 ------------------------------------------------------

${project.name} - ${project.description}

  Ejemplo de codificación manual de fakes de objetos del contenedor
  (<HttpServletRequest / HttpServletResponse>), que permiten realizar
  verificación por estado.

  Los test se hacen más simples y además se mejora su legibilidad
  ya que permite eliminar mucha verificación por interacción.
  Los tests quedan más simples que cuando se usaban los mocks
  automáticos de "mockito":

    * Corolario:

      Las librerías de ayuda al testing facilitan el trabajo,
      pero no hay que empeñarse con su uso cuando no es
      necesario, siempre buscamos la simplicidad.

  En el ContactCommandConverter se pueden seguir usando los stubs de
  mockito en lugar de los nuevos fakes, como aqui no se hace verificación
  por interacción, con los fakes quedaría prácticamente igual.

  * Refactorización de tests:

    Se eliminan los tests que surgieron por el uso de tanto
    copy & paste y que ya no son necesarios.
