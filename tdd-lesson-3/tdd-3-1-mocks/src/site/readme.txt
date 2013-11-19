 ------------------------------------------------------
 ${project.description}
 ------------------------------------------------------
 Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 ------------------------------------------------------

${project.name} - ${project.description}

    Proyecto de enunciado del ejercicio.

    <<Refactorización:>>

     En base a <<SRP>> (<Single Responsability Principle>) se extraerá la
     responsabilidad de generación de identificadores a una clase nueva.

     Por un lado se decide que la generación de ids es una responsabilidad
     que no pertenece a GlobalAddressBook./
     Por otro lado extrayendo la funcionalidad se permite su reutilización.


    <<Mocks/Stubs:>>

      Suponemos que la nueva funcionalidad va a ser implementada por
      otro desarrollador./
      Podemos esperar a que la tenga acabada, pero para ver un ejemplo de
      stubs/mocks vamos a refactorizar sin disponer de la implementación.


* TO DO:

     Modificar los tests para que puedan funcionar sin disponer aun
     de la clase IdGenerator (el código de generación de ids se ha
     eliminado de la clase y se supone que un IdGenerator que aun
     no tenemos implementará esta funcionalidad).

     <<PROBLEMA>>: ¿Como se puede hacer que GlobalAddressBook utilice durante
                    los tests un Mock de IdGenerator sin incluir el Mock en
                    la implementación del código funcional?
