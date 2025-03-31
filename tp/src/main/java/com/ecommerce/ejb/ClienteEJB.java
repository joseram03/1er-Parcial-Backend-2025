package com.ecommerce.ejb;

import com.ecommerce.model.Cliente;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ClienteEJB {

    @PersistenceContext(unitName = "ecommercePU")
    private EntityManager em;

    public List<Cliente> listar() {
        return em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
    }

    public Cliente obtenerPorId(Long id) {
        return em.find(Cliente.class, id);
    }

    public void guardar(Cliente cliente) {
        if (cliente.getIdCliente() == null) {
            em.persist(cliente);
        } else {
            em.merge(cliente);
        }
    }

    public void eliminar(Long id) {
        Cliente cliente = obtenerPorId(id);
        if (cliente != null) {
            em.remove(cliente);
        }
    }
}