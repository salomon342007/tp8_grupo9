package ar.edu.unju.escmi.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


public class EmfSingleton {

    private static final EntityManagerFactory emf = buildEntityManagerFactory();

    private static EntityManagerFactory buildEntityManagerFactory() {
        try {
            return Persistence.createEntityManagerFactory("tp8PU");
        } catch (Throwable ex) {
            System.err.println("Initial EntityManagerFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Devuelve la instancia compartida de EntityManagerFactory.
     */
    public static EntityManagerFactory getInstance() {
        return emf;
    }

    /**
     * Cierra el factory si está abierto.
     */
    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
