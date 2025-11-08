package ar.edu.unju.escmi.dao;

import ar.edu.unju.escmi.model.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ClienteDAOImpl extends GenericDAOImpl<Cliente, Long> implements ClienteDAO {

    public ClienteDAOImpl() {
        super(Cliente.class);
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
    public void delete(Cliente entity) {
        // eliminación lógica
        entity.setEstado(false);
        update(entity);
    }
}
