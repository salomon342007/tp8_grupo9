package ar.edu.unju.escmi.dao;

import ar.edu.unju.escmi.model.Factura;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class FacturaDAOImpl extends GenericDAOImpl<Factura, Long> implements FacturaDAO, IFacturaDao {

    public FacturaDAOImpl() {
        super(Factura.class);
    }

    @Override
    public void guardarFactura(Factura factura) {
        save(factura);
    }

    @Override
    public void eliminarFactura(Factura factura) {
        delete(factura);
    }

    @Override
    public Factura buscarFacturaPorId(long idFactura) {
        return findById(idFactura);
    }

    @Override
    public List<Factura> buscarFacturas() {
        return findAll();
    }

    @Override
    public List<Factura> buscarFacturasConMontoMayorA(double monto) {
        return findByTotalGreaterThan(monto);
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
    public List<Factura> findByTotalGreaterThan(double amount) {
        EntityManager em = em();
        try {
            TypedQuery<Factura> q = em.createQuery(
                    "SELECT DISTINCT f FROM Factura f JOIN f.detalles d GROUP BY f HAVING SUM(d.subtotal) > :amount",
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
