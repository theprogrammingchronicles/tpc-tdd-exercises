 ------------------------------------------------------
 ${project.description}
 ------------------------------------------------------
 Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 ------------------------------------------------------

${project.name} - ${project.description}

   Se corrigen los tests que se habian roto al implementar la funcionalidad
   especificada por "testAddDuplicateNameDifferentSurname".

   Para corregir estos tests se detectan nuevos test unitarios que dirigen
   el desarrollo de estas correcciones.

    * Aparición de nuevos tests:

      Desde los tests de aceptación van apareciendo tests unitarios no
      contemplados inicialmente, debido a necesidades detectadas durante
      la implementación (ahora se usa el vocabulario de la implementación).

      <<IMPORTANTE>>: Los Test de Aceptación no deben limitar el numero de tests
                      que necesitamos para aplicar TDD.

    * La aplicación iterativa del bucle TDD hace que tests ya existentes
      comiencen a fallar. En este ejemplo se trata de funcionalidad a corregir.

    []

    * Los nuevos tests aparecen de forma natural debido a necesidades que se
      van encontrando durante la implementación o la refactorización (fallos
      en tests que anteriormente funcionaban).

      (<<Ver>>: testAddDuplicateSurname)