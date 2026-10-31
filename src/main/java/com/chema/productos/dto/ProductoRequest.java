package com.chema.productos.dto;

import com.chema.productos.modelo.TipoProducto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/** DTO de entrada: evita que el cliente asigne id u otros datos internos de JPA. */
public record ProductoRequest(
    @NotBlank(message = "El nombre es obligatorio.") @Size(max = 100) String nombre,
    @NotNull(message = "El precio es obligatorio.") @DecimalMin("0.00") @Digits(integer = 10, fraction = 2) BigDecimal precio,
    @NotNull(message = "La cantidad es obligatoria.") @Min(0) Integer cantidad,
    TipoProducto tipo,
    @Size(max = 80) String marca,
    @Size(max = 80) String modelo
) { }
