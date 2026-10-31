package com.chema.productos.repository;

import com.chema.productos.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring genera la implementación de las operaciones CRUD. */
public interface ProductoRepository extends JpaRepository<Producto, Long> { }
