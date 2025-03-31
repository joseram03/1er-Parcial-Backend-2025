package com.ecommerce.ejb;

import com.ecommerce.model.Categoria;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class CategoriaEJB {

    @PersistenceContext(unitName = "ecommercePU")
    private EntityManager em;

    public List<Categoria> listar() {
        return em.createQuery("SELECT c FROM Categoria c", Categoria.class).getResultList();
    }

    public Categoria obtenerPorId(Long id) {
        return em.find(Categoria.class, id);
    }

    public void guardar(Categoria categoria) {
        if (categoria.getIdCategoria() == null) {
            em.persist(categoria);
        } else {
            em.merge(categoria);
        }
    }

    public void eliminar(Long id) {
        Categoria categoria = obtenerPorId(id);
        if (categoria != null) {
            em.remove(categoria);
        }
    }
}