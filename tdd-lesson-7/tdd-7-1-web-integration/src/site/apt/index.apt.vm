 ------------------------------------------------------
 ${project.description}
 ------------------------------------------------------
 Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 ------------------------------------------------------

${project.name} - ${project.description}

    Desarrollo de la parte cliente de la capa de presentación (vistas
    con JSP sin lógica)

    El desarrollo de las vistas muestra nuevas necesidades de código
    funcional en los controladores. Toda la lógica adicional se desarrolla
    como siempre usando TDD con tests unitarios aislados.

    Al abordar el despliegue, configuración e integración de nuestra
    aplicación vemos que falta código ejecutable por implementar.

     * Este código por supuesto se desarrolla usando TDD.

     * Todo código ejecutable siempre tendrá al menos las pruebas
       unitarias que dirigieron su codificación.


* NOTAS SOBRE LA INTEGRACION

    Hasta ahora teníamos un sistema que "parece" correcto a nivel de
    pruebas unitarias. ¿Pero asegura esto que todo funciona al 100%?

    Despues de implementar las vistas y la la nueva funcionalidad de 
    configuración de vistas en controladores, todo parece funcionar
    correctamente. 

    Pero si intentamos añadir contactos duplicados, la Interfaz de Usuario
    no reporta correctamente los errores (pantalla demasiado genérica).


     *

       Los tests unitarios no han mostrado fallos ocultos. Es decir, en este
       caso el test visual de UI nos ha mostrado información que los tests
       unitarios no estaban mostrando.

     *

       Aunque en este caso el fallo se debe a que se nos olvidó codificar
       parte de las pruebas de aceptación en los controladores (capa de
       presentación).


     *

       Esto se habría evitado si la implementación de la lógica de presentación,
       de los controladores, hubieramos dirigido los tests unitarios usando
       los tests de aceptación al igual que hicimos con la logica de negocio.

       Este es un ejemplo de que aunque controlemos fallos en capas internas
       mediante tests unitarios, los mismos fallos podrían tener que controlarse
       en capas superiores, y por tanto aparecer en los tests.


    Los tests de integración, tests de sistema y tests de interfaz de
    usuario siguen siendo necesarios al margen del uso de TDD.

    No obstante con TDD vamos a conseguir que el número de defectos
    en la integración sea menor (y por tanto el esfuerzo para solucionarlos).

    Los tests mencionados, en teoría están fuera del bucle incremental
    de TDD (recordatorio de la teoría).

    El problema principal es que los tests de este tipo necesitan un
    soporte extra especifico de la tecnología utilizada. En Java por
    ejemplo hay varias herramientas disponibles (al margen de herramientas
    especificas para Web UIs como Seleniun).

    También suelen ser demasiado lentos para TDD. Se recuerda que en TDD
    necesitamos lanzar las pruebas cada pocos minutos para programar
    incrementalmente. No es raro ver que un día los hemos lanzado los
    tests hasta unas 200 veces.

    También son tests más complejos, están mas separados del código, son
    más sensibles a cambios de requisitos, etc. Por lo que al final
    requerirán esfuerzos extras de mantenimiento (unos esfuerzos que
    en TDD pasan desapercibidos ya que surgen de forma natural).
