 ------------------------------------------------------
 ${project.description}
 ------------------------------------------------------
 Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 ------------------------------------------------------

${project.name} - ${project.description}

 Implementación de un test sobre código con multiples condicionales.

 Muestra como codificando un test ejercitaríamos todas las lineas
 de código de la misma manera que usando manualmente un depurador.

 Los tests se pueden codificar pensando lo que haríamos si lanzaramos
 la aplicación y depuraramos linea a linea con distintos datos de entrada.

  * <<IMPORTANTE>> (ESTO NO ES TDD)

    Este es un ejemplo introductorio de Unit Testing, será la última
    vez en el curso que implementemos el código antes que el test, a
    partir de ahora solo usaremos TDD.

    De hecho este ejercicio se ha implementado usando TDD, y tras
    terminar se han borrado las clases de test.

  * <<NOTA:>> Se ha implementado de forma un tanto extraña para explicar
              algunos conceptos de test code coverage.

* EJERCICIO

  Implementar los tests de la clase "Horoscope":

  * Las fechas se pueden introducir en tres formatos y el
    horoscopo detecta automaticamente el formato:

    * Corto: "12/10/2010"

    * Largo: "12 de octubre de 2010"

    * Día: "10" (sería el día 10 del mes y año actual)

  * Si el cumpleaños es en verano (Junio, Julio y Agosto):

    * Si es domingo la suerte es 10.

    * Si no es domingo la suerte es 8.

  * Si el cumpleaños es en invierno (Diciembre, Enero, Febrero):

    * Si es navidad (del 25 al 6) la suerte es 9.

    * Si no es navidad la suerte es 6.

  * El resto del año la suerte es 1.   