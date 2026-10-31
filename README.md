# Sistema de Gestión de Productos

**José María Rodríguez Cruz · Proyecto Módulo 4 · Escuela Musk**

Aplicación Java con POO, inventario y API REST. Permite crear, consultar, buscar, modificar y eliminar productos genéricos o electrónicos. H2 guarda los datos durante la ejecución. El cliente de demostración es Postman; no se necesita una página web.

## 1. Abrir y ejecutar en Windows

1. Descomprime el ZIP. Abre en VS Code **la carpeta que contiene este README y `pom.xml`**.
2. Instala un **JDK 17 o superior compatible**. El proyecto compila para Java 17. Comprueba `java -version` y `javac -version`. `JAVA_HOME` debe señalar la carpeta del JDK.
3. Instala las extensiones **Extension Pack for Java** y **Spring Boot Extension Pack**. VS Code las recomienda al abrir la carpeta.
4. Abre una terminal PowerShell en esa carpeta y ejecuta:

```powershell
.\mvnw.cmd clean verify
java -jar target/gestion-productos-1.0.0.jar
```

La primera compilación necesita Internet para descargar Maven y las dependencias. No hace falta instalar Maven por separado: se incluye Maven Wrapper. Cuando aparezca `Started ProductosApplication`, consulta `http://localhost:8081/productos`: inicialmente devuelve `[]`.

En Linux/macOS:

```bash
chmod +x mvnw
./mvnw clean verify
java -jar target/gestion-productos-1.0.0.jar
```

Detén el servidor con **Ctrl+C**. Si el puerto está ocupado, arranca con `java -jar target/gestion-productos-1.0.0.jar --server.port=8082` y cambia `baseUrl` en Postman.

## 2. Hola Mundo y demostración de POO

Para verificar el JDK sin Spring, desde la raíz:

```powershell
javac -encoding UTF-8 verificacion/HolaMundo.java
java -cp verificacion HolaMundo
```

Después de compilar el proyecto, ejecuta la demostración independiente:

```powershell
java -jar target/gestion-productos-1.0.0.jar --demo
```

Muestra un array inicial, un `ArrayList`, herencia, polimorfismo, búsqueda por nombre, mayor cantidad y rechazo de un precio negativo. Este modo termina después de imprimir los resultados; no arranca la API ni carga estos ejemplos en H2.

## 3. Probar la API

Importa `postman/Productos.postman_collection.json` en Postman. Con el servidor arrancado, ejecuta las peticiones **en orden**. La colección guarda automáticamente los identificadores creados. También puedes usar los ejemplos de `docs/API.md`.

| Método | Ruta | Resultado |
|---|---|---|
| GET | `/productos` | Lista completa, 200 |
| GET | `/productos?nombre=port` | Coincidencias parciales sin distinguir mayúsculas, 200 |
| GET | `/productos/mayor-stock` | Producto con más unidades, 200; 204 si vacío |
| GET | `/productos/{id}` | Un producto, 200; 404 si no existe |
| POST | `/productos` | Crea un producto, 201 y cabecera Location |
| PUT | `/productos/{id}` | Sustituye sus datos editables, 200 |
| DELETE | `/productos/{id}` | Elimina el producto, 204 |

Los nombres pueden repetirse: cada producto tiene un `id` distinto. En empate de stock se devuelve el primer producto del inventario (en la API, el de menor id). PUT exige nombre, precio y cantidad; si es electrónico, también marca y modelo. El tipo no se puede cambiar después del alta.

## 4. H2 y configuración

Archivo: `src/main/resources/application.properties`.

| Dato | Valor |
|---|---|
| Puerto | `8081` |
| Consola H2 | `http://localhost:8081/h2-console` |
| JDBC URL | `jdbc:h2:mem:inventario` |
| Usuario | `sa` |
| Contraseña | Vacía |
| Esquema | `create-drop` |

La base de datos es **en memoria**: conserva los productos entre peticiones mientras el proceso está activo, pero se reinicia al detenerlo. Es el comportamiento solicitado por el enunciado. No requiere MySQL. En la consola se puede ejecutar `SELECT * FROM PRODUCTOS;`.

## 5. Organización del código

| Carpeta dentro de `src/main/java/com/chema/productos` | Responsabilidad |
|---|---|
| `modelo` | Producto, ProductoElectronico, Describible, Inventario y validaciones |
| `controller` | Rutas HTTP y códigos de respuesta |
| `service` | Casos de uso y transacciones |
| `repository` | Acceso a H2 mediante Spring Data JPA |
| `dto` | Contrato de entrada/salida JSON |
| `config` | Validación de los tipos de datos recibidos en JSON |
| `exception` | HttpException y traducción de errores a respuestas HTTP |
| `demo` | Ejemplo de consola para explicar POO |

Se usa `BigDecimal` para precios con hasta dos decimales. El nombre de la clase `ProductoElectronico` se escribe sin tilde para facilitar su uso en herramientas; corresponde a la clase del enunciado. JPA conserva ambos tipos en una tabla mediante `SINGLE_TABLE` y la columna discriminadora `clase_producto`.

En las consultas, el servicio obtiene los productos de H2 y crea un `Inventario` temporal. Así se practican los bucles y el `ArrayList` sin mantener dos almacenes globales que puedan desincronizarse. Para grandes volúmenes se sustituiría esta carga completa por consultas filtradas y paginadas en la base de datos.

## 6. Arquitectura y patrones

**Monolito por capas con API REST sobre Spring MVC.** Un solo JAR contiene controlador, servicio, modelo y repositorio. `@RestController` devuelve JSON mediante conversores HTTP; no se ha creado una vista HTML.

| Patrón / principio aplicado | Ejemplo y motivo |
|---|---|
| Repository | ProductoRepository separa el acceso a datos del caso de uso |
| Service Layer | ProductoService coordina reglas, persistencia y transacciones |
| DTO | ProductoRequest/Response delimitan el contrato HTTP y no permiten asignar el id |
| Inyección de dependencias | Spring suministra servicio y repositorio por constructor |
| Encapsulación | Campos privados y setters que rechazan estados inválidos |
| Herencia y polimorfismo | ProductoElectronico extiende Producto y redefine mostrarInfo/descripcion |
| Abstracción | Describible define un contrato sin exponer la implementación |

La comparación de alternativas está en `docs/Comparativa_Arquitecturas.pdf`. El proyecto **no implementa microservicios ni arquitectura hexagonal**. Su dominio contiene anotaciones JPA; para aislarlo con puertos y adaptadores habría que separar ese modelo de persistencia.

## 7. Verificación y límites

`clean verify` ejecuta los tests de `src/test/java`. Hay pruebas del modelo y de la API con H2 real y MockMvc. Comprueban CRUD, persistencia de la subclase, búsqueda, stock, datos inválidos, códigos HTTP y que un PUT rechazado conserve los datos.

Consulta `docs/VERIFICACION.md` para los resultados obtenidos en el entorno de preparación. Esa verificación no sustituye la instalación y ejecución en tu ordenador.

Alcance académico local: no incluye autenticación, interfaz gráfica, paginación ni persistencia en disco. Son ampliaciones futuras, no requisitos de esta entrega.

## 8. Entrega

El enunciado solicita **un repositorio GitHub y un documento adjunto con su enlace**, además del diagrama y la presentación. El ZIP contiene los archivos para publicarlos; no equivale por sí solo a tener el repositorio publicado.

1. Sigue `docs/PUBLICAR_GITHUB.md` para subir el código.
2. Completa el enlace en `docs/Entrega_Proyecto_Modulo4.pdf` y guarda el PDF.
3. Adjunta ese documento y los PDF de comparación y presentación en la plataforma según sus campos. La presentación también está en PPTX editable.

`docs/GUION_PRESENTACION.md` contiene una explicación para practicar y una demostración paso a paso.

## Fuentes

Enunciado y apuntes del Módulo 4 aportados por el alumno. Referencias oficiales y arquitectura consultadas el 31/10/2026:

- [Requisitos de Spring Boot 3.5](https://docs.spring.io/spring-boot/3.5/system-requirements.html).
- [Spring MVC: @ResponseBody y @RestController](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods/responsebody.html).
- [Lewis y Fowler: Microservices](https://martinfowler.com/articles/microservices.html).
- [Cockburn: Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture).

Para reproducir la selección de dependencias en Spring Initializr: Maven, Java 17, Jar, grupo `com.chema`, artefacto `gestion-productos`, Spring Web, Spring Data JPA, Validation y H2 Database. La versión exacta de esta entrega está fijada en `pom.xml`.
