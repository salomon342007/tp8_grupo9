package ar.edu.unju.escmi.dao;

import ar.edu.unju.escmi.model.Producto;
import java.util.List;

public interface ProductoDAO extends GenericDAO<Producto, Long> {
    List<Producto> findActive();
}
