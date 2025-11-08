package ar.edu.unju.escmi.dao;

import ar.edu.unju.escmi.model.DetalleFactura;
import java.util.List;

public interface DetalleFacturaDAO extends GenericDAO<DetalleFactura, Long> {
    List<DetalleFactura> findAllByFacturaId(Long facturaId);
}
