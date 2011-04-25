 ------------------------------------------------------
 ${project.description}
 ------------------------------------------------------
 Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 ------------------------------------------------------

${project.name} - ${project.description}

 Introducción a Automated Unit Testing

 Los ejercicios 1.x son los unicos que se realizarán sin utilizar TDD, tan
 solo para mostrar conceptos muy básicos de unit testing.

 A partir del ejercicion 2 siempre se utilizará TDD para los desarrollos.

* Resumen

  * Ejemplo sencillo de un Test Unitario Automatizado sin hacer uso de
    Test Driven Development.

  []

  * Mostrar un ejemplo de que incluso es posible programar tests
    sin usar librerías xUnit (viendo las desventajas).

  []

  * Mostrar un ejemplo con JUnit (practicamente igual que sin JUnit
    pero con algunas ventajas importantes).

  []

  * Programando un test unitario (cobertura del código).


** NOTAS JAVA

   Unit Tests con Maven:

   * Por defecto Maven siempre ejecuta los tests al realizar un build
     (<<<mvn package>>>), interrumpiendo el build si los tests fallan.

   []

   * Los ejemplos del curso incluyen informes de <Test Code Coverage> usando
     la herramienta "cobertura" {{http://cobertura.sourceforge.net/}} al
     generar la documentación del proyecto (<<<mvn site>>>).
