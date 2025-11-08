package ar.edu.unju.escmi.dao;

import ar.edu.unju.escmi.model.Factura;
import java.math.BigDecimal;
import java.util.List;

public interface FacturaDAO extends GenericDAO<Factura, Long> {
    List<Factura> findActive();

    List<Factura> findByTotalGreaterThan(BigDecimal amount);
}
