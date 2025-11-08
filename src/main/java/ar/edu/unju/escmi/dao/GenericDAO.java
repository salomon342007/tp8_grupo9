package ar.edu.unju.escmi.dao;

import java.util.List;

public interface GenericDAO<T, ID> {
    T save(T entity);

    T update(T entity);

    void delete(T entity); // implement logical delete in entity if desired

    T findById(ID id);

    List<T> findAll();
}
