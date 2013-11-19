 ------------------------------------------------------
 ${project.description}
 ------------------------------------------------------
 Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 ------------------------------------------------------

${project.name} - ${project.description}

    Se continua con la implementación del resto de la funcionalidad del ejercicio,
    aplicando el bucle TDD y guiándose por los tests de aceptación como punto de
    partida de los tests unitarios.

    En los siguientes ejercicios se mostrarán diferentes ejemplos de uso de
    TDD a lo largo del desarrollo de la funcionalidad. Observando como el
    diseño y la arquitectura va evolucionando, y como se van aplicando diferentes
    técnicas de unit testing.

* RESUMEN

    * Refactorización de tests: eliminación de código de test repetido mediante la
      agrupación en clases de test abstractas.

    []

    * Test de interfaces genéricas y de implementaciones concretas.

    []

    * Test de propiedades de objetos de datos (JavaBean setters / getters),

      ¿Son realmente necesarios estos tests? ¿Se pueden testear de forma completa
       sin romper la encapsulación private que ofrece OOP?.

    * Ejemplo de test erróneo que lanza falsos oks.

    []

    * Ejemplo de defectos relacionados con YAGNI: se está implementando más
      funcionalidad de la requerida por el tests.

    []

    * Mal uso de TDD: casi sin notarlo se está implementando funcionalidad
      antes de haber implementado el test que la verifica.

      Es fácil incluso no darse cuenta y dejar finalmente código sin testear,
      y en cualquier caso estaríamos usando TAD (Test After Development).

    * Nueva funcionalidad que rompe tests ya existentes.

    []

    * Aparición de nuevos tests unitarios que no estaban contemplados en los
      test de aceptación.

      Al bajar a nivel de implementación aparecen necesidades no contempladas ya
      que ahora se trabaja con el vocabulario de la implementación.

    * Ejemplo de algún antipatron TDD.

    []

    * Ejemplos más avanzados de mocks automáticos.
