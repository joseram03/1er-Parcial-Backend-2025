package com.ecommerce.controllers;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import com.ecommerce.ejb.CategoriaEJB;
import com.ecommerce.model.Categoria;

@Path("/categorias")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CategoriaController {

    @EJB
    private CategoriaEJB categoriaEJB;

    @GET
    public Response listar() {
        try {
            List<Categoria> categorias = categoriaEJB.listar();
            return Response.ok(categorias).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar categorías: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") Long id) {
        try {
            Categoria categoria = categoriaEJB.obtenerPorId(id);
            if (categoria != null) {
                return Response.ok(categoria).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Categoría no encontrada con ID: " + id)
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener categoría: " + e.getMessage())
                    .build();
        }
    }

    @POST
    public Response crear(Categoria categoria) {
        try {
            categoriaEJB.guardar(categoria);
            return Response.status(Response.Status.CREATED)
                    .entity(categoria)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al crear categoría: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") Long id, Categoria categoria) {
        try {
            Categoria existente = categoriaEJB.obtenerPorId(id);
            if (existente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Categoría no encontrada con ID: " + id)
                        .build();
            }

            categoria.setIdCategoria(id);
            categoriaEJB.guardar(categoria);
            return Response.ok(categoria).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al actualizar categoría: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") Long id) {
        try {
            Categoria existente = categoriaEJB.obtenerPorId(id);
            if (existente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Categoría no encontrada con ID: " + id)
                        .build();
            }

            categoriaEJB.eliminar(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al eliminar categoría: " + e.getMessage())
                    .build();
        }
    }
}