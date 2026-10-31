package com.chema.productos.controller;

import com.chema.productos.dto.*;
import com.chema.productos.service.ProductoService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Entrada HTTP. La lógica de negocio y la persistencia se delegan al servicio. */
@RestController
@RequestMapping("/productos")
public class ProductoController {
    private final ProductoService service;
    public ProductoController(ProductoService service) { this.service = service; }
    @GetMapping
    public List<ProductoResponse> listar(@RequestParam(required = false) String nombre) { return service.listar(nombre); }
    @GetMapping("/mayor-stock")
    public ResponseEntity<ProductoResponse> mayorCantidad() {
        return service.mayorCantidad().map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }
    @GetMapping("/{id}")
    public ProductoResponse obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest datos) {
        ProductoResponse nuevo = service.crear(datos);
        return ResponseEntity.created(URI.create("/productos/" + nuevo.id())).body(nuevo);
    }
    @PutMapping("/{id}")
    public ProductoResponse actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequest datos) {
        return service.actualizar(id, datos);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id); return ResponseEntity.noContent().build();
    }
}
