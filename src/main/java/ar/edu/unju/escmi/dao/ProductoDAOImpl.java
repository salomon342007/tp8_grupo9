package ar.edu.unju.escmi.dao;

import ar.edu.unju.escmi.model.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ProductoDAOImpl extends GenericDAOImpl<Producto, Long> implements ProductoDAO {

    public ProductoDAOImpl() {
        super(Producto.class);
    }

    @Override
    public List<Producto> findActive() {
        EntityManager em = em();
        try {
            TypedQuery<Producto> q = em.createQuery("SELECT p FROM Producto p WHERE p.estado = true", Producto.class);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Producto entity) {
        entity.setEstado(false);
        update(entity);
    }
}
