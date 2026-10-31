package com.chema.productos.modelo;

import java.math.BigDecimal;

/** Reglas del dominio compartidas por constructores y setters. */
final class Validaciones {
    private Validaciones() { }
    static String texto(String valor, String campo, int maximo) {
        if (valor == null || valor.isBlank() || valor.length() > maximo) {
            throw new IllegalArgumentException(campo + " es obligatorio y admite hasta " + maximo + " caracteres.");
        }
        return valor.trim();
    }
    static BigDecimal precio(BigDecimal valor) {
        if (valor == null || valor.signum() < 0 || valor.scale() > 2
                || valor.compareTo(new BigDecimal("9999999999.99")) > 0) {
            throw new IllegalArgumentException("El precio debe estar entre 0 y 9999999999.99, con hasta 2 decimales.");
        }
        return valor;
    }
    static int cantidad(int valor) {
        if (valor < 0) throw new IllegalArgumentException("La cantidad no puede ser negativa.");
        return valor;
    }
}
