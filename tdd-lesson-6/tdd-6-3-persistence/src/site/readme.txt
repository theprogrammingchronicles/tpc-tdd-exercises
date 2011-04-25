 ------------------------------------------------------
 ${project.description}
 ------------------------------------------------------
 Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 ------------------------------------------------------

${project.name} - ${project.description}

    Ejemplo de programación de un Data Access Object usando TDD. Se crea
    una implementación de la interfaz AddressBookDao basada en JDBC.

        * Ejemplo de uso de una base de datos embebida y en memoria para
          usar test de integración como test unitarios.

        []

        * Implementación de la independencia entre los tests realizando
          un rollback al final de cada test.

        []

        * Ejemplo de validación basada en el acceso directo a la base de datos
          en lugar del acceso a través del DAO bajo prueba.

        []

        * Ejemplo de creación de Mock Parcial basado en la implementación de
          un <<Delegation Pattern>> que permite manipular las transacciones
          desde los tests (no se puede hacer con el spy de mockito).


* PROGRAMANDO DAOs CON TDD

    Siendo completamente rigurosos para programar verdaderos test unitarios
    de un objeto de estas características habría que independizar el código
    del acceso real a la base de datos, creando mocks del los objetos de la
    librería de acceso base de datos (mock de los objetos JDBC).

     * <<NOTA>>:

         En un ejercicio posterior se realizará una refactorización de
         eliminación de código duplicado, que extrae, centraliza y
         hace reutilizable la mayor parte del código de JDBC.

         Esta reutilización del código permite realizar verdaderos test
         unitarios aislados de la base de datos, que garantizan una
         correcta utilización y liberación de los recursos. Al estar
         centralizado y generalizado el código JDBC, estos test ya no
         necesitan aparecer en cada uno de los DAOs.

    Siendo completamente rigurosos cualquier test que no pueda independizarse
    del acceso a una base de datos se convierte en un <<test de integración>>,
    ya que el código está accediendo a un sistema externo (la base de datos).


* MOCK BASE DE DATOS

    En una primera aproximación (antes de disponer de la refactorización que
    nos simplifica la creación de verdaderos test unitarios), se programarán
    tests basados en el acceso a un mock de la base de datos

    Si las sentencias SQL utilizadas no son muy específicas de un sistema
    de base de datos concreto y se usa un SQL genérico, se puede utilizar
    un mock de una base de datos embebida o basada en memoria.

    El mock es una base de datos embebida (y si se puede creada en memoria). Las
    tablas y campos necesarios se crean al inicio de la batería de test. No se
    crea el esquema completo, solo las tablas y campos usados por el DAO.


* INDEPENDENCIA DE LOS TESTS (rollback)

    Para mantener el principio <<INDEPENDENT>> de las propiedades <<FIRST>> se
    realiza un rollback de los cambios al finalizar cada test.

    Esto también permite que estos test puedan funcionar como test de integración
    contra la base de datos de desarrollo, ya que mantiene las tablas limpias de
    datos de test.

    Para convertirlos en test de integración tan solo habría que eliminar del
    <class fixture> de creación de las tablas, y conectar con la base de
    datos de desarrollo.

    El mantener la independencia no significa que siempre se comience cada test
    con una base de datos vacía. Se puede crear un <fixture> en el <setUp> que
    inserte los datos comunes antes de cada test.


* NOTAS SOBRE TEST DE INTEGRACIÓN

    Al igual que con los controladores de la capa de presentación, estas clases
    son muy repetitivas y simples. Dirigir el desarrollo a un nivel tan granular
    podría no aportar mucho. La implementación es muy directa y está condiciona
    por la tecnología usada (JDBC). El desarrollo lo dirigirá más el framework
    (JDBC) que cualquier test unitario.

    Una decisión válida sería obviar la programación de test unitarios, y tratarlos
    como test de integración contra la base de datos de desarrollo. Se programan
    exactamente igual, pero ya no se pueden utilizar fácilmente durante el bucle
    TDD ya que no cumplen bien las propiedades <<FIRST>>, serían lanzados en
    períodos más espaciados (en cada commit).

    No obstante se puede seguir desarrollando con filosofía Test First, aunque
    no se lancen los tests tan continuamente que usando TDD riguroso.

    <<NOTA:>>

    Se verá en un ejercicio posterior que mediante una buena refactorización
    que elimine el código repetitivo se pueden crear test unitarios completamente
    aislados de la base de datos, que garantice por ejemplo la correcta gestión
    y liberación de recursos.

