 ------------------------------------------------------
 ${project.description}
 ------------------------------------------------------
 Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 ------------------------------------------------------

${project.name} - ${project.description}

    Resultado después de iterar varias veces el ciclo TDD, creando
    nuevos tests y completando su funcionalidad.

    Se obtiene una primera aproximación que posiblemente se tenga
    que refinar refactorizando.

    Al implementar la funcionalidad de obtener un contacto existente
    (getContact) se indentifican necesidades de implementación que no
    aparecían en la especificación de requisitos o tests de aceptación.
    La necesidad de que cada contacto tenga un ID único asociado (en
    TDD se trabaja con el vocabulario de la implementación en lugar
    del vocabulario del negocio).

    * <<IMPORTANTE>>

      Se implementa un servicio de agenda con almacenamiento en memoria. Esto
      no tiene nada que ver con la naturaleza incremental de TDD. Se implementa
      un almacenamiento en memoria para simplificar los ejercicios iniciales.

      En una aplicación real sería conveniente empezar a implementar directamente
      una versión basada en el almacenamiento sobre base de datos.

