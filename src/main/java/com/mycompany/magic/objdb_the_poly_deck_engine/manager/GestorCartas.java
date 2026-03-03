package com.mycompany.magic.objdb_the_poly_deck_engine.manager;

import java.util.List;

import javax.persistence.EntityManager;

import com.mycompany.magic.objdb_the_poly_deck_engine.model.Carta;
import com.mycompany.magic.objdb_the_poly_deck_engine.utils.LectorFicheros;

public class GestorCartas {

    private EntityManager em;

    public GestorCartas(EntityManager em) {
        this.em = em;
    }

    /**
     * Operació CREATE: Càrrega des de Fitxer [cite: 299]
     */
    public void importarCartes(String rutaArxiu) {
        System.out.println("Llegint cartes del fitxer...");
        
        // 1. Delegamos la lectura al utilitario
        List<Carta> cartesNoves = LectorFicheros.llegirCartes(rutaArxiu);

        if (cartesNoves.isEmpty()) {
            System.out.println("No s'han trobat cartes per importar.");
            return;
        }

        System.out.println("Iniciant persistència de " + cartesNoves.size() + " cartes...");
        
        // 2. Iniciamos transacción
        em.getTransaction().begin();

        try {
            // 3. Pasamos cada objeto de estado New a Managed [cite: 302]
            for (Carta c : cartesNoves) {
                em.persist(c);
            }
            em.getTransaction().commit();
            System.out.println("Cartes importades correctament a la base de dades.");
            
        } catch (Exception e) {
            System.err.println("Error durant la persistència: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }
    
}
