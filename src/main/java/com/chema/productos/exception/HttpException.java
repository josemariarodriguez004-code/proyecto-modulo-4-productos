package com.chema.productos.exception;

import org.springframework.http.HttpStatus;

/** Error de negocio que el manejador central convierte en una respuesta HTTP. */
public class HttpException extends RuntimeException {
    private final HttpStatus estado;
    public HttpException(HttpStatus estado, String mensaje) {
        super(mensaje); this.estado = estado;
    }
    public HttpStatus getEstado() { return estado; }
}
