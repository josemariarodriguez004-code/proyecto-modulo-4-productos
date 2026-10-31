package com.chema.productos;

import com.chema.productos.modelo.*;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ModeloTest {
    private Producto p(String nombre, int cantidad) { return new Producto(nombre, new BigDecimal("3.50"), cantidad); }
    @Test void validaConstructorYPrecioCero() {
        assertThat(new Producto("Muestra", BigDecimal.ZERO, 0).getCantidad()).isZero();
        assertThatThrownBy(() -> new Producto(" ", BigDecimal.ONE, 1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Producto("Libro", new BigDecimal("-1"), 1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Producto("Libro", BigDecimal.ONE, -1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Producto("Libro", new BigDecimal("1.999"), 1)).isInstanceOf(IllegalArgumentException.class);
    }
    @Test void settersInvalidosConservanValores() {
        Producto producto = p("Libro", 3);
        assertThatThrownBy(() -> producto.setCantidad(-1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> producto.setPrecio(new BigDecimal("-5"))).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> producto.setNombre(null)).isInstanceOf(IllegalArgumentException.class);
        assertThat(producto.getCantidad()).isEqualTo(3);
        assertThat(producto.getPrecio()).isEqualByComparingTo("3.50");
        assertThat(producto.getNombre()).isEqualTo("Libro");
    }
    @Test void herenciaPolimorfismoYAbstraccion() {
        Producto producto = new ProductoElectronico("Ratón", BigDecimal.TEN, 2, "Logitech", "M185");
        Describible describible = producto;
        assertThat(producto.getTipo()).isEqualTo(TipoProducto.ELECTRONICO);
        assertThat(describible.descripcion()).contains("Logitech", "M185", "Ratón");
        assertThatThrownBy(() -> new ProductoElectronico("Ratón", BigDecimal.ONE, 1, "", "M185"))
            .isInstanceOf(IllegalArgumentException.class);
    }
    @Test void busquedaParcialIgnoraMayusculasYEspaciosExtremos() {
        Inventario inventario = new Inventario(List.of(p("Cuaderno", 3), p("Portátil", 1), p("Ratón", 7)));
        assertThat(inventario.buscarPorNombre(" PORT ")).extracting(Producto::getNombre).containsExactly("Portátil");
        assertThat(inventario.buscarPorNombre("inexistente")).isEmpty();
        assertThatThrownBy(() -> inventario.buscarPorNombre(" ")).isInstanceOf(IllegalArgumentException.class);
    }
    @Test void mayorCantidadYEmpateConservanPrimerProducto() {
        Producto primero=p("Primero",5), segundo=p("Segundo",5);
        Inventario inventario = new Inventario(List.of(primero,segundo,p("Tercero",1)));
        assertThat(inventario.mayorCantidad()).contains(primero);
        segundo.setCantidad(8);
        assertThat(inventario.mayorCantidad()).contains(segundo);
    }
    @Test void listaYArraySonContenedoresIndependientes() {
        Inventario inventario = new Inventario(List.of(p("Libro",2)));
        inventario.listar().clear();
        Producto[] array = inventario.comoArray(); array[0] = p("Otro",7);
        assertThat(inventario.listar()).extracting(Producto::getNombre).containsExactly("Libro");
    }
    @Test void inventarioVacioYProductoNulo() {
        Inventario inventario = new Inventario();
        assertThat(inventario.mayorCantidad()).isEmpty();
        assertThat(inventario.comoArray()).isEmpty();
        assertThatThrownBy(() -> inventario.agregar(null)).isInstanceOf(IllegalArgumentException.class);
    }
}
