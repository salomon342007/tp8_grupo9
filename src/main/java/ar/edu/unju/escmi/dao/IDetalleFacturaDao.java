package ar.edu.unju.escmi.dao;

import ar.edu.unju.escmi.model.DetalleFactura;
import java.util.List;

public interface IDetalleFacturaDao {
    List<DetalleFactura> obtenerDetalles();

    void guardarDetalle(DetalleFactura detalle);
}
