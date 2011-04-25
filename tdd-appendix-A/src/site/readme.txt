 ------------------------------------------------------
 ${project.description}
 ------------------------------------------------------
 Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 ------------------------------------------------------

${project.name} - ${project.description}

 Ejemplo para la explicación del principio DIP (Dependency Inversion Principle)
 de las propiedades SOLID.

 El principio DIP se resuelve de una manera muy desacoplada utilizando
 el paradigma IoC (Inversion of Control), que a su vez puede implementar
 con el patrón de diseño Direct Injection (DI)

===

 <<NOTA>>\
 La literatura sobre el tema muestra que no se ponen de acuerdo sobre la
 nomenclatura. Hay defensores de que IoC es un paradigma más genérico que
 DI, y que DI es una de las formas de resolverlo. Otros defienden que el
 responsable de su popularización o invención cambió de idea y lo que
 al principio llamó IoC lo cambió por DI.

===
 <<LEGUAJES SIN INTERFACES>>\
 El principio DIP habla de hacer independizar mediante la introducción
 de una abstracción intermedia sin detalles de implementación.\
 En muchos lenguajes esta abstracción es una Interfaz, en otros lenguajes
 se puede usar una abstracción equivalente, por ejemplo en C++ sería una
 clase que solo tiene métodos virtuales puros (ninguna implementación).


===

 <<FRAMEWORKS>>\
 La aplicación del paradigma o patrón IoC mediante Direct Injection no necesita
 de ningún framework. No es necesario usar ni Spring, ni el DI de JDK7, ni
 Plexus, etc. Es un patrón de diseño.


* RESUMEN

    *

     Ejemplo de un diseño con dependencias fuertemente acopladas, mostrando
     los inconvenientes que presenta y las ventajas que se consiguen
     refinando hacia un acoplamiento mucho más débil.

    *

     Ejemplo de desacoplamiento basado en DIP, en las que las clases se
     independizan mediante abstracciones (interfaces), de forma que los
     módulos de alto nivel no dependen de módulos de bajo nivel y los
     interfaces (o abstracciones) no dependen en detalles

     Es decir:

        * Las clases solo dependen de interfaces.

        * Los interfaces solo usan otros interfaces (tampoco dependen de detalles).

    *

     Desacoplando la instanciación de las implementaciones (los detalles), ejemplo
     de búsqueda en un servidor de objetos.

    *

     Desacoplando la instanciación de las implementaciones (los detalles), ejemplo
     de instanciación a través del patrón de diseño Factory.

    *

     Desacoplando la instanciación de implementaciones delegando la responsabilidad
     de configuración de dependencias a los clientes, usando Direct Injection.
