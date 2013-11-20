 ------------------------------------------------------
 ${project.description}
 ------------------------------------------------------
 Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 ------------------------------------------------------

${project.name} - ${project.description}

    * Aparición de nuevos tests:

      Desde los tests de aceptación van apareciendo tests unitarios no
      contemplados inicialmente, debido a necesidades detectadas durante
      la implementación (ahora se usa el vocabulario de la implementación).

      <<IMPORTANTE>>: Los Test de Aceptación no deben limitar el numero de tests
                      que necesitamos para aplicar TDD.

    * Aparición de tests que tests que no se pueden hacer fallar para poder
      implementar nueva funcionalidad.

      <<Ver>>: testAddDuplicateSurname

      Ejemplo de test que no se puede hacer fallar, ya que el código
      existente no tiene en cuenta apellidos, para poder implementar
      esta funcionalidad primero es necesario implementar la funcionalidad
      opuesta.

      Es necesario implementar el test que verifica que si se pueden
      añadir dos contactos con el mismo nombre.

