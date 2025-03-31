package com.ecommerce.controllers;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.ecommerce.ejb.VentaEJB;
import com.ecommerce.model.Venta;
import com.ecommerce.model.DetalleVenta;

@Path("/ventas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VentaController {

    @EJB
    private VentaEJB ventaEJB;

    @GET
    public Response listar(@QueryParam("fecha") String fechaStr,
                           @QueryParam("idCliente") Long idCliente) {
        try {
            // Convertir la fecha string a Date si es necesario
            Date fecha = null;
            if (fechaStr != null && !fechaStr.isEmpty()) {
                try {
                    // Aquí usamos un formato específico para la fecha
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    fecha = formatter.parse(fechaStr);
                } catch (Exception e) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("El formato de la fecha es incorrecto. Use el formato 'yyyy-MM-dd'.")
                            .build();
                }
            }


            List<Venta> ventas = ventaEJB.listar(fecha, idCliente);
            return Response.ok(ventas).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar ventas: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") Long id) {
        try {
            Venta venta = ventaEJB.obtenerPorId(id);
            if (venta != null) {
                return Response.ok(venta).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Venta no encontrada con ID: " + id)
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener venta: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}/detalles")
    public Response obtenerDetalles(@PathParam("id") Long id) {
        try {
            List<DetalleVenta> detalles = ventaEJB.obtenerDetalles(id);
            if (detalles != null && !detalles.isEmpty()) {
                return Response.ok(detalles).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontraron detalles para la venta con ID: " + id)
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener detalles de venta: " + e.getMessage())
                    .build();
        }
    }

    @POST
    public Response crear(Venta venta) {
        try {
            ventaEJB.guardar(venta);
            return Response.status(Response.Status.CREATED)
                    .entity(venta)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al crear venta: " + e.getMessage())
                    .build();
        }
    }
}