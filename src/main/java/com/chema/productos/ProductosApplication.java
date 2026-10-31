package com.chema.productos;

import com.chema.productos.demo.DemoInventario;
import java.util.Arrays;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProductosApplication {
    public static void main(String[] args) {
        // El mismo JAR permite mostrar la POO sin arrancar el servidor.
        if (Arrays.asList(args).contains("--demo")) {
            DemoInventario.main(new String[0]);
            return;
        }
        SpringApplication.run(ProductosApplication.class, args);
    }
}
