package com.mycompany.magic.objdb_the_poly_deck_engine.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {

    // Equivalent al SessionFactory de Hibernate
    private static final EntityManagerFactory emf;

    // Bloc estàtic per a inicialitzar la factoria una sola vegada en arrancar l'app
    static {
        try {
            // "PolyDeckPU" ha de coincidir EXACTAMENT amb el <persistence-unit name="..."> del persistence.xml
            emf = Persistence.createEntityManagerFactory("PolyDeckPU");
        } catch (Throwable ex) {
            System.err.println("Error catastròfic iniciant l'EntityManagerFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Equivalent a openSession() en Hibernate.
     * Retorna un EntityManager perquè el GestorCartes treballi.
     */
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Tanca la factoria global en apagar l'aplicació.
     */
    public static void shutdown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}