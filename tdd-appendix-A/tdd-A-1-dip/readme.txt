 ------------------------------------------------------
 ${project.description}
 ------------------------------------------------------
 Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 ------------------------------------------------------

${project.name} - ${project.description}

    Ejemplo de un diseño con dependencias fuertemente acopladas, mostrando
    los inconvenientes que presenta y las ventajas que se consiguen
    refinando hacia un acoplamiento mucho más débil.


VIOLACIÓN DE DEPENDENCY INVERSION PRINCIPLE

   * La clase GlobalAddressBook depende directamente de las clases de
     bajo nivel (dependencias) --> RandomIdGenerator.

   *

     Depende directamente de los detalles, con lo que se dificulta la
     tarea de ¿qué pasa si queremos cambiar RandomIdGenerator por una
     implementación basada en ficheros o base de datos?.

   *

     Es una clase difícil de testear. Imaginemos que RandomIdGenerator
     es una clase muy complicada llena de funcionalidad, por tanto nos
     interesa poder probar GlobalAddressBook sin su dependencia real,
     probarla de forma aislada para no complicar el descubrimiento de
     bugs en esta clase. RandomIdGenerator ya tiene sus propios tests
     y sabemos que es válida, pero aquí queremos validar única y
     exclusivamente GlobalAddressBook, y no queremos que futuros fallos en
     RandomIdGenerator nos dificulten los test de esta clase.
     (<Ver: TestGlobalAddressBook>)

   *

     Tenemos que cambiar GlobalAddressBook ("que es muy compleja y mantenida
     por varios desarrolladores"). La funcionalidad actual podría verse
     afectada por ese cambio. Probablemente tengamos que rehacer los tests.

 Deberíamos hacer que GlobalAddressBook dependa de una abstracción es decir,
 de una interfaz.