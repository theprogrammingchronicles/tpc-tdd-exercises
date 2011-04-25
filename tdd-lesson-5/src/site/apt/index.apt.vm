 ------------------------------------------------------
 ${project.description}
 ------------------------------------------------------
 Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 ------------------------------------------------------

${project.name} - ${project.description}

    * Ejercicios y ejemplos de técnicas avanzadas de mocking.

    []

    * Aplicación de TDD en frameworks rígidos no preparados para el unit testing.

    []

    * Aplicación de TDD en la capa de presentación de una aplicación Web.

    []


ADVANCED MOCKING & TDD

    Ejercicios de diferentes tipos de stub, mock, fake y spy. Validación por
    estado o interacción. Beneficios, ventajas y sus peligros. Cuando usarlo
    y cuando evitarlo.


* RESUMEN

   * Validación por Interacción mediante la creación automática de
     los mocks exigidos por el framework de ejecución (en este caso
     son objetos del contenedor).

   []

   * Ejemplos varios de refactorización incremental. Tanto en el
     código funcional como en el código de test.

   []

   * Mocks que devuelven Mocks, que devuelven Mocks, …

   []

   * Codificación de Spies: (Mocks Parciales)

     Simulación de comportamientos heredados del framework
     en el Object Under Tests.

   * Spies Automaticos (Mocks Parciales)

   []

   * La lógica de presentación permite identificar la necesidad de refactorizar
     parte de la lógica de negocio (refactorizar proyectos ya terminados y probados).

   []

   * Ejemplos de desarrollo con TDD del procesamiento de formularios.

   []

   * Refactoricación de código duplicado creando librerías de utilidades
     de test:

     Reutilización para todo el equipo de desarrollo (fijar estándares en el
     equipo de desarrollo: extreme programming).

   * Se empieza a visualizar la aparición de nuestro propio framework
     en la arquitectura (nacido de la refactorización).

   []

   * Codificación manual de fakes:

     Simplifican el código de test sin hacer uso de librerías de generación de mocks.

   * Utilizando librerías especializadas de testing, que ofrecen
     mocks o fakes ya programados para entornos específicos.


HEAVYHEIGHT FRAMEWORKS & WEB PRESENTATION LAYER

    Desarrollo de componentes ejecutados en un framework basado en
    componentes de contenedor.

    Aunque los ejemplos se basan en el desarrollo de aplicaciones web
    con tecnología Java, tratan de transmitir las dificultades y soluciones
    para la aplicación de TDD en entornos no preparados para el unit
    testing (técnicas no ligadas al desarrollo de servlets).

    También funcionan como ejemplos de como aplicar TDD en el desarrollo
    de la capa de presentación de una aplicación web.

        * Se mostrará que mediante un reparto apropiado de responsabilidades,
          la lógica más comprometida puede tener una buena cobertura de test.

        []

        * Los ejercicios muestran que la lógica de presentación de los controladores
          también puede ser desarrollada mediante TDD.

          Aunque siguiendo la filosofía de trasladar responsabilidades a la lógica
          de negocio, quedan con un código tan repetitivo, que conviene plantear
          si no sería mejor delegar las pruebas de los controladores a los test de
          integración.

          <<¿Qué está realmente dirigiendo el desarrollo de la lógica de los
             controladores? ¿Las pruebas unitarias o la arquitectura que impone
             el propio framework utilizado?>>


* DISEÑO DE LA CAPA DE PRESENTACIÓN

    Arquitectura MVC usando sólo tecnologías nativas de J2EE:

        * Modelo: JavaBeans (Java-Old Plain Objects).

        * Controlador: Servlets.

        * Vista: JSP (sin lógica: solo JSTL + EL).


** <<NO SE USA FRAMEWORKS MVC DE TERCEROS>>

    Se desarrollara una arquitecta MCV sin el uso de frameworks
    de controlador frontal como Structs o SpringMVC (solo J2EE).


** <<RECORDATORIO MVC>>

    Una arquitectura MVC facilita el uso de TDD. Los controladores no
    deben tener absolutamente nada de lógica de negocio. Solo son
    "wrappers" de la capa de negocio.

    La lógica de presentación se reduce a lo mínimo, y por tanto
    requiere menos unit testing.

        * Un controlador <<NUNCA>> debe tener lógica de negocio.

        * Una vista <<NO>> debería tener lógica ejecutable.

