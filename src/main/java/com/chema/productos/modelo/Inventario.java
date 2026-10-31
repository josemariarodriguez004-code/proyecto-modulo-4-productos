package com.chema.productos.modelo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

/** Colección de trabajo. En la API se reconstruye desde H2 en cada consulta. */
public class Inventario {
    private final ArrayList<Producto> productos = new ArrayList<>();
    public Inventario() { }
    public Inventario(Collection<Producto> iniciales) {
        if (iniciales == null) throw new IllegalArgumentException("La colección es obligatoria.");
        for (Producto producto : iniciales) agregar(producto);
    }
    public void agregar(Producto producto) {
        if (producto == null) throw new IllegalArgumentException("El producto es obligatorio.");
        productos.add(producto);
    }
    public ArrayList<Producto> listar() {
        // Copia de la lista: el cliente no puede añadir o quitar elementos de esta colección.
        return new ArrayList<>(productos);
    }
    public Producto[] comoArray() { return productos.toArray(new Producto[0]); }
    public void mostrarTodos() {
        for (Producto producto : productos) producto.mostrarInfo();
    }
    public ArrayList<Producto> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("Indica un nombre para buscar.");
        String busqueda = nombre.trim().toLowerCase(Locale.ROOT);
        ArrayList<Producto> encontrados = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.getNombre().toLowerCase(Locale.ROOT).contains(busqueda)) encontrados.add(producto);
        }
        return encontrados;
    }
    public Optional<Producto> mayorCantidad() {
        Producto mayor = null;
        for (Producto producto : productos) {
            if (mayor == null || producto.getCantidad() > mayor.getCantidad()) mayor = producto;
        }
        // En empate conserva el primero. Vacío se representa con Optional.empty().
        return Optional.ofNullable(mayor);
    }
}
