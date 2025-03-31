package com.ecommerce;

import com.ecommerce.controllers.CategoriaController;
import com.ecommerce.controllers.ClienteController;
import com.ecommerce.controllers.ProductoController;
import com.ecommerce.controllers.VentaController;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class JaxRsActivator extends Application {
    // Clase vacía que activa JAX-RS
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();

        // Agregar todos los recursos REST
        resources.add(CategoriaController.class);
        resources.add(ProductoController.class);
        resources.add(ClienteController.class);
        resources.add(VentaController.class);

        return resources;
    }
}