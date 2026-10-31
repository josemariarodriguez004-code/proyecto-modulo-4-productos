package com.chema.productos.modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;

/** Entidad base. Los atributos privados se modifican mediante métodos validados. */
@Entity
@Table(name = "productos")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "clase_producto")
@DiscriminatorValue("GENERICO")
public class Producto implements Describible {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String nombre;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;
    @Column(nullable = false)
    private int cantidad;

    // JPA necesita un constructor sin argumentos para reconstruir las entidades.
    protected Producto() { }
    public Producto(String nombre, BigDecimal precio, int cantidad) {
        setNombre(nombre); setPrecio(precio); setCantidad(cantidad);
    }
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public BigDecimal getPrecio() { return precio; }
    public int getCantidad() { return cantidad; }
    public TipoProducto getTipo() { return TipoProducto.GENERICO; }
    public void setNombre(String nombre) { this.nombre = Validaciones.texto(nombre, "El nombre", 100); }
    public void setPrecio(BigDecimal precio) { this.precio = Validaciones.precio(precio); }
    public void setCantidad(int cantidad) { this.cantidad = Validaciones.cantidad(cantidad); }

    @Override
    public String descripcion() {
        return "Producto: " + nombre + ", precio: " + precio + ", cantidad: " + cantidad;
    }
    public void mostrarInfo() { System.out.println(descripcion()); }
}
