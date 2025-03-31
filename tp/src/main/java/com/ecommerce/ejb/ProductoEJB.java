package com.ecommerce.ejb;

import com.ecommerce.model.Producto;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class ProductoEJB {

    @PersistenceContext(unitName = "ecommercePU")
    private EntityManager em;

    public List<Producto> listar(String nombreFiltro, Long idCategoriaFiltro) {
        String jpql = "SELECT p FROM Producto p WHERE 1=1";

        if (nombreFiltro != null && !nombreFiltro.trim().isEmpty()) {
            jpql += " AND LOWER(p.nombre) LIKE LOWER(:nombre)";
        }

        if (idCategoriaFiltro != null) {
            jpql += " AND p.categoria.idCategoria = :idCategoria";
        }

        TypedQuery<Producto> query = em.createQuery(jpql, Producto.class);

        if (nombreFiltro != null && !nombreFiltro.trim().isEmpty()) {
            query.setParameter("nombre", "%" + nombreFiltro.trim() + "%");
        }

        if (idCategoriaFiltro != null) {
            query.setParameter("idCategoria", idCategoriaFiltro);
        }

        return query.getResultList();
    }

    public Producto obtenerPorId(Long id) {
        return em.find(Producto.class, id);
    }

    public void guardar(Producto producto) {
        if (producto.getIdProducto() == null) {
            em.persist(producto);
        } else {
            em.merge(producto);
        }
    }

    public void eliminar(Long id) {
        Producto producto = obtenerPorId(id);
        if (producto != null) {
            em.remove(producto);
        }
    }

    public boolean verificarExistencia(Long idProducto, int cantidad) {
        Producto producto = obtenerPorId(idProducto);
        return producto != null && producto.getCantidadExistente() >= cantidad;
    }

    public void actualizarExistencia(Long idProducto, int cantidad) {
        Producto producto = obtenerPorId(idProducto);
        if (producto != null) {
            producto.setCantidadExistente(producto.getCantidadExistente() - cantidad);
            em.merge(producto);
        }
    }
}
