package ar.edu.unju.escmi.dao;

import ar.edu.unju.escmi.model.Factura;
import java.util.List;

public interface IFacturaDao {
    void guardarFactura(Factura factura);

    void eliminarFactura(Factura factura);
    
    Factura buscarFacturaPorId(long idFactura);

    List<Factura> buscarFacturas();

    List<Factura> buscarFacturasConMontoMayorA(double monto);
}
