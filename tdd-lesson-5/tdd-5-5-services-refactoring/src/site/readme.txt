 ------------------------------------------------------
 ${project.description}
 ------------------------------------------------------
 Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 ------------------------------------------------------

${project.name} - ${project.description}

  Refactorización de las librerías de la lógica de negocio o servicios, al
  empezar a usarlos desde la capa de presentación (controladores) nos damos
  cuenta de que es mejor aplicar una refactorización según DIP.

    * Extraer GlobalAddressBook como un interface.

    []

    * Retomar un proyecto anterior y refactorizar ahora es sencillo y fiable, <<fin
      del "no lo toco por si acaso">>.

    []

    * En este ejemplo la refactorización es muy ligera, por lo que ni fallan los tests.

    []

    * <<Nota sobre IoC:>>

      Las interfaces no deben incluir los métodos o propiedades de configuración.
      Son detalles de implementación, otra implementación de AddressBook podría no
      requerir el IdGenerator.

