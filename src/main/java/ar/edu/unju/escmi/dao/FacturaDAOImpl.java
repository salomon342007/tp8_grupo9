package ar.edu.unju.escmi.dao;

import ar.edu.unju.escmi.model.Factura;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.List;

public class FacturaDAOImpl extends GenericDAOImpl<Factura, Long> implements FacturaDAO {

    public FacturaDAOImpl() {
        super(Factura.class);
    }

    @Override
    public List<Factura> findActive() {
        EntityManager em = em();
        try {
            TypedQuery<Factura> q = em.createQuery("SELECT f FROM Factura f WHERE f.estado = true", Factura.class);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Factura> findByTotalGreaterThan(BigDecimal amount) {
        // JPQL can't call entity method getTotal() directly; use join and compute
        EntityManager em = em();
        try {
            TypedQuery<Factura> q = em.createQuery(
                    "SELECT DISTINCT f FROM Factura f JOIN f.detalles d GROUP BY f HAVING SUM(d.precioUnitario * d.cantidad) > :amount",
                    Factura.class);
            q.setParameter("amount", amount);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Factura entity) {
        entity.setEstado(false);
        update(entity);
    }
}
