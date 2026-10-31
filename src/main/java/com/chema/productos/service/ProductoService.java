package com.chema.productos.service;

import com.chema.productos.dto.*;
import com.chema.productos.exception.HttpException;
import com.chema.productos.modelo.*;
import com.chema.productos.repository.ProductoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductoService {
    private final ProductoRepository repository;
    // Inyección por constructor: Spring proporciona el repositorio.
    public ProductoService(ProductoRepository repository) { this.repository = repository; }

    private Inventario inventarioActual() {
        // H2 es la fuente de datos. Esta lista es temporal, sin una segunda copia global.
        return new Inventario(repository.findAll(Sort.by("id")));
    }
    @Transactional(readOnly = true)
    public List<ProductoResponse> listar(String nombre) {
        Inventario inventario = inventarioActual();
        List<Producto> productos = nombre == null ? inventario.listar() : inventario.buscarPorNombre(nombre);
        return productos.stream().map(ProductoResponse::desde).toList();
    }
    @Transactional(readOnly = true)
    public Optional<ProductoResponse> mayorCantidad() {
        return inventarioActual().mayorCantidad().map(ProductoResponse::desde);
    }
    private Producto buscar(Long id) {
        if (id == null || id <= 0) throw new HttpException(HttpStatus.BAD_REQUEST, "El id debe ser positivo.");
        return repository.findById(id).orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Producto no encontrado con ID: " + id));
    }
    @Transactional(readOnly = true)
    public ProductoResponse obtener(Long id) { return ProductoResponse.desde(buscar(id)); }

    private Producto construir(ProductoRequest datos, TipoProducto tipo) {
        if (tipo == TipoProducto.ELECTRONICO) {
            if (datos.marca() == null || datos.marca().isBlank() || datos.modelo() == null || datos.modelo().isBlank()) {
                throw new HttpException(HttpStatus.BAD_REQUEST, "Un producto electrónico necesita marca y modelo.");
            }
            return new ProductoElectronico(datos.nombre(), datos.precio(), datos.cantidad(), datos.marca(), datos.modelo());
        }
        if (datos.marca() != null || datos.modelo() != null) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Marca y modelo corresponden al tipo ELECTRONICO.");
        }
        return new Producto(datos.nombre(), datos.precio(), datos.cantidad());
    }
    public ProductoResponse crear(ProductoRequest datos) {
        TipoProducto tipo = datos.tipo() == null ? TipoProducto.GENERICO : datos.tipo();
        return ProductoResponse.desde(repository.save(construir(datos, tipo)));
    }
    public ProductoResponse actualizar(Long id, ProductoRequest datos) {
        Producto actual = buscar(id);
        TipoProducto tipo = datos.tipo() == null ? actual.getTipo() : datos.tipo();
        if (tipo != actual.getTipo()) throw new HttpException(HttpStatus.CONFLICT, "No se puede cambiar el tipo de un producto existente.");
        // Valida el conjunto antes de modificar la entidad gestionada por JPA.
        Producto valido = construir(datos, tipo);
        actual.setNombre(valido.getNombre()); actual.setPrecio(valido.getPrecio()); actual.setCantidad(valido.getCantidad());
        if (actual instanceof ProductoElectronico electronico && valido instanceof ProductoElectronico datosElectronicos) {
            electronico.setMarca(datosElectronicos.getMarca()); electronico.setModelo(datosElectronicos.getModelo());
        }
        return ProductoResponse.desde(repository.save(actual));
    }
    public void eliminar(Long id) { repository.delete(buscar(id)); }
}
