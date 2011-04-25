 ------------------------------------------------------
 ${project.description}
 ------------------------------------------------------
 Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 ------------------------------------------------------

${project.name} - ${project.description}

    Creación de una implementación de la interfaz GlobalAddressBook basada en el
    acceso a DAOs de la capa de persistencia.

* RESTRICCIONES DE DISEÑO

  Para mostrar un ejemplo de cómo una lógica de negocio que necesita acceder
  a la capa de persistencia se puede desarrollar con TDD se tomarán las siguientes
  decisiones de diseño:

      * Los DAOs de la capa de persistencia no proporcionarán soporte para la
        comprobación de repetidos, validez de los contactos, etc.

        El código de comprobación de validez y requisitos de los contactos se
        sigue manteniendo como parte de la lógica de negocio, no se delega ni
        a los DAOs ni a un buen diseño de restricciones o reglas de integridad
        en el esquema de la base de datos.

      * Los DAOs de la capa de persistencia serán muy básicos, tan solo realizaran
        las tareas de almacenamiento y recuperación.


    <<NOTAS>>:\
    En este caso concreto quizás no sea una buena decisión de diseño ya que los
    DAOs podrían realizar muchas de las verificaciones que ahora existen en
    MemAddressBook. De hecho esta decisión de diseño provocará condiciones de
    carrera en la verificación de contactos repetidos.

    Sería más apropiado pasar mucha de dicha lógica al DAO, ya que se puede
    apoyar en facilidades de la base de datos, como restricciones, transacciones,
    etc. Quizás el servicio DbAddressBook ya no debería existir y se debería usar
    directamente el AddressBookDAO desde la capa de presentación.

    Se toma esta decisión tan solo para disponer un ejercicio que permita
    mostrar el desarrollo de una lógica de negocio que depende de objetos de
    la lógica de persistencia.


* IMPLEMENTACION

  La implementación y los tests de MemAddressBook se puede reutilizar, por
  tanto se toma la decisión de copiar MemAddressBook. La implementación se irá
  refactorizando a través de la copia incremental de los tests ya disponibles en
  TestMemAddressBook, adaptándolos a las necesidades de validación y verificación.

  Sigue siendo una aproximación TDD, antes de refactorizar la implementación
  copiada desde MemAddressBook, se copia uno de los tests de TestMemAddressBook,
  se ejecuta para verificar que falla, y se refactoriza tanto la implementación
  como el test.

  En una refactorización posterior podríamos eliminar la duplicidad de código
  entre DbAddressBook y MemAddressBook, en caso de que ambas implementaciones
  se quieran seguir usando.

  <<MOCKS>>\
  Para evitar el uso de mocks de los DAOs se deberían utilizar las dependencias
  reales, pero esto implicaría que se estarían realizando test de integración, ya
  que se estarían realizando las pruebas contra una base de datos.

  Esta sería una buena opción se consiguiera que los tests contra los DAOs
  reales cumplieran las propiedades <<FIRST>>.
