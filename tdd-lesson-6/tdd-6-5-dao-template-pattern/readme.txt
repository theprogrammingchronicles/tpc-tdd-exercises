 ------------------------------------------------------
 ${project.description}
 ------------------------------------------------------
 Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 ------------------------------------------------------

${project.name} - ${project.description}

    * Ejemplo de refactorización mediante el <<Template Design Pattern>>, que
      entre otras muchas ventajas simplifica y robustece el testing unitario.

    []

    * Más ejemplos de cuando es apropiada la validación por interacción y
      la validación por estado (<<TestJdbcDaoTemplate>>).

    []

* REFACTORIZACIÓN

    Refactorización en el DAO del código de manipulación de objetos de la
    librería de acceso a base de datos (JDBC), para la eliminación de código
    repetitivo y su generalización. Lo que proporciona un código que será
    completamente reutilizable en otros DAOs.

    La eliminación del código repetido se basa en el <<Template Design Pattern>>,
    que es útil cuando varios métodos solo difieren en unas pocas líneas internas,
    permitiendo extraer el código repetitivo que rodea dicho código.

    El patrón <<template>> permite modificar el comportamiento de un código
    de plantilla mediante la entrega de callbacks que se ejecutan en puntos
    concretos de la plantilla.

    En el ejemplo el DAO reduce el código en unas 50 líneas además de proporcionar
    las siguientes ventajas:

        * El código de los DAO ahora tiene <<menos responsabilidades>> extraídas
          mediante la aplicación del principio <<SRP>> de <<SOLID>>.

        []

        * <<Los tests de los DAOs se simplifican>> ya que deben probar menos
          responsabilidades (extraídas en el código del template), que ahora
          será el encargado de validar dichas responsabilidades.

        []

       * Los templates centralizan un código genérico de manipulación de
         objetos de la librería de acceso a base de datos que suele ser
         muy propenso a errores por descuidos.

        []

        * Los test de los templates pueden ser más rigurosos validando la
          gestión y cierre de recursos de la base de datos. Estos test ya
          no se deben repetir en todos los DAOs.

        []

        * Al estar centralizado el acceso a la base de datos en un solo punto
          reutilizable por todos los DAOs, se pueden realizar <<Test Unitarios
          Verdaderamente Aislados>> sin necesidad de acceder a una base de
          datos (real, embebida o en memoria). Es más sencillo basar los tests
          en la creación de stubs y mocks.

        []

        * El códidigo resultante es más robusto y menos propenso a errores,
          aseguramos de que todos los DAOs realizarán una gestión correcta y
          liberación de los recursos de acceso a la base de datos. Ya no es
          su responsabilidad, el template es el único encargado de liberar
          y crear estos recursos.

        []

        * La creación de nuevos DAOS será bastante más sencilla, menos propensa
          a errores, y con mucho menos código.

        []

    <<NOTA:>> Existen librerías que ya proporcionan este tipo de templates para
              el caso de JDBC, como <JDBC Spring Templates>.
