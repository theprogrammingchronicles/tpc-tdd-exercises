 ------------------------------------------------------
 ${project.description}
 ------------------------------------------------------
 Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 ------------------------------------------------------

${project.name} - ${project.description}

 Aplicando un diseño basado en Dependency Inversion Principle.

 Desacoplando la instanciación mediante Direct Injection

 La responsabilidad de crear y configurar las dependencias se eleva a
 un nivel superior. El cliente de GlobalAddressBook decide que dependencias
 utilizará la instancia que necesita, es decir, configura parte de su
 comportamiento en función de las caracteristicas deseadas.

 En este entorno la clase MemGlobalAddressBook o FakeGlobalAddressBook son
 completamente independientes de cualquier implementación concreta de
 IdGenerator, solo saben que se usará un IdGenerator, cualquier implementación
 existente le servirá, y con cualquiera funcionará correctamente, es el
 cliente el que decide que le conviene.

 <<MUY IMPORTANTE>>\
 La configuración de dependencias es un detalle de implementación, MemGlobalAddressBook
 necesita un IdGenerator, pero DBGlobalAddressBook no lo necesita. Nunca debemos
 meter métodos de configuración en la interfaz GlobalAddressBook, sería meter
 detalles de implementación. Solo metemos lo generico y lo que es parte real
 de la API y no de la implementación.

TESTEABLE

 Ahora MemAddressBook está mejor preparada para el unit testing, podemos
 aislar los tests de las dependencias, no los basamos ni en RandomIdGenerator
 ni en IncrementIdGenerator, solo queremos probar que el código de esta clase
 es válido sin importarnos el de otras clases.