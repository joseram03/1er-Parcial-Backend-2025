package com.ecommerce.controllers;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import com.ecommerce.ejb.ProductoEJB;
import com.ecommerce.model.Producto;

@Path("/productos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductoController {

    @EJB
    private ProductoEJB productoEJB;

    @GET
    public Response listar(@QueryParam("nombre") String nombre,
                           @QueryParam("idCategoria") Long idCategoria) {
        try {
            List<Producto> productos = productoEJB.listar(nombre, idCategoria);
            return Response.ok(productos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar productos: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") Long id) {
        try {
            Producto producto = productoEJB.obtenerPorId(id);
            if (producto != null) {
                return Response.ok(producto).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Producto no encontrado con ID: " + id)
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener producto: " + e.getMessage())
                    .build();
        }
    }

    @POST
    public Response crear(Producto producto) {
        try {
            productoEJB.guardar(producto);
            return Response.status(Response.Status.CREATED)
                    .entity(producto)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al crear producto: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") Long id, Producto producto) {
        try {
            Producto existente = productoEJB.obtenerPorId(id);
            if (existente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Producto no encontrado con ID: " + id)
                        .build();
            }

            producto.setIdProducto(id);
            productoEJB.guardar(producto);
            return Response.ok(producto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al actualizar producto: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") Long id) {
        try {
            Producto existente = productoEJB.obtenerPorId(id);
            if (existente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Producto no encontrado con ID: " + id)
                        .build();
            }

            productoEJB.eliminar(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al eliminar producto: " + e.getMessage())
                    .build();
        }
    }
}