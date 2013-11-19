 ------------------------------------------------------
 ${project.description}
 ------------------------------------------------------
 Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 ------------------------------------------------------

${project.name} - ${project.description}

    Ejemplo introductorio y simplificado de aplicación del paso
    de refactoring. Enunciado siguiente ejercicio.

    * Métodos privados:

      ¿Se deben crear test para métodos privados?

      En java se pueden testear mediante la invocación con la API de
      <reflection>. Algunas librerías ofrecen facilidades para esto.
      Pero, ¿ES NECESARIO?

    * TODO: Se decide aumentar la funcionalidad del método getContact
      de forma que devuelva una excepción cuando el contacto no existe
      o el id es invalido.

* METODOS PRIVADOS

      No es necesario hacer tests directos de los método privados, es
      más lógico probarlos indirectamente a través de los métodos públicos
      o protegidos (API publica o API para subclases).

      Son detalles de implementación que dan soporte a los métodos públicos
      (la API). Desde la API se puede dirigir el test para conseguir cobertura
      de test completa en los privados.

      Aplicando TDD siempre se comienza desarrollando primero el test, por tanto
      siempre se estarán probando métodos públicos. Lo primero que se diseña
      es la API pública.

      Los métodos privados aparecen a partir de la implementación de los
      métodos públicos. Durante la implementación se puede decidir que
      conviene dividir el método publico en varios privados, o durante la
      refactorización se puede decidir extraer código repetido en un método
      privado.

      El código de un método privado sólo se puede utilizar desde el código
      de la clase en la que se ha declarado, es decir desde la API publica,
      por lo que los métodos privados deberían ser fácilmente testeables
      invocando los métodos públicos.

      Si un método privado se hace difícil de testear, quizás sea momento
      para plantearse si realmente ese método privado no representa una
      responsabilidad que debe ser extraída en otra clase.

      Ver: <SRP> de <<SOLID>>.