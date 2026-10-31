package com.chema.productos.demo;

import com.chema.productos.modelo.*;
import java.math.BigDecimal;

/** Demostración de arrays, ArrayList, herencia, polimorfismo y condicionales. */
public class DemoInventario {
    public static void main(String[] args) {
        System.out.println("¡Hola, Mundo!");
        Producto[] iniciales = {
            new Producto("Cuaderno", new BigDecimal("3.50"), 25),
            new ProductoElectronico("Portátil", new BigDecimal("799.99"), 5, "Lenovo", "IdeaPad"),
            new ProductoElectronico("Ratón", new BigDecimal("19.90"), 12, "Logitech", "M185")
        };
        Inventario inventario = new Inventario();
        for (Producto producto : iniciales) inventario.agregar(producto);
        System.out.println("Inventario inicial:");
        inventario.mostrarTodos();
        System.out.println("Búsqueda por nombre: port");
        for (Producto producto : inventario.buscarPorNombre("port")) producto.mostrarInfo();
        inventario.mayorCantidad().ifPresent(p -> System.out.println("Mayor cantidad: " + p.getNombre()));
        Describible descripcion = iniciales[1];
        System.out.println("Abstracción: " + descripcion.descripcion());
        System.out.println("Tamaño del array obtenido: " + inventario.comoArray().length);
        try {
            inventario.agregar(new Producto("Inválido", new BigDecimal("-1"), 1));
        } catch (IllegalArgumentException e) {
            System.out.println("Error controlado: " + e.getMessage());
        }
        System.out.println("Productos tras rechazar el inválido: " + inventario.listar().size());
    }
}
