# Contrato de la API

Base: `http://localhost:8081`. Para POST y PUT: `Content-Type: application/json`.

## Crear un producto genérico

`POST /productos`

```json
{"nombre":"Cuaderno","precio":3.50,"cantidad":25,"tipo":"GENERICO"}
```

Devuelve 201 con un id generado. Si se omite `tipo`, el alta es GENERICO. Marca y modelo se omiten o se envían como null.

## Crear un producto electrónico

`POST /productos`

```json
{"nombre":"Portátil","precio":799.99,"cantidad":5,"tipo":"ELECTRONICO","marca":"Lenovo","modelo":"IdeaPad"}
```

La respuesta incluye `id`, `nombre`, `precio`, `cantidad`, `tipo`, `marca`, `modelo` y `descripcion`. Utiliza el id devuelto en las siguientes operaciones; no supongas que siempre será 1.

## Leer, buscar y encontrar mayor stock

`GET /productos` lista todos. `GET /productos?nombre=PORT` busca coincidencias parciales sin distinguir mayúsculas. `GET /productos/{id}` obtiene uno. `GET /productos/mayor-stock` obtiene el de mayor cantidad. Una búsqueda sin coincidencias devuelve `[]` con 200; mayor stock sin productos devuelve 204 sin cuerpo.

## Actualizar

`PUT /productos/{id}`

```json
{"nombre":"Portátil actualizado","precio":749.99,"cantidad":8,"tipo":"ELECTRONICO","marca":"Lenovo","modelo":"IdeaPad"}
```

PUT requiere todos los datos editables, no es una modificación parcial. Omitir tipo conserva el existente. Cambiarlo devuelve 409. Primero se valida el conjunto y luego se modifica la entidad dentro de una transacción.

## Eliminar

`DELETE /productos/{id}` devuelve 204. Consultar o eliminar ese id otra vez devuelve 404.

## Validación

| Campo | Regla |
|---|---|
| id en la ruta | Long entero positivo |
| nombre | Obligatorio, no blanco, hasta 100 caracteres; se recortan espacios exteriores |
| precio | Obligatorio, 0 a 9999999999.99, hasta 2 decimales |
| cantidad | Obligatoria, entero entre 0 y 2147483647 |
| tipo | GENERICO o ELECTRONICO; opcional con las reglas indicadas |
| marca y modelo | Obligatorios para electrónico, no blancos, hasta 80 caracteres |

Los campos JSON desconocidos (por ejemplo `id` en el cuerpo) se rechazan. No se redondean cantidades fraccionarias ni precios con más de dos decimales.

Se exigen tipos JSON correctos: nombre, marca y modelo son textos; precio y cantidad son números sin comillas. El tipo solo admite los nombres `GENERICO` y `ELECTRONICO`, nunca índices numéricos como `0` o `1`. Un tipo de dato incorrecto devuelve 400 sin guardar cambios.

| Código | Cuándo |
|---|---|
| 200 | Consulta o actualización correcta |
| 201 | Producto creado; Location indica su ruta |
| 204 | Eliminación correcta o inventario vacío al buscar mayor stock |
| 400 | Datos, JSON o id inválidos |
| 404 | El producto solicitado no existe |
| 409 | Intento de cambiar el tipo de un producto existente |

Ejemplo de error controlado:

```json
{"estado":404,"mensaje":"Producto no encontrado con ID: 999999","errores":{}}
```

Los errores de Bean Validation añaden los campos afectados en `errores`. El texto exacto de sus mensajes puede depender del idioma configurado en Java.

## Prueba rápida en PowerShell

```powershell
$base = 'http://localhost:8081'
$datos = @{nombre='Cuaderno'; precio=3.50; cantidad=25} | ConvertTo-Json
$producto = Invoke-RestMethod "$base/productos" -Method Post -ContentType 'application/json; charset=utf-8' -Body $datos
Invoke-RestMethod "$base/productos/$($producto.id)"
$cambios = @{nombre='Cuaderno azul'; precio=4.20; cantidad=10} | ConvertTo-Json
Invoke-RestMethod "$base/productos/$($producto.id)" -Method Put -ContentType 'application/json; charset=utf-8' -Body $cambios
Invoke-RestMethod "$base/productos/$($producto.id)" -Method Delete
```
