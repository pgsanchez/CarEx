INTRODUCCIÓN
============

GastaCar es una aplicación pensada para apuntar los gastos que tienes en tu coche (o en tus coches). Luego podrás verlos de manera resumida y ver con más claridad cuanto gastas cada año en el coche.
En el market podrás encontrar varias aplicaciones como esta. No creo que esta sea la mejor ni la más completa. Entonces, ¿en qué se diferencia del resto? Pues simplemente en que la he desarrollado para cubrir mis necesidades, ya que el resto siempre tenían algo que no me terminaba de convencer. Esta aplicación es mas simple y mas sencilla que el resto; algunas tienen tanta información que abruma y, sinceramente, hay mucha información que yo no utilizo. Además esta aplicación es completamente gratuita y no tiene publicidad. Como digo la he desarrollado para mí, y no necesito sacar dinero con ella. Con que me sea útil es suficiente.

VERSIONES
---------

3/9/2018 Primera versión Beta. Beta 0.1 Tiene lo básico para ir metiendo datos; repostajes y visitas al taller. 

DESARROLLO (para programadores)
-------------------------------

Si estás interesado en saber como funciona esta aplicación, o en realizar modificaciones, te interesa leer esta parte. Te servirá para luego entender mejor el código.

##¿Cómo funciona internamente?##

La aplicación mantiene internamente dos listas, una lista de repostajes y otra de mantenimientos.

Un repostaje es, como su propio nombre indica, una anotación que se hace en la aplicación cada vez que echas gasolina al coche.

Un mantenimiento es una anotación que se hace cada vez que llevas el coche al taller, o que tú mismo le reparas alguna cosa.

Repostaje y Mantenimiento son dos objetos que tienen una serie de atributos (que se describirán más adelante) para guardar toda la información relevante de esas acciones.

Cualquiera de las dos acciones se puede realizar sobre distintos coches (si tienes más de uno). Para hacer esto, también habrá un sistema de gestión de coches que permitirá añadir, borrar y modificar coches. El coche solo tiene dos datos; un nombre para reconocerlo, y un icono asociado para que sea más fácil encontrarlo en las listas.

El funcionamiento básico de la aplicación es ir guardando y manteniendo estas dos listas de repostajes y mantenimientos. El resto del funcionamiento es simplemente hacer cálculos con ellas y mostrar distinta información.
Para no perder las listas, hay una parte de la aplicación que se encarga de guardar esos datos en una BD.

Cada vez que haces un repostaje se guardan los datos referentes a ese repostaje en la aplicación. Lo mismo se hace cuando realizas un mantenimiento en el coche.Además, se pueden modificar, por si hubiera habido algún error al meterlos. También se pueden borrar, aunque en la práctica este caso se dará en raras ocasiones.

Resumiendo: toda la aplicación se basa en tres listas de datos (Repostajes, Mantenimientos y Coches), aunque las dos primeras son las importantes.

**Descripción de la Base de Datos**

Habrá tres tablas en la BD; una para repostajes, otra para mantenimientos, y otra para los coches.

Habrá una serie de clases que se encargan del mantenimiento de esos datos en la BD. Estas clases tienen unas funciones, que se pueden llamar desde la clase principal de la aplicación, y que sirven para realizar las funciones básicas CRUD (Añadir, Modificar, Borrar y Obtener datos).

Cuando arranca, la aplicación lee las tablas de la BD y con esos datos carga las listas de repostajes, mantenimientos y coches.

La pantalla inicial muestra en la ventana la lista de repostajes (por ser mas frecuentes que las visitas al taller), pero será un TabView, y en la segunda pestaña estarán los mantenimientos, así que será fácil pasar de una lista a la otra.

Esta lista de repostajes estará ordenada por fecha, apareciendo los repostajes más nuevos en la parte superior (independientemente del coche que sea). Cada elemento muestra información básica del repostaje; coche, km totales y parciales, importe, lugar donde se repostó…

Esta ventana tiene un botón para añadir un nuevo repostaje. Este botón lleva a una nueva ventana para rellenar los datos relativos a un nuevo repostaje, y da la opción de guardarlos (en este caso se guardarían en la BD y se mostrarían en la lista) o de cancelar, en cuyo caso se vuelve atrás sin guardar nada.

Además, tocando en un elemento de esta lista se va también a su página de detalles, en la que se da la opción de modificar los datos y guardarlos en la BD.

El funcionamiento de la lista de mantenimientos es igual al de la lista de repostajes.

#####*Nota importante para programadores*
La forma de proceder al **añadir**, **modificar**, o **borrar** un elemento es: 
1. Al dar al botón de guardar, se hace el cambio en la BD
2. Se borra la lista
3. Se lee de nuevo la lista de la BD y se muestra

Una vez se tiene esto, sacar gráficas, resúmenes o informes de gastos es solo cuestión de echarle imaginación en lo que se quiera/pueda obtener con esos datos.

*Esta documentación irá creciendo a medida que lo haga el proyecto.*
