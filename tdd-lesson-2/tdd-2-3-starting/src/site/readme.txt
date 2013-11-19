 ------------------------------------------------------
 ${project.description}
 ------------------------------------------------------
 Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 ------------------------------------------------------

${project.name} - ${project.description}

    Resultado después de iterar varias veces el bucle TDD.

    Se obtiene una primera aproximación que posiblemente se tenga
    que refinar refactorizando.

    Al implementar la funcionalidad de obtener un solo contacto se
    identifican necesidades de implementación que no se mencionaban
    en la especificación o tests de aceptación. La necesidad de que
    cada contacto tenga un ID único asociado (vocabulario de la
    implementación vs vocabulario del negocio).

    * <<IMPORTANTE>>

      Se implementa un servicio de agenda con almacenamiento en memoria. Esto
      no tiene nada que ver con la naturaleza incremental de TDD. Se implementa
      un almacenamiento en memoria para simplificar los ejercicios iniciales.

      En una aplicación real sería conveniente empezar a implementar directamente
      una versión basada en el almacenamiento sobre base de datos.

