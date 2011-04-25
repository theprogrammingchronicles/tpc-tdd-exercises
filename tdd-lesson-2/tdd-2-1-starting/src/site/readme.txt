 ------------------------------------------------------
 ${project.description}
 ------------------------------------------------------
 Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 ------------------------------------------------------

${project.name} - ${project.description}

  Proyecto de enunciado del ejercicio.

   [[1]] Se selecciona el User Story <Añadir Contactos>.

   [[2]] Se selecciona algún test de aceptación de dicho User Story.

   [[3]] Se idean tests unitarios de nivel más granular, a partir de un
         diseño preliminar:

            El servicio se implementará en la clase GlobalAddressBook. Deberá
            disponer de un método <addContact> y un método <getAll> que
            permita verificar que los contactos se añaden correctamente.

   [[4]] Primer test:

            GlobalAddressBook permite añadir un Contacto (Contact), y se verifica
            que se añade correctamente implementando y usando <getAll>.

   [[5]] Segundo test:

             Implementando <addContact> se deduce que será útil devolver un "id",
             que será utilizado en el método <getContact>. GlobalAddressBook debe
             devolver un id que permita recuperar un contacto usando dicho id.

             Se han localizado detalles de implementación. Los test de aceptación no
             deben mencionar nada de ids, ya que usan el vocabulario del negocio.
             A nivel de implementación se localizan nuevas necesidades y por tanto
             aparecen nuevos test unitarios al margen de los test de aceptación.

