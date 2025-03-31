package com.ecommerce.ejb;


import com.ecommerce.model.Cliente;
import com.ecommerce.model.DetalleVenta;
import com.ecommerce.model.Producto;
import com.ecommerce.model.Venta;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Stateless
public class VentaEJB {

    @PersistenceContext(unitName = "ecommercePU")
    private EntityManager em;

    @EJB
    private ProductoEJB productoEJB;

    @Resource(lookup = "java:jboss/mail/Default")
    private Session mailSession;

    public List<Venta> listar(Date fechaFiltro, Long idClienteFiltro) {
        String jpql = "SELECT v FROM Venta v LEFT JOIN FETCH v.detalles WHERE 1=1";

        if (fechaFiltro != null) {
            jpql += " AND CAST(v.fecha AS DATE) = CAST(:fecha AS DATE)";
        }

        if (idClienteFiltro != null) {
            jpql += " AND v.cliente.idCliente = :idCliente";
        }

        TypedQuery<Venta> query = em.createQuery(jpql, Venta.class);

        if (fechaFiltro != null) {
            query.setParameter("fecha", fechaFiltro);
        }

        if (idClienteFiltro != null) {
            query.setParameter("idCliente", idClienteFiltro);
        }

        return query.getResultList();
    }


    public List<Venta> listarTodas() {
        // Crear la consulta JPQL para obtener todas las ventas
        String jpql = "SELECT v FROM Venta v";

        // Crear la consulta TypedQuery
        TypedQuery<Venta> query = em.createQuery(jpql, Venta.class);

        // Ejecutar la consulta y devolver la lista de resultados
        return query.getResultList();
    }


    public Venta obtenerPorId(Long id) {
        return em.find(Venta.class, id);
    }

    public List<DetalleVenta> obtenerDetalles(Long idVenta) {
        return em.createQuery(
                        "SELECT d FROM DetalleVenta d WHERE d.venta.idVenta = :idVenta",
                        DetalleVenta.class)
                .setParameter("idVenta", idVenta)
                .getResultList();
    }

    public void guardar(Venta venta) {
        // Validar existencia de productos y actualizar inventario
        for (DetalleVenta detalle : venta.getDetalles()) {
            Producto producto = productoEJB.obtenerPorId(detalle.getProducto().getIdProducto());

            if (producto == null || producto.getCantidadExistente() < detalle.getCantidad()) {
                throw new RuntimeException("No hay suficiente existencia del producto: " +
                        (producto != null ? producto.getNombre() : "No existe"));
            }

            // Copiar el precio actual del producto al detalle
            detalle.setPrecio(producto.getPrecioVenta());
        }

        // Calcular total
        double total = 0;
        for (DetalleVenta detalle : venta.getDetalles()) {
            total += detalle.getPrecio() * detalle.getCantidad();
        }
        venta.setTotal(total);

        // Establecer fecha actual
        venta.setFecha(new Date());

        // Guardar venta
        if (venta.getIdVenta() == null) {
            List<DetalleVenta> detalles = venta.getDetalles();
            venta.setDetalles(null);

            em.persist(venta);
            em.flush(); // Asegurar que el ID se genere

            venta.setDetalles(detalles);

            // Relacionar detalles con la venta
            for (DetalleVenta detalle : venta.getDetalles()) {
                detalle.setVenta(venta);
                em.persist(detalle);

                // Actualizar existencia
                productoEJB.actualizarExistencia(
                        detalle.getProducto().getIdProducto(),
                        detalle.getCantidad());
            }

            // Enviar correo
            enviarCorreo(venta);
        }
    }

    private void enviarCorreo(Venta venta) {
        try {
            Cliente cliente = venta.getCliente();

            MimeMessage message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress("ecommerce@ejemplo.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(cliente.getEmail()));
            message.setSubject("Confirmación de compra - Pedido #" + venta.getIdVenta());

            // Construir el cuerpo del correo
            StringBuilder body = new StringBuilder();
            body.append("Estimado/a ").append(cliente.getNombre()).append(" ").append(cliente.getApellido()).append(",\n\n");
            body.append("¡Gracias por tu compra!\n\n");
            body.append("Detalles de la compra:\n");
            body.append("Fecha: ").append(venta.getFecha()).append("\n");
            body.append("Total: $").append(venta.getTotal()).append("\n\n");
            body.append("Productos:\n");

            for (DetalleVenta detalle : venta.getDetalles()) {
                body.append("- ").append(detalle.getProducto().getNombre())
                        .append(" (").append(detalle.getCantidad()).append(" x $")
                        .append(detalle.getPrecio()).append(") = $")
                        .append(detalle.getCantidad() * detalle.getPrecio()).append("\n");
            }

            body.append("\nSaludos,\nEquipo de Ecommerce");

            message.setText(body.toString());

            Transport.send(message);
        } catch (Exception e) {
            // Loguear el error pero no detener la transacción
            e.printStackTrace();
        }
    }
}