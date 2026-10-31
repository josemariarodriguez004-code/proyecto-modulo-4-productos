# Verificación del proyecto

Fecha: 31/10/2026. Comprobación en el entorno de preparación, no en el ordenador del alumno.

## Compilación y tests

JDK 17.0.20, Maven 3.9.9 y Spring Boot 3.5.16. Se extrajo el ZIP en una carpeta limpia y se ejecutó `./mvnw -o -B clean verify` correctamente. Tras la revisión y corrección descritas abajo, se repitió la compilación limpia con el mismo comando: tests y empaquetado correctos. La opción `-o` utiliza las dependencias ya descargadas; para la primera ejecución se debe omitir, como indica README.

| Suite | Tests | Fallos | Errores | Omitidos |
|---|---:|---:|---:|---:|
| ModeloTest | 7 | 0 | 0 | 0 |
| ProductoApiTest | 9 | 0 | 0 | 0 |

## Ejecución de consola

HolaMundo.java se compiló y ejecutó por separado. El JAR empaquetado se ejecutó con `--demo`. Salida obtenida:

```text
¡Hola, Mundo!
Inventario inicial:
Producto: Cuaderno, precio: 3.50, cantidad: 25
Producto: Portátil, precio: 799.99, cantidad: 5, marca: Lenovo, modelo: IdeaPad
Producto: Ratón, precio: 19.90, cantidad: 12, marca: Logitech, modelo: M185
Búsqueda por nombre: port
Producto: Portátil, precio: 799.99, cantidad: 5, marca: Lenovo, modelo: IdeaPad
Mayor cantidad: Cuaderno
Abstracción: Producto: Portátil, precio: 799.99, cantidad: 5, marca: Lenovo, modelo: IdeaPad
Tamaño del array obtenido: 3
Error controlado: El precio debe estar entre 0 y 9999999999.99, con hasta 2 decimales.
Productos tras rechazar el inválido: 3
```

## Peticiones a un servidor real

Se arrancó el JAR con el puerto de comprobación 18081 y H2 vacío. El puerto normal del proyecto sigue siendo 8081. Se enviaron las siguientes 18 peticiones y se comprobaron sus códigos y los datos relevantes.

| Método | Ruta | HTTP |
|---|---|---:|
| GET | `/productos` | 200 |
| GET | `/productos/mayor-stock` | 204 |
| POST | `/productos` | 201 |
| POST | `/productos` | 201 |
| GET | `/productos/2` | 200 |
| GET | `/productos?nombre=PORT` | 200 |
| GET | `/productos/mayor-stock` | 200 |
| PUT | `/productos/2` | 200 |
| GET | `/productos/mayor-stock` | 200 |
| PUT | `/productos/2` | 400 |
| PUT | `/productos/2` | 409 |
| GET | `/productos/2` | 200 |
| POST | `/productos` | 400 |
| GET | `/productos/999999` | 404 |
| DELETE | `/productos/1` | 204 |
| DELETE | `/productos/2` | 204 |
| GET | `/productos` | 200 |
| GET | `/productos/mayor-stock` | 204 |

Tras los PUT rechazados se comprobó que el precio seguía en 749.99 y la cantidad en 30. Al finalizar, el listado estaba vacío. Las peticiones de esta evidencia se ejecutaron mediante un cliente HTTP automatizado; no se afirma haber ejecutado la interfaz gráfica de Postman.

## Segunda revisión y corrección

Se detectó y reprodujo que Jackson convertía algunos valores de tipo incorrecto: aceptaba números como nombre, cantidades entre comillas y valores `0` o `"0"` como tipo GENERICO. Se deshabilitaron las conversiones automáticas de escalares y de índices a enumeraciones; JsonConfig rechaza números y booleanos en los campos de texto.

Las 16 pruebas existentes se ampliaron con casos de entrada incorrecta y actualización rechazada sin cambios. Resultado: 16 tests, 0 fallos, 0 errores y 0 omitidos. Se adjuntan los informes renovados de Surefire en `docs/evidencias`.

Con el JAR corregido se repitieron las 18 peticiones anteriores y se añadieron 10 POST inválidos: tipo 0, tipo "0", tipo 1, tipo "1", nombre numérico, nombre booleano, precio entre comillas, cantidad entre comillas, marca numérica y modelo booleano. Todos devolvieron 400. Un GET final confirmó que ninguna entrada inválida se había guardado. Total: **29 peticiones HTTP verificadas**.

También se corrigió la descripción de las respuestas del controlador en ARQUITECTURA.md y se documentaron los tipos JSON admitidos en API.md. El ZIP se comprobó para descartar archivos dañados y confirmar que contiene los fuentes `.java`, la configuración Maven y los documentos, sin incluir la carpeta de compilación `target`.

## Documentos

PPTX validado estructuralmente y renderizado para revisión. El PDF de presentación reproduce esos renders. Formulario de entrega comprobado: dos campos rellenables con valores y apariencias coherentes.

## Pendiente antes de entregar

Ejecutar el proyecto en el equipo del alumno, publicar el código en su repositorio GitHub y completar el documento con el enlace real. La publicación no se ha realizado.
