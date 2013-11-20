 ------------------------------------------------------
 ${project.description}
 ------------------------------------------------------
 Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 ------------------------------------------------------

${project.name} - ${project.description}

    En este ejemplo se continua con la implementación del resto de los tests de
    aceptación de la funcionalidad de <añadir contactos>.

    Se empiezan codificando los distintos test unitarios que se derivan
    de cada uno de los test de aceptación del documento (addContact).

    * Ejemplo de defectos relacionados con YAGNI: se está implementando más
      funcionalidad de la requerida por el tests.

    []

    * Mal uso de TDD: casi sin notarlo se está implementando funcionalidad
      antes de haber implementado el test que la verifica.

      Es fácil incluso no darse cuenta y dejar finalmente código sin testear,
      y en cualquier caso estaríamos usando TAD (Test After Development).

      Si se codifica más funcionalidad de la exigida por el test estaremos
      dejando funcionalidad sin probar. Cuando se localiza nueva funcionalidad
      se debe volver a los tests y codificar los que prueban de dicha funcionalidad.
