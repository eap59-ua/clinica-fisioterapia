# Cómo reiniciar correctamente el backend

## ⚠️ IMPORTANTE: Tus cambios están aplicados en el código, pero necesitas recompilar

Los archivos ya están modificados correctamente:
- `FisioterapeutaService.java` - retorna `null` en vez de lanzar excepción
- `FisioterapeutaController.java` - retorna `404` cuando no hay nota
- `CrearNotaSesionRequest.java` - sin validación `@NotNull` en `citaId`

**El problema:** El backend está ejecutando la versión antigua (compilada antes de los cambios).

## Pasos para reiniciar correctamente:

### Opción 1: Desde IntelliJ IDEA (Recomendado)

1. **Detener la aplicación**
   - Click en el botón rojo STOP (cuadrado rojo) en la barra de herramientas
   - Espera a que diga "Process finished"

2. **Limpiar y recompilar**
   - Ve a: `Build` → `Rebuild Project`
   - O presiona: `Ctrl + Shift + F9`
   - Espera a que termine (verás "Build completed successfully")

3. **Reiniciar la aplicación**
   - Click en el botón verde PLAY (triángulo verde)
   - O presiona: `Shift + F10`

### Opción 2: Desde terminal

```bash
cd backend

# Limpiar y compilar
mvn clean install -DskipTests

# Ejecutar
mvn spring-boot:run
```

## ✅ Verificación

Después de reiniciar, verifica en los logs que NO aparezcan estos errores:
- ❌ `RuntimeException: No hay nota registrada para esta cita`
- ❌ `Field error in object 'crearNotaSesionRequest' on field 'citaId'`

Si siguen apareciendo, el backend no se recompiló correctamente.

## Qué debería funcionar después del reinicio:

1. **GET** `/api/fisioterapeuta/citas/10/nota`
   - Si NO hay nota → Respuesta `404` (normal, no es error)
   - Si hay nota → Respuesta `200` con datos

2. **POST** `/api/fisioterapeuta/citas/10/nota`
   - Con body: `{"contenido": "...", "diagnostico": "...", etc}`
   - Sin necesidad de enviar `citaId` en el body
   - Debería crear la nota sin errores de validación

---

**Nota:** Si IntelliJ no detecta los cambios, usa `File` → `Invalidate Caches` y reinicia IntelliJ.
