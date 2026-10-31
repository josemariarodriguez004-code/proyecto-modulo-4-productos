package com.chema.productos.exception;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ManejadorExcepciones {
    public record ErrorRespuesta(int estado, String mensaje, Map<String, String> errores) { }
    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ErrorRespuesta> negocio(HttpException e) {
        return ResponseEntity.status(e.getEstado()).body(new ErrorRespuesta(e.getEstado().value(), e.getMessage(), Map.of()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRespuesta> validacion(MethodArgumentNotValidException e) {
        Map<String, String> campos = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> campos.putIfAbsent(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(new ErrorRespuesta(400, "Datos del producto no válidos.", campos));
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorRespuesta> dominio(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ErrorRespuesta(400, e.getMessage(), Map.of()));
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorRespuesta> json() {
        return ResponseEntity.badRequest().body(new ErrorRespuesta(400, "JSON inválido, campo desconocido o tipo incorrecto.", Map.of()));
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorRespuesta> tipoParametro() {
        return ResponseEntity.badRequest().body(new ErrorRespuesta(400, "El id debe ser un número entero de tipo Long.", Map.of()));
    }
}
