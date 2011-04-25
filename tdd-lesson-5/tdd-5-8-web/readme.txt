 ------------------------------------------------------
 ${project.description}
 ------------------------------------------------------
 Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 ------------------------------------------------------

${project.name} - ${project.description}

    Ejemplo de uso de mocks o fakes para componentes de servlets
    disponibles en librerías de testing.

    Existen librerías de terceros que ya tienen implementados fakes como
    los que hemos codificado en el ejercicio 5.7. Estos fakes suelen
    ser muy completos y nos ahorramos su codificación.


    * Spring Tests Utilities: Proporciona una librería de utilidades de testing.

        Ofrece mock/fakes completos y configurables, que implementan
        toda la profundidad de las dependencias, devolviendo en los
        métodos otros mock/fakes ya preparados para la verificación.

        Además de implementar todas las dependencias del contenedor
        en forma de mocks/fakes, realiza validaciones adicionales con
        asserts internos para asegurar la semántica de los servlets.


    * <<IMPORTANTE>> (No confundir el Spring Framework).

        Esto no significa que ahora el proyecto use Spring Framework,
        Spring MVC o cualquiera de sus utilidades. El código funcional
        no incluye nada de Spring Framework, ni siquiera la dependencia.

        Solo se usa el jar de testing de Spring como dependencia de testing
        (scope test). El jar ni siquiera será incluido en el WAR.

        Hay que tener en cuenta que Spring es un conjunto de utilidades,
        no es sólo MVC, IoC, AOP, etc, es un conjunto de todo eso y más.