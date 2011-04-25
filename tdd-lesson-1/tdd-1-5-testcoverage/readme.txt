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

    * <<NOTAS SOBRE LA SOLUCIÓN>> (Ver TestHoroscope):

        Aparte de los temas de cobertura de código, es muy importante fijarse
        en el hecho de que en la clase TestHoroscope, probamos UNICA
        y EXCLUSIVAMENTE la clase Horoscope, ya que la clase SmartDateParser
        ya tiene sus propios tests y asumimos que funciona bien.

        Es un antipatron el convertir un test de una clase en un test de
        sus dependencias. Por ejemplo, probando en Horoscope con todos los
        formatos de fechas. Al final el test de una clase acaba
        convirtiendose en test de las dependencias. Esto hace los tests
        más complejos a la vez que redundantes. Los tests de formato deben
        estar en la clase TestSmartDateParser.

    * <<IMPORTANTE>> (ESTO NO ES TDD):

        Este es un ejemplo introductorio de Unit Testing, será la última
        vez en el curso que implementemos el código antes que el test, a
        partir de ahora solo usaremos TDD.

        De hecho este ejercicio se ha implementado usando TDD, y tras
        terminar se han borrado las clases de test.

    * <<NOTA:>> Se ha implementado de forma un tanto extraña para explicar
                algunos conceptos de unit testing.