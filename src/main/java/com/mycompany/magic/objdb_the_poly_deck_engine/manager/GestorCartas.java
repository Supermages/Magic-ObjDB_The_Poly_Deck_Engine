package com.mycompany.magic.objdb_the_poly_deck_engine.manager;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.mycompany.magic.objdb_the_poly_deck_engine.model.Carta;
import com.mycompany.magic.objdb_the_poly_deck_engine.model.Encanteri;
import com.mycompany.magic.objdb_the_poly_deck_engine.model.Jugador;
import com.mycompany.magic.objdb_the_poly_deck_engine.model.Mazo;
import com.mycompany.magic.objdb_the_poly_deck_engine.utils.LectorFicheros;

public class GestorCartas {

    private final EntityManager em;

    public GestorCartas(EntityManager em) {
        this.em = em;
    }

    // =========================================================================
    // UTILITATS BÀSIQUES
    // =========================================================================

    /**
     * Mètode auxiliar per netejar la base de dades abans d'una importació.
     */
    public void netejarBaseDades() {
        System.out.println("AVÍS: Esborrant totes les dades actuals de la BD per fer una importació neta...");
        em.getTransaction().begin();
        try {
            // L'ordre és important per les relacions: primer esborrem els que tenen dependències
            em.createQuery("DELETE FROM Jugador").executeUpdate();
            em.createQuery("DELETE FROM Mazo").executeUpdate();
            em.createQuery("DELETE FROM Carta").executeUpdate();
            em.getTransaction().commit();
            System.out.println("Base de dades netejada correctament.");
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.err.println("Error netejant la base de dades: " + e.getMessage());
        }
    }

    /**
     * Retorna tots els jugadors registrats a la BD.
     */
    public List<Jugador> obtenirTotsElsJugadors() {
        return em.createQuery("SELECT j FROM Jugador j", Jugador.class).getResultList();
    }

    // =========================================================================
    // [C]REATE
    // =========================================================================

    /**
     * Operació CREATE: Càrrega des de Fitxer
     */
    public void importarCartes(String rutaArxiu) {
        System.out.println("Llegint cartes del fitxer...");
        
        List<Carta> cartesNoves = LectorFicheros.llegirCartes(rutaArxiu);

        if (cartesNoves.isEmpty()) {
            System.out.println("No s'han trobat cartes per importar.");
            return;
        }

        System.out.println("Iniciant persistència de " + cartesNoves.size() + " cartes...");
        em.getTransaction().begin();

        try {
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

    /**
     * Operació CREATE: Jugadors i Mazos
     */
    public void crearJugadorAmbMazo(String nick, int nivell, String nomMazo, List<Carta> cartesMazo) {
        em.getTransaction().begin();
        try {
            // SOLUCIÓ AL PROBLEMA: Fem servir getResultList per evitar la NoResultException
            List<Jugador> resultats = em.createQuery("SELECT j FROM Jugador j WHERE j.nick = :nick", Jugador.class)
                                        .setParameter("nick", nick)
                                        .getResultList();

            Jugador jugador;
            if (resultats.isEmpty()) {
                // No existeix, el creem de zero
                jugador = new Jugador();
                jugador.setNick(nick);
                jugador.setNivell(nivell);
                System.out.println("Nou jugador creat: " + nick);
            } else {
                // Ja existeix, l'aprofitem
                jugador = resultats.get(0);
                System.out.println("Jugador existent trobat: " + nick);
            }
            
            Mazo mazo = new Mazo();
            mazo.setNom(nomMazo);
            // La relació requereix CascadeType.PERSIST per desar les cartes [cite: 106]
            mazo.setCartes(cartesMazo); 
            
            jugador.getMazos().add(mazo);
            
            // Si Jugador té CascadeType.ALL sobre mazos, això ho guarda tot [cite: 120]
            em.persist(jugador); 
            em.getTransaction().commit();
            System.out.println("Mazo '" + nomMazo + "' assignat correctament a " + nick + ".");
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.err.println("Error creant jugador/mazo: " + e.getMessage());
        }
    }

    // =========================================================================
    // [R]EAD
    // =========================================================================

    /**
     * Testing: Llistar Totes les Cartes
     */
    public List<Carta> testLlistarTotesLesCartes() {
        String jpql = "SELECT c FROM Carta c";
        List<Carta> totes = em.createQuery(jpql, Carta.class).getResultList();
        
        if (totes.isEmpty()) {
            System.out.println("No s'ha trobat cap carta a la base de dades. L'import ha fallat?");
        } else {
            for (Carta c : totes) {
                System.out.println(c.toString());
            }
        }
        return totes;
    }

    /**
     * Tasca A: Obtenir Totes les Cartes
     */
    public List<Carta> obtenirTotesLesCartes() {
        String jpql = "SELECT c FROM Carta c";
        List<Carta> totes = em.createQuery(jpql, Carta.class).getResultList();
        if (totes.isEmpty()) {
            System.out.println("No s'ha trobat cap carta a la base de dades. L'import ha fallat?");
        }
        return totes;
    }

    /**
     * Tasca A (ID): Demostra la Garantia d'Identitat de la Caché L1
     */
    public Carta buscarPerId(long id) {
        Carta c1 = em.find(Carta.class, id);
        Carta c2 = em.find(Carta.class, id);
        
        if (c1 != null && c1 == c2) {
            System.out.println("Garantia d'Identitat confirmada: c1 i c2 són la mateixa instància en memòria[cite: 307].");
        }
        return c1;
    }

    /**
     * Consulta 1: Filtre de Subclasse
     */
    public List<String> buscarNomsCriaturesVoladoresFosques() {
        String jpql = "SELECT c.nom FROM Criatura c WHERE c.vola = true AND c.cost.negre > 2";
        TypedQuery<String> query = em.createQuery(jpql, String.class);
        return query.getResultList();
    }

    /**
     * Consulta 2: Navegació "Deep Path" i Agregació
     */
    public Double calcularMitjanaForcaCriaturesJugador(String nickJugador) {
        Long count = em.createQuery("SELECT COUNT(j) FROM Jugador j WHERE j.nick = :nick", Long.class)
                       .setParameter("nick", nickJugador)
                       .getSingleResult();
                       
        if (count == 0) {
            System.out.println("ERROR: No s'ha trobat cap jugador amb el nick '" + nickJugador + "'.");
            return null; 
        }

        String jpql = "SELECT AVG(c.forca) FROM Criatura c, Jugador j JOIN j.mazos m " +
                      "WHERE j.nick = :nick AND c MEMBER OF m.cartes";
        
        TypedQuery<Double> query = em.createQuery(jpql, Double.class);
        query.setParameter("nick", nickJugador);
        
        Double mitjana = query.getSingleResult();
        return mitjana != null ? mitjana : 0.0;
    }

    /**
     * Consulta 3: Cerca per Component Incrustat (@Embedded)
     */
    public List<Encanteri> buscarEncanterisIncolorsCars() {
        String jpql = "SELECT e FROM Encanteri e WHERE e.cost.blau = 0 AND e.cost.blanc = 0 AND e.cost.incolor > 1";
        TypedQuery<Encanteri> query = em.createQuery(jpql, Encanteri.class);
        return query.getResultList();
    }

    // =========================================================================
    // [U]PDATE
    // =========================================================================

    /**
     * Tasca A: Demostració de Dirty Checking 
     */
    public void actualitzarDescripcioManaged(long idCarta, String novaDescripcio) {
        em.getTransaction().begin();
        Carta carta = em.find(Carta.class, idCarta);
        if (carta != null) {
            carta.setDescripcio(novaDescripcio);
            em.getTransaction().commit();
            System.out.println("Carta actualitzada via Dirty Checking.");
        } else {
            em.getTransaction().rollback();
            System.out.println("No s'ha trobat cap carta amb aquest ID.");
        }
    }

    /**
     * Tasca B: Demostració de Merge amb objecte Detached 
     */
    public void actualitzarNomDetached(long idCarta, String nouNom) {
        Carta cartaDetached = em.find(Carta.class, idCarta);
        if (cartaDetached == null) {
            System.out.println("No s'ha trobat cap carta amb aquest ID.");
            return;
        }
        em.close(); 
        
        cartaDetached.setNom(nouNom);
        
        EntityManager nouEm = com.mycompany.magic.objdb_the_poly_deck_engine.utils.JPAUtil.getEntityManager();
        nouEm.getTransaction().begin();
        nouEm.merge(cartaDetached); 
        nouEm.getTransaction().commit();
        nouEm.close();
        System.out.println("Carta actualitzada via Merge.");
    }

    // =========================================================================
    // [D]ELETE
    // =========================================================================

    /**
     * Tasca A: Eliminar per ID 
     */
    public void eliminarCartaPerId(long idCarta) {
        em.getTransaction().begin();
        Carta carta = em.find(Carta.class, idCarta);
        if (carta != null) {
            em.remove(carta);
            em.getTransaction().commit();
            System.out.println("Carta eliminada de la BD.");
        } else {
            em.getTransaction().rollback();
            System.out.println("No s'ha trobat cap carta amb aquest ID.");
        }
    }

    /**
     * Tasca B: Demostració d'Orphan Removal 
     */
    public void eliminarMazoOrphanRemoval(long idJugador, int indexMazoAEsborrar) {
        em.getTransaction().begin();
        Jugador jugador = em.find(Jugador.class, idJugador);
        
        if (jugador == null) {
            System.out.println("ERROR: No s'ha trobat cap jugador amb l'ID " + idJugador);
            em.getTransaction().rollback();
            return;
        }
        
        if (jugador.getMazos().size() <= indexMazoAEsborrar) {
            System.out.println("ERROR: El jugador " + jugador.getNick() + " no té cap mazo a l'índex " + indexMazoAEsborrar);
            em.getTransaction().rollback();
            return;
        }

        jugador.getMazos().remove(indexMazoAEsborrar); 
        em.getTransaction().commit();
        System.out.println("Mazo eliminat correctament via Orphan Removal.");
    }
}