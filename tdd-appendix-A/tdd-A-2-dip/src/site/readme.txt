 ------------------------------------------------------
 ${project.description}
 ------------------------------------------------------
 Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 ------------------------------------------------------

${project.name} - ${project.description}

 Aplicando un diseño basado en Dependency Inversion Principle.

 En una primera aproximación mostramos como se desacoplan implementaciones
 introduciendo una abstracción intermedia sin detalles de implementación.

 En lenguajes con soporte se pueden usar interfaces, en otros lenguajes se
 pueden usar artefactos equivalentes, como en C++ el uso de clases con
 todos los método virtuales puros.

 De esta forma las clases ya no dependen directamente de los detalles de
 la implementación, si no de interfaces. Y los interfaces tampoco dependen
 de detalles de implementación ya que todos los métodos utilizan a su vez
 otras interfaces.

  *

    Las clases solo dependen de interfaces (o abstracción equivalente)

  *

    Las interfaces solo utilizan otros interfaces (que tampoco tienen detalles de implementación)


 Ahora podemos cambiar la implementación de las dependencias sin afectar
 absolutamente a nada del código, con solo entregar clases que implementen
 los mismos interfaces.

SIGUIENTE PROBLEMATICA

 Por muchos interfaces intermedios que se hayan introducido vemos que
 la clase GlobalAddressBook tiene que seguir teniendo acceso a la
 clase con la implementación utilizada, para instanciarla.

 Por tanto aunque los cambios tendrán menos impactos, aun sigue siendo necesario
 modificar la clase AddressBook para intercambiar la implementación.