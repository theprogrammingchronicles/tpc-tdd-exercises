 ------------------------------------------------------
 ${project.description}
 ------------------------------------------------------
 Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 ------------------------------------------------------

${project.name} - ${project.description}

    Se implementa la funcionalidad especificada por el test
    "testAddWithoutFirstName".

    * Ejemplo de defectos relacionados con YAGNI:

      Durante la implementación decidimos que si se está controlando
      que firstName no puede ser null, también tiene sentido probar
      que no entregan cadena vacía o espacios en blanco.

      Estamos codificando sin haber escrito los tests por adelantado. Se
      observa que incluso se implementa con bug que no se detectan.

    []

    * Mal uso de TDD: casi sin notarlo se está implementando funcionalidad
      antes de haber implementado el test que la verifica.

      Es fácil incluso no darse cuenta y dejar finalmente código sin testear,
      y en cualquier caso estaríamos usando TAD (Test After Development).

      Si se codifica más funcionalidad de la exigida por el test estaremos
      dejando funcionalidad sin probar. Cuando se localiza nueva funcionalidad
      se debe volver a los tests y codificar los que prueban de dicha funcionalidad.
