 ------------------------------------------------------
 ${project.description}
 ------------------------------------------------------
 Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 ------------------------------------------------------

${project.name} - ${project.description}

    Creación de una implementación de GlobalAddressBook basada en el
    acceso a los DAOs de la capa de presentación.

    * Se completa la funcionalidad de DbAddressBook copiando tests ya disponibles
      desde MemAddressBook, y refactorizando la implementación actual para basarla
      en el acceso a un AddressBookDao. También aparecen nuevos tests.

    []

    * Se requiere la utilización de stubs / mocks para aislar los test de
      la implementación real del AddressBook Dao y del acceso a la base de
      datos. De hecho la implementación del DAO aun no está disponible.

    []

    * Los test solo sirven para validar el código existente en DbAddressBook,
      no para validar el código de acceso a la Base de Datos, responsabilidad
      del AddressBookDao que tendrá sus propios tests.


* <<VERIFICACIÓN ESTADO (STUB) O INTERACCIÓN (MOCK)>>

      Se debe evitar siempre que se pueda la <verificación por interacción>.
      Los test no se deben convertir en una verificación rigurosa de que
      DbAddressBook llama o no llama a los métodos de AddressBookDao.

      El objetivo principal es verificar el código o lógica del Object Under
      Test. De hecho los métodos que son una delegación directa sin lógica
      de negocio se podrían dejar sin test, ya que dicho test no ayuda mucho
      a dirigir el desarrollo, y unos test de integración de nivel superior
      realizarán la validación mucho mejor y sin usar mocks.

      En este caso el <<verify>> de un mock debería servir solo como sustituto
      de un <<assert>>. Usando la dependencia real el <<assert>> nos permite
      validar el resultado, usando un mock el resultado final será una llamada
      a un objeto que no tenemos, por tanto se comprobará con un <<verify>>.

      Se deben evitar los <<verify>> redundantes, y tener cuidado con los
      que verifican la falta de interacción ya que hacen el test inflexible
      a futuros cambios y refactorizaciones.

       <<VER>>: \
        Basic kinds of interactions: <<asking an object of data>> and <<telling
        an object to do something.>>

          {{http://monkeyisland.pl/2008/04/26/asking-and-telling/}}

          {{http://codebetter.com/blogs/aaron.jensen/archive/2008/04/03/separate-stub-and-verify-duplicate-code-necessarily.aspx}}

* RESUMEN

    * Cambiando validación de interacción por validación de estado, evitando
      validaciones redundantes. Usando llamadas programadas (stub) en lugar
      de verificación por interacción.

      Donde sea posible la programación de la respuesta del stub del DAO
      simula datos disponibles en la base de datos (no disponible).

    * Verificación por interacción: matching de argumentos o captura de
      de argumentos. Verificación del resultado mediante el matching de
      los datos usados en la interacción.

    []

    * Implementación de funcionalidad adicional para facilitar el matching de
      argumentos y la verificación de datos.

      La implementación de un método <<Contact#equals>> facilitaría la
      comparación de contactos y el uso de los matching de argumentos.

    * Introducción a los {{{http://code.google.com/p/hamcrest/}Hamcrest Matchers}}
      (Java, C++, Objective-C, Python and PHP).

      En el siguiente ejercicio 6.4 se mostrará un ejemplo de cómo simplificar
      tests mediante la creación de custom matchers (<<assertThat>>).

    * Se detectan fallos en la implementación anterior de la lógica de negocio.

      La lógica de negocio se ha copiado de MemAddressBook, y los test muestran
      mediante su fallo que el uso de una base de datos ahora requiere adaptar
      no solo la delegación en el DAO si no también lógica ya existente.

      <Ej.> En esta implementación no podemos asumir que los nombres y apellidos
      siempre están almacenados sin espacios, como se asumía en la implementación
      basada en memoria.

      <<Lo mejor es que esta necesidad la han detectado los test unitarios que
        ya teníamos de forma automática>>.

    * Test unitarios para métodos de delegación directa.

      En estos casos el test unitario tan solo valida que la implementación
      se delega completamente en la dependencia. Realmente no ayuda mucho
      a dirigir el desarrollo.

      <¿Es necesario tener test unitarios de este tipo de métodos?>

      Si disponemos de una batería de test de integración de más alto nivel
      no sería muy necesario, los test de integración validarán este tipo
      de código de forma más clara. Los fallos serán fáciles de detectar
      aunque sean test de integración debido a la simplicidad de una delegación
      directa (sin lógica de negocio).

