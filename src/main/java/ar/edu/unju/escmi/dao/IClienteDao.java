package ar.edu.unju.escmi.dao;

import ar.edu.unju.escmi.model.Cliente;
import java.util.List;

public interface IClienteDao {
    void guardarCliente(Cliente cliente);

    void modificarCliente(Cliente cliente);

    List<Cliente> obtenerClientes();

    Cliente buscarPorDni(String dni);

    void eliminarCliente(Cliente cliente);
}
