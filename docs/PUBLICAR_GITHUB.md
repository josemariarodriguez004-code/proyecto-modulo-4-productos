# Publicar y entregar el proyecto

El código está preparado. **El repositorio todavía no está publicado.** En la cuenta conectada `josemariarodriguez004-code` no había repositorios accesibles al preparar esta entrega.

1. En GitHub, crea un repositorio nuevo. Nombre sugerido: `proyecto-modulo-4-productos`.
2. Elige su visibilidad según las normas del curso. Si es privado, concede acceso al profesor. Para el siguiente procedimiento, créalo vacío (sin README inicial).
3. Desde la carpeta del proyecto, abre PowerShell. Si tienes Git instalado:

```powershell
git init
git add .
git commit -m "Proyecto modulo 4: gestion de productos con Java y Spring"
git branch -M main
```

4. Copia de GitHub la **URL HTTPS real** del repositorio. Usa esa URL como argumento de `git remote add origin` y después ejecuta `git push -u origin main`. Completa la autenticación que solicite GitHub.
5. Comprueba en el navegador que aparecen `pom.xml`, `src/main/java`, `src/test/java`, `docs`, `postman`, `mvnw`, `mvnw.cmd` y `.mvn/wrapper/maven-wrapper.properties`. `.gitignore` evita subir `target` y archivos `.class`.

Alternativa: abre la carpeta como repositorio local en GitHub Desktop y publica desde allí. Sube el contenido del proyecto, no solamente el ZIP.

## Documento que se adjunta en la plataforma

Abre `Entrega_Proyecto_Modulo4.pdf`, escribe o pega el enlace real en el campo **Repositorio GitHub** y guarda una copia. Verifica que el enlace permanece visible al reabrirla. La línea de estado inicial indica que está pendiente: cambia ese campo a “Publicado y acceso comprobado” cuando hayas comprobado el acceso del profesor.

Adjunta el documento completado, `Comparativa_Arquitecturas.pdf` y `Presentacion_Proyecto_Modulo4.pdf` (o su PPTX) donde indique la plataforma. El enunciado exige código en GitHub: el ZIP sirve para descargar y preparar el proyecto, pero no sustituye ese enlace.

Si deseas que se publique desde esta conversación, proporciona el enlace de un repositorio accesible mediante la conexión GitHub. No se ha inventado ninguna URL ni se ha subido el proyecto a una cuenta distinta.
