# Correspondencia con el enunciado

| Parte / objetivo | Implementación | Comprobación |
|---|---|---|
| 1. Entorno y Hola Mundo | README, recomendaciones VS Code, Maven Wrapper, verificacion/HolaMundo.java | Compilación y ejecución; instalar JDK en el equipo del alumno |
| 2. Producto, constructor, getters/setters | modelo/Producto.java | ModeloTest |
| 2. Encapsulación y herencia | Campos privados; ProductoElectronico extends Producto | ModeloTest y persistencia de subclase en ProductoApiTest |
| 2. Polimorfismo | mostrarInfo() sobrescrito y descripcion() | DemoInventario y ModeloTest |
| Objetivo: abstracción | Interfaz Describible | Referencia de interfaz en DemoInventario |
| 3. Arrays y ArrayList | Array inicial, Inventario y comoArray() | DemoInventario y ModeloTest |
| 3. Agregar, mostrar, buscar con bucle | Inventario.agregar, mostrarTodos, buscarPorNombre | Demo y búsqueda parcial probada |
| 4. Condicionales y validación | Validaciones, Bean Validation y HttpException | Constructores, setters y respuestas 400 |
| 4. Producto con mayor cantidad | Inventario.mayorCantidad y GET /productos/mayor-stock | Vacío, empate y máximo |
| 5. Spring Boot y Maven | pom.xml, ProductosApplication | clean verify y arranque del JAR |
| 5. Controlador, servicio, repositorio | ProductoController, ProductoService, ProductoRepository | Tests de API con contexto Spring |
| 6. JPA y H2 | application.properties, @Entity, SINGLE_TABLE | CRUD y lectura posterior desde H2 |
| 7. Manejo de errores | HttpException y ManejadorExcepciones | 400, 404, 409; actualización rechazada no altera datos |
| Entrega: código comentado | src/main/java y src/test/java, README | Archivos Java reales dentro del ZIP |
| Entrega: arquitecturas | Comparativa_Arquitecturas.pdf y ARQUITECTURA.md | Diagramas y decisión explicada |
| Entrega: presentación | Presentacion_Proyecto_Modulo4.pdf/.pptx | Flujo, decisiones, patrones y demostración |
| Entrega: enlace GitHub | Entrega_Proyecto_Modulo4.pdf | PENDIENTE de publicar y completar el enlace real |

Decisiones respecto al ejemplo: BigDecimal sustituye a double para precios; la clase ProductoElectronico se escribe sin tilde; cero unidades es válido (sin stock); el tipo es inmutable después del alta. Estas decisiones se explican en README y presentación.
