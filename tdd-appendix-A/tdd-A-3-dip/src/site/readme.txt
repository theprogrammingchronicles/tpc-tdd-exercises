 ------------------------------------------------------
 ${project.description}
 ------------------------------------------------------
 Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 ------------------------------------------------------

${project.name} - ${project.description}

 Aplicando un diseño basado en Dependency Inversion Principle.

 Desacoplando la clase de la instanciación de los detalles de implementación es
 decir, de la instanciación de clases que implementan la interfaz de la
 que dependenden.

 Solución basada en la busqueda de instancias en un servicio de
 directorio de nombres (Ej: JNDI).

 AUN SE PUEDE DESACOPLAR MAS

 Ahora no dependemos en absoluto de RandomIdGenerator, pero nos estamos
 acoplando al servicio de nombres, hemos cambiado una dependencia por
 otra.

 Aun se puede desacoplar más.

 Configuramos en el contenedor JNDI una factoria de instancias o
 un singleton, que implementa IdGenerator. Si queremos cambiar
 una implementación por otra cambiamos la configuración del
 contenedor de JNDI.

 ¿Pero y si necesitamos un sistema con dos MemAddressBooks y
  cada uno con un IdGenerator diferente?

