package com.chema.productos.dto;

import com.chema.productos.modelo.*;
import java.math.BigDecimal;

/** Respuesta estable, separada de las entidades y de los detalles de persistencia. */
public record ProductoResponse(Long id, String nombre, BigDecimal precio, int cantidad,
                               TipoProducto tipo, String marca, String modelo, String descripcion) {
    public static ProductoResponse desde(Producto p) {
        String marca = null, modelo = null;
        if (p instanceof ProductoElectronico electronico) {
            marca = electronico.getMarca(); modelo = electronico.getModelo();
        }
        return new ProductoResponse(p.getId(), p.getNombre(), p.getPrecio(), p.getCantidad(),
                p.getTipo(), marca, modelo, p.descripcion());
    }
}
