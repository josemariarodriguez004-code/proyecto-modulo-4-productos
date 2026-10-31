package com.chema.productos.modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;

/** Hereda los datos comunes y añade marca y modelo. Nombre sin tilde por portabilidad. */
@Entity
@DiscriminatorValue("ELECTRONICO")
public class ProductoElectronico extends Producto {
    // Son anulables en la tabla porque los productos genéricos no tienen estos datos.
    @Column(length = 80)
    private String marca;
    @Column(length = 80)
    private String modelo;
    protected ProductoElectronico() { }
    public ProductoElectronico(String nombre, BigDecimal precio, int cantidad, String marca, String modelo) {
        super(nombre, precio, cantidad);
        setMarca(marca); setModelo(modelo);
    }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public void setMarca(String marca) { this.marca = Validaciones.texto(marca, "La marca", 80); }
    public void setModelo(String modelo) { this.modelo = Validaciones.texto(modelo, "El modelo", 80); }
    @Override public TipoProducto getTipo() { return TipoProducto.ELECTRONICO; }
    @Override public String descripcion() {
        return super.descripcion() + ", marca: " + marca + ", modelo: " + modelo;
    }
    // El bucle de Inventario invoca esta versión cuando el objeto es electrónico.
    @Override public void mostrarInfo() { System.out.println(descripcion()); }
}
