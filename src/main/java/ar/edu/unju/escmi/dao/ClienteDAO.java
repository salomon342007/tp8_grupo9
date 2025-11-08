package ar.edu.unju.escmi.dao;

import ar.edu.unju.escmi.model.Cliente;
import java.util.List;

public interface ClienteDAO extends GenericDAO<Cliente, Long> {
    List<Cliente> findActive();
}
