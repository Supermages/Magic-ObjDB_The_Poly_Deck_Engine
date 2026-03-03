package com.mycompany.magic.objdb_the_poly_deck_engine.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {

    // Equivalente al SessionFactory de Hibernate
    private static final EntityManagerFactory emf;

    // Bloque estático para inicializar la factoría una sola vez al arrancar la app
    static {
        try {
            // "PolyDeckPU" debe coincidir EXACTAMENTE con el <persistence-unit name="..."> del persistence.xml
            emf = Persistence.createEntityManagerFactory("PolyDeckPU");
        } catch (Throwable ex) {
            System.err.println("Error catastròfic iniciant l'EntityManagerFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Equivalente a openSession() en Hibernate.
     * Retorna un EntityManager para que el GestorCartes trabaje.
     */
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Cierra la factoría global al apagar la aplicación.
     */
    public static void shutdown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}