package com.ecommerce.controllers;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import com.ecommerce.ejb.ClienteEJB;
import com.ecommerce.model.Cliente;

@Path("/clientes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClienteController {

    @EJB
    private ClienteEJB clienteEJB;

    @GET
    public Response listar() {
        try {
            List<Cliente> clientes = clienteEJB.listar();
            return Response.ok(clientes).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar clientes: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") Long id) {
        try {
            Cliente cliente = clienteEJB.obtenerPorId(id);
            if (cliente != null) {
                return Response.ok(cliente).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Cliente no encontrado con ID: " + id)
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener cliente: " + e.getMessage())
                    .build();
        }
    }

    @POST
    public Response crear(Cliente cliente) {
        try {
            clienteEJB.guardar(cliente);
            return Response.status(Response.Status.CREATED)
                    .entity(cliente)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al crear cliente: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") Long id, Cliente cliente) {
        try {
            Cliente existente = clienteEJB.obtenerPorId(id);
            if (existente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Cliente no encontrado con ID: " + id)
                        .build();
            }

            cliente.setIdCliente(id);
            clienteEJB.guardar(cliente);
            return Response.ok(cliente).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al actualizar cliente: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") Long id) {
        try {
            Cliente existente = clienteEJB.obtenerPorId(id);
            if (existente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Cliente no encontrado con ID: " + id)
                        .build();
            }

            clienteEJB.eliminar(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al eliminar cliente: " + e.getMessage())
                    .build();
        }
    }
}