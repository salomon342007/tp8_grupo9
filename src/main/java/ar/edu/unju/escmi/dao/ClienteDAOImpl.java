package ar.edu.unju.escmi.dao;

import ar.edu.unju.escmi.model.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ClienteDAOImpl extends GenericDAOImpl<Cliente, Long> implements ClienteDAO, IClienteDao {

    public ClienteDAOImpl() {
        super(Cliente.class);
    }

    @Override
    public void guardarCliente(Cliente cliente) {
        save(cliente);
    }

    @Override
    public void modificarCliente(Cliente cliente) {
        update(cliente);
    }

    @Override
    public List<Cliente> obtenerClientes() {
        // Devolver solo clientes activos
        EntityManager em = em();
        try {
            TypedQuery<Cliente> q = em.createQuery("SELECT c FROM Cliente c WHERE c.estado = true", Cliente.class);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Cliente buscarPorDni(String dni) {
        EntityManager em = em();
        try {
            TypedQuery<Cliente> q = em.createQuery("SELECT c FROM Cliente c WHERE c.dni = :dni", Cliente.class);
            q.setParameter("dni", dni);
            List<Cliente> resultado = q.getResultList();
            return resultado.isEmpty() ? null : resultado.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Cliente> findActive() {
        EntityManager em = em();
        try {
            TypedQuery<Cliente> q = em.createQuery("SELECT c FROM Cliente c WHERE c.estado = true", Cliente.class);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void eliminarCliente(Cliente cliente) {
        // Eliminación lógica: marcar estado=false
        cliente.setEstado(false);
        update(cliente);
    }

    @Override
    public void delete(Cliente entity) {
        // eliminación lógica
        entity.setEstado(false);
        update(entity);
    }
}
