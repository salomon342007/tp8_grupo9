package ar.edu.unju.escmi.dao;

import ar.edu.unju.escmi.model.DetalleFactura;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class DetalleFacturaDAOImpl extends GenericDAOImpl<DetalleFactura, Long> implements DetalleFacturaDAO {

    public DetalleFacturaDAOImpl() {
        super(DetalleFactura.class);
    }

    @Override
    public List<DetalleFactura> findAllByFacturaId(Long facturaId) {
        EntityManager em = em();
        try {
            TypedQuery<DetalleFactura> q = em.createQuery("SELECT d FROM DetalleFactura d WHERE d.factura.id = :fid",
                    DetalleFactura.class);
            q.setParameter("fid", facturaId);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
