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

 Solución basada en el patron de diseño Factory

 AUN SE PUEDE DESACOPLAR MAS

 Ahora no dependemos en absoluto de RandomIdGenerator, pero nos estamos
 acoplando a un servicio de instanciación

 De hecho ahora podríamos tener incluso más trabajo ya que el sistema
 de busqueda de instancias lo implementamos nosotros.

 ¿Pero y si necesitamos un sistema con dos MemAddressBooks y
  cada uno con un IdGenerator diferente?
