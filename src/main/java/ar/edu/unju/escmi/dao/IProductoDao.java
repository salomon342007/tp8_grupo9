package ar.edu.unju.escmi.dao;

import ar.edu.unju.escmi.model.Producto;
import java.util.List;

public interface IProductoDao {
    void guardarProducto(Producto producto);

    double obtenerPrecioPorId(Long idProd);

    void eliminarProducto(Producto producto);

    void modificarPrecio(Long idProd, double nuevoPrecio);

    Producto buscarPorId(Long id);

    List<Producto> obtenerProductos();
}
