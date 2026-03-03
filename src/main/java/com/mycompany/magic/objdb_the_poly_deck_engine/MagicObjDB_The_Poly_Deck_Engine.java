package com.mycompany.magic.objdb_the_poly_deck_engine;

import javax.persistence.EntityManager;

import com.mycompany.magic.objdb_the_poly_deck_engine.manager.GestorCartas;
import com.mycompany.magic.objdb_the_poly_deck_engine.utils.JPAUtil;

/**
 *
 * @author Grupo X
 */
public class MagicObjDB_The_Poly_Deck_Engine {

    public static void main(String[] args) {
        System.out.println("Arrancant el motor Poly-Deck...");
        
        // 1. Obrim el Context de Persistència (S'inicia la connexió a ObjectDB)
        EntityManager em = JPAUtil.getEntityManager();
        
        // 2. Instanciem el nostre gestor passant-li el EntityManager
        GestorCartas gestor = new GestorCartas(em);
        
        // 3. Executem l'Operació CREATE (Importar des de fitxer)
        // COMPTE: Ajusta la ruta al teu fitxer cartes.txt real!
        gestor.importarCartes("src/main/resources/data/cartes.txt");
        
        // 4. TESTING: Mostrem per pantalla el que s'ha guardat a la BD
        gestor.testLlistarTotesLesCartes();
        
        // 5. Apaguem de forma segura
        em.close();
        JPAUtil.shutdown();
        
        System.out.println("Motor apagat correctament.");
    }
}
