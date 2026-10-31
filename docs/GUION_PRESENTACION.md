# Guion de presentación

José María Rodríguez Cruz · Duración orientativa: 7-9 minutos más la demostración. Practica y adapta estas frases a tu manera de hablar.

## 1. Sistema de Gestión de Productos

Soy José María Rodríguez Cruz. Voy a presentar un sistema de gestión de productos desarrollado con Java y Spring Boot. El proyecto une los fundamentos de programación orientada a objetos con una API REST. Permite crear, consultar, modificar y eliminar productos. También permite buscar por nombre y conocer qué producto tiene más unidades. Mostraré primero cómo está organizado y después una demostración de sus operaciones.

## 2. Qué resuelve la aplicación

El usuario puede trabajar con productos genéricos, como un cuaderno, y electrónicos, como un portátil. Ambos tienen nombre, precio y cantidad. Los electrónicos añaden marca y modelo. La aplicación ofrece las cuatro operaciones CRUD y dos consultas del inventario. Se utiliza Postman como cliente, por lo que no es necesaria una página web. H2 conserva los productos mientras está funcionando el servidor; al detenerlo, los datos desaparecen porque la base es en memoria, tal como pide el enunciado.

## 3. POO aplicada al modelo

Producto es la clase base. Sus atributos son privados y se consultan o modifican mediante getters y setters: esto es encapsulación. ProductoElectronico utiliza extends para heredar esos datos y añadir marca y modelo. Sobrescribe mostrarInfo y descripcion. Cuando un bucle recorre referencias de tipo Producto, se ejecuta la implementación correspondiente a cada objeto: eso muestra polimorfismo. La interfaz Describible representa la abstracción: promete una descripción, sin obligar al consumidor a conocer la clase concreta. Para los precios se usa BigDecimal y se admiten hasta dos decimales.

## 4. Del array al inventario dinámico

La demostración comienza con un array de productos. Un bucle los añade al ArrayList de Inventario. El array tiene un tamaño fijo, mientras que la lista permite añadir elementos. Inventario muestra cada producto, busca coincidencias en el nombre y recorre los elementos para encontrar el de mayor cantidad. Si hay empate, conserva el primero. En la API se crea un inventario temporal con los productos que devuelve H2. No hay otra lista global que pueda quedar desactualizada respecto a la base de datos.

## 5. Cómo viaja una petición

Una petición llega a ProductoController. El controlador valida el cuerpo recibido y llama al servicio. ProductoService aplica las reglas, por ejemplo que un electrónico tenga marca y modelo. Después utiliza ProductoRepository, que extiende JpaRepository y realiza el acceso a H2. Las consultas de búsqueda y máximo pasan por Inventario. Finalmente se devuelve un DTO, que Spring convierte a JSON. Las operaciones que modifican datos se ejecutan dentro de una transacción. Esta separación evita concentrar toda la lógica en el controlador.

## 6. Un contrato REST sencillo

GET permite consultar. POST crea un producto y responde con 201 y la ruta del recurso creado. PUT actualiza un producto existente y exige sus datos editables. DELETE lo elimina y devuelve 204 sin contenido. Los identificadores son Long positivos. Además del CRUD, se puede filtrar usando el parámetro nombre y consultar mayor-stock. La colección Postman incluida guarda los ids de las altas para que las siguientes peticiones no dependan de un número fijo.

## 7. Las entradas inválidas se rechazan

No se permiten nombres vacíos, precios negativos ni cantidades negativas o fraccionarias. Un producto sin existencias puede tener cantidad cero. Para productos electrónicos, marca y modelo son obligatorios. Un error en los datos devuelve 400; un producto inexistente devuelve 404. También he decidido impedir el cambio de tipo después del alta y responder 409. Antes de modificar una entidad se valida el conjunto de datos. Las pruebas verifican que una actualización rechazada mantiene intacto el producto guardado.

## 8. Monolito frente a microservicios

En un monolito, las partes del servidor se distribuyen juntas. Aquí todo se construye como un JAR. Esto facilita arrancar, depurar y probar un proyecto pequeño. En una alternativa de microservicios, productos y ventas podrían desplegarse por separado y comunicarse mediante HTTP o mensajes. Esa separación también introduce coordinación y fallos de red. Para el alcance del módulo, un monolito es suficiente. Los servicios de ventas del dibujo son una comparación; no forman parte del código entregado.

Fuente: https://martinfowler.com/articles/microservices.html

## 9. MVC y arquitectura hexagonal

MVC separa modelo, vista y controlador. Este proyecto usa Spring MVC para exponer una API: RestController devuelve JSON y no utiliza una plantilla HTML. En arquitectura hexagonal, los casos de uso se aíslan de las tecnologías mediante puertos y adaptadores. Un controlador HTTP podría ser un adaptador de entrada y JPA, uno de salida. No he implementado una arquitectura hexagonal completa: el modelo actual contiene anotaciones de JPA. Para evolucionar hacia ella, habría que separar ese modelo y definir los puertos propios. Monolito y hexagonal no son opciones incompatibles: se refieren a aspectos diferentes.

Fuentes: https://alistair.cockburn.us/hexagonal-architecture y https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods/responsebody.html

## 10. Patrones que aparecen en el código

El patrón Repository ofrece una interfaz para acceder a los productos guardados. Service Layer concentra las operaciones de negocio y las transacciones. Los DTO separan el JSON de entrada y salida de las entidades JPA; por ejemplo, el cliente no puede elegir el id en el cuerpo de una petición. La inyección por constructor permite que Spring proporcione el servicio y el repositorio. No se añaden patrones por decorar el proyecto: cada uno ayuda a separar una responsabilidad y facilita comprobar su comportamiento.

## 11. Verificación del comportamiento

Se incluyen siete pruebas del modelo y nueve de la API con Spring, MockMvc y H2. Comprueban las operaciones CRUD, la recuperación del producto electrónico desde la base de datos, la búsqueda, el mayor stock y los casos de error. También se ejecuta el JAR real y se envían peticiones HTTP para comprobar el arranque y el contrato. Estas verificaciones se hicieron en el entorno de preparación. En mi ordenador debo ejecutar de nuevo clean verify y la demostración antes de entregar.

## 12. Demostración y entrega

Para la demostración, primero ejecuto el modo demo y explico la herencia y el inventario. Después arranco la API e importo la colección de Postman. Creo un cuaderno y un portátil, consulto la lista y el mayor stock, actualizo el portátil y provoco un error con un precio negativo para comprobar que se rechaza. Finalmente elimino los productos. El proyecto está pensado para aprendizaje local. Como mejoras futuras se podrían añadir autenticación, paginación y una base de datos en disco. Para completar la entrega debo publicar los archivos en GitHub y pegar el enlace real en el documento adjunto.

## Secuencia de demostración

1. En PowerShell: `.\mvnw.cmd clean verify`.
2. Ejecuta `java -jar target/gestion-productos-1.0.0.jar --demo`. Explica la salida.
3. Ejecuta `java -jar target/gestion-productos-1.0.0.jar` y deja abierta esa terminal.
4. En Postman importa la colección y ejecuta las 12 peticiones en orden. Describe el método, los datos y el código HTTP de cada respuesta.
5. Enseña Producto, ProductoElectronico, Inventario y ProductoService en el editor.
6. Antes de entregar, revisa el documento de GitHub: el enlace debe ser real y accesible al profesor.

## Preguntas para practicar

- ¿Por qué BigDecimal? Representa los precios decimales sin la aproximación binaria de double.
- ¿Dónde se aplica el polimorfismo? Al recorrer Producto y ejecutar la versión sobrescrita de la subclase.
- ¿Por qué ArrayList y H2? La lista permite practicar los recorridos; H2 es el almacén usado por la API.
- ¿Por qué no microservicios? Un solo dominio pequeño y un alumno no necesitan varios despliegues.
- ¿Qué ocurre al reiniciar? H2 en memoria se vacía.
- ¿Cómo evitas cambios parciales? Validación previa del conjunto y transacción del servicio.
