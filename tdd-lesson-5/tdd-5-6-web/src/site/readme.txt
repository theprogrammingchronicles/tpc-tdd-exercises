 ------------------------------------------------------
 ${project.description}
 ------------------------------------------------------
 Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 ------------------------------------------------------

${project.name} - ${project.description}

  Implementación de la validación y conversión de los parámetros entregados
  en el Http Post desde el formulario.

  <<NOTA>>: Este subproyecto muestra el estado final después de varias
            iteraciones (y refactorizaciones) del ciclo TDD.

    * Refactorización de todos los controladores para que todos utilicen
      un sistema de configuración de vistas por Direct Injection.

    []

    * Ejemplo de implementación de un <<Custom Matcher>> que simplifica la
      programación de stubs y la verificación con mocks sin afectar al código
      funcional (evita implementar un <equals> en <Contact> adaptado a las
      necesidades de los tests).

          Se implementa como un custom matcher basado en la librería
          {{{http://code.google.com/p/hamcrest/}Hamcrest Matchers}} (Java, C++,
          Objective-C, Python and PHP) y por tanto se puede usar tanto en
          los métodos de JUnit como en los de Mockito y multitud de librerías.

          * Los assert basados en matchers (<<assertThat>>) proponen una sintaxis
            de lectura más natural.

          * Los matchers proporcionan utilidades que facilitan validaciones que
            de otra forma requeririan más código, como la verificación de colecciones
            evitando antipatrones de fijación del orden de los elementos como <<hasItems>>.

          * También proporcionan una informacion detallada y más descriptiva de
            la causa del error en caso de fallos de los test.

    []

    * Refactorización de tests: Se usa en todos el spying de mockito.

    []

    * Refactorización de tests: Se está creando una librería de utilidades
      para el testing (código generico para tests de Servlets).

       * Extreme Programing: <<Fijar Estandares>>

         Organizar como crecen las librerías de utilidades para que
         todos las usemos y no se repitan implementaciones.


    * Refactorización SRP:

      Descubrimos que la funcionalidad de validar y convertir parámetros es una
      responsabilidad externa del controlador.

    * Está comenzando a aparecer un framework en nuestra arquitectura:
      En TDD nunca nos ponemos por adelantado a diseñar un framework
      generico que se pueda reutilizar en un futuro.

      <"Se programa para el ahora y nunca para el futuro">

      Pero en las clases Converter empezamos a vislumbrar que mediante
      la refactorización iterativa estamos dando los primeros pasos
      hacia nuestro propio framework, sin haberlo diseñado por adelantado.

    * Refactorización del código repetido en el object under test que se
      a producido por la codificación copy & paste.

      ContactCommandConverter --> ContactCommandConverterV2.