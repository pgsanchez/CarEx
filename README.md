INTRODUCCIÓN
============

GastaCar es una aplicación pensada para apuntar los gastos que tienes en tu coche (o en tus coches). Luego podrás verlos de manera resumida y ver con más claridad cuanto gastas cada año en el coche.

GastaCar te dará información sobre el gasto anual en combustible, reparaciones, ITV, Seguro e Impuestos. Ten en cuenta una cosa; esta, como el resto de aplicaciones parecidas, solo te empezará a resultar útil a medio/largo plazo, cuando los datos que hayas ido metiendo en la aplicación sean suficientes como para que la información que te da sea un reflejo real de tus gastos en el coche.

En el market podrás encontrar varias aplicaciones como esta. No creo que esta sea la mejor ni la más completa. Entonces, ¿en qué se diferencia del resto? Pues simplemente en que la he desarrollado para cubrir mis necesidades, ya que el resto siempre tenían algo que no me terminaba de convencer. Esta aplicación es mas simple y mas sencilla que el resto; algunas tienen tanta información que abruma y, sinceramente, hay mucha información que yo no utilizo. Además esta aplicación es completamente gratuita, no tiene publicidad ni accede a tus fotos o datos ni a los de tus contactos. Como digo la he desarrollado para mí, y no necesito sacar dinero con ella. Con que me sea útil es suficiente. Y si a ti también te resulta útil, pues mejor.

Para más información puedes leer el blog de mi web: [pgsanchez.net](https://pgsanchez.net)

Capturas de pantalla
====================

![alt tag](https://github.com/pgsanchez/CarEx/blob/master/design/PantallaInicial.png)

<br/>

![alt tag](https://github.com/pgsanchez/CarEx/blob/master/design/Repostaje.png)

<br/>

![alt tag](https://github.com/pgsanchez/CarEx/blob/master/design/Mantenimiento.png)

<br/>

![alt tag](https://github.com/pgsanchez/CarEx/blob/master/design/Informes.png)

<br/>

![alt tag](https://github.com/pgsanchez/CarEx/blob/master/design/Graficas.png)

<br/>

VERSIONES
---------

3/9/2018 Primera versión Beta. Beta 0.1 Tiene lo básico para ir metiendo datos; repostajes y visitas al taller.

**12/10/2019 Primera versión FINAL.  GastaCar 1.0**


DESARROLLO (para programadores)
-------------------------------

Si estás interesado en saber como funciona internamente esta aplicación, o en realizar modificaciones, te interesa leer esta parte. Te servirá para luego entender mejor el código.

**¿Cómo funciona internamente?**

La aplicación mantiene internamente tres listas, una de coches, una lista de repostajes y otra de mantenimientos. Estas dos últimas son las importantes.

Un repostaje es, como su propio nombre indica, una anotación que se hace en la aplicación cada vez que echas gasolina al coche.

Un mantenimiento es una anotación que se hace cada vez que llevas el coche al taller, o que tú mismo le reparas alguna cosa.

Repostaje y Mantenimiento son dos objetos que tienen una serie de atributos (que se describirán más adelante) para guardar toda la información relevante de esas acciones.

Cualquiera de las dos acciones se puede realizar sobre distintos coches (si tienes más de uno). Para hacer esto, también habrá un sistema de gestión de coches que permitirá añadir, borrar y modificar coches. El coche solo tiene tres datos; un nombre para reconocerlo, y un icono asociado para que sea más fácil encontrarlo en las listas y un color para el icono.

El funcionamiento básico de la aplicación es ir guardando y manteniendo las dos listas de repostajes y mantenimientos. El resto del funcionamiento es simplemente hacer cálculos con ellas y mostrar distinta información.

Para no perder las listas, hay una parte de la aplicación que se encarga de guardar esos datos en una BD.

Cada vez que haces un repostaje se guardan los datos referentes a ese repostaje en la aplicación. Lo mismo se hace cuando realizas un mantenimiento en el coche. Además, se pueden modificar, por si hubiera habido algún error al meterlos. También se pueden borrar, aunque en la práctica este caso se dará en raras ocasiones.

Resumiendo: toda la aplicación se basa en tres listas de datos (Coches, Repostajes y Mantenimientos), aunque las dos últimas son las importantes.



*P.D.Esta documentación irá creciendo a medida que lo haga el proyecto.*
