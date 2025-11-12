package ar.edu.unju.escmi.dao;

import ar.edu.unju.escmi.model.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ProductoDAOImpl extends GenericDAOImpl<Producto, Long> implements ProductoDAO, IProductoDao {

    public ProductoDAOImpl() {
        super(Producto.class);
    }

    @Override
    public void guardarProducto(Producto producto) {
        save(producto);
    }

    @Override
    public double obtenerPrecioPorId(Long idProd) {
        Producto p = findById(idProd);
        return p != null ? p.getPrecioUnitario() : 0.0;
    }

    @Override
    public void eliminarProducto(Producto producto) {
        delete(producto);
    }

    @Override
    public void modificarPrecio(Long idProd, double nuevoPrecio) {
        Producto p = findById(idProd);
        if (p != null) {
            p.setPrecioUnitario(nuevoPrecio);
            update(p);
        }
    }

    @Override
    public Producto buscarPorId(Long id) {
        return findById(id);
    }

    @Override
    public List<Producto> obtenerProductos() {
        return findAll();
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
