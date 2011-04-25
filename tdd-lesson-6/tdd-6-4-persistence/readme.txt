 ------------------------------------------------------
 ${project.description}
 ------------------------------------------------------
 Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 ------------------------------------------------------

${project.name} - ${project.description}

    Ejemplo de programación de un Data Access Object usando TDD. Se crea
    una implementación de la interfaz AddressBookDao basada en JDBC.

    Se implementa la funcionalidad del JdbcAddressBookDao aplicando TDD
    sobre el sistema de mock de base de datos en memoria utilizado.

        * Ejemplo de implementación de un <<Custom Matcher>> que simplifica
          el código de validación de los test (<IsContactEqual>) usando el
          <<assertThat>> de JUnit.

          Se implementa como un custom matcher está basado en la librería
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

        * Ejemplo de control de liberación de recursos basado en una implementación
          propia que evita la utilización de verificaciones de interacción
          complicadas y dificiles de entender.


