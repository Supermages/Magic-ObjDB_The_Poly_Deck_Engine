package com.mycompany.magic.objdb_the_poly_deck_engine.manager;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.mycompany.magic.objdb_the_poly_deck_engine.model.Carta;
import com.mycompany.magic.objdb_the_poly_deck_engine.model.Encantamiento;
import com.mycompany.magic.objdb_the_poly_deck_engine.model.Jugador;
import com.mycompany.magic.objdb_the_poly_deck_engine.model.Mazo;
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

    // =========================================================================
    // OPERACIÓ READ: CONSULTES BÀSIQUES I CACHÉ L1 
    // =========================================================================

    /**
     * Tasca A (ID): Demostra la Garantia d'Identitat de la Caché L1 [cite: 305-307]
     */
    public Carta buscarPerId(long id) {
        Carta c1 = em.find(Carta.class, id);
        Carta c2 = em.find(Carta.class, id);
        
        // Si la caché L1 funciona, ObjectDB no va a disco 2 veces. 
        // Devuelve exactamente el mismo espacio en memoria.
        if (c1 == c2) {
            System.out.println("Garantia d'Identitat confirmada: c1 i c2 són la mateixa instància en memòria[cite: 307].");
        }
        return c1;
    }

    // =========================================================================
    // OPERACIÓ READ: CONSULTES POLIMÒRFIQUES AVANÇADES (JPQL)
    // =========================================================================

    /**
     * Consulta 1: Filtre de Subclasse [cite: 309]
     * "Mostra el nom de totes les Criatures que volen i tenen cost negre > 2" 
     */
    public List<String> buscarNomsCriaturesVoladoresFosques() {
        // En JPQL consultamos la clase 'Criatura', no tablas SQL. Accedemos a 'cost.negre' directamente.
        String jpql = "SELECT c.nom FROM Criatura c WHERE c.vola = true AND c.cost.negre > 2";
        TypedQuery<String> query = em.createQuery(jpql, String.class);
        return query.getResultList();
    }

    /**
     * Consulta 2: Navegació "Deep Path" i Agregació [cite: 314]
     * "Mitjana de força de les criatures als mazos d'un jugador concret" [cite: 315]
     */
    public Double calcularMitjanaForcaCriaturesJugador(String nickJugador) {
        // Navegamos: Jugador -> Mazo -> Carta. Filtramos por tipo Criatura para poder leer 'forca'.
        String jpql = "SELECT AVG(c.forca) FROM Criatura c, Jugador j JOIN j.mazos m " +
                      "WHERE j.nick = :nick AND c MEMBER OF m.cartes";
        
        TypedQuery<Double> query = em.createQuery(jpql, Double.class);
        query.setParameter("nick", nickJugador);
        
        Double mitjana = query.getSingleResult();
        return mitjana != null ? mitjana : 0.0;
    }

    /**
     * Consulta 3: Cerca per Component Incrustat (@Embedded) [cite: 317]
     * "Encanteris que no costen blau ni blanc (0), però incolor > 3" [cite: 318]
     */
    public List<Encantamiento> buscarEncanterisIncolorsCars() {
        // Accedemos a los atributos del @Embeddable 'CostMana' desde la entidad principal [cite: 319]
        String jpql = "SELECT e FROM Encantamiento e WHERE e.cost.blau = 0 AND e.cost.blanc = 0 AND e.cost.incolor > 3";
        TypedQuery<Encantamiento> query = em.createQuery(jpql, Encantamiento.class);
        return query.getResultList();
    }

    // =========================================================================
    // TESTING: COMPROVACIÓ DE CÀRREGA DE DADES
    // =========================================================================

    /**
     * Recupera absolutament totes les cartes de la BD per comprovar l'import.
     * Demostra el polimorfisme de JPA: Retornarà Criatures, Terres i Encanteris barrejats.
     */
    public List<Carta> testLlistarTotesLesCartes() {
        
        // Consulta JPQL ultra-neta. Fes atenció a que no hi ha JOINs.
        String jpql = "SELECT c FROM Carta c";
        
        // TypedQuery ens evita haver de fer (List<Carta>) i suprimir warnings
        List<Carta> totes = em.createQuery(jpql, Carta.class).getResultList();
        
        if (totes.isEmpty()) {
            System.out.println("No s'ha trobat cap carta a la base de dades. L'import ha fallat?");
        } else {
            for (Carta c : totes) {
                // Al cridar c.toString(), Java executarà l'override de la filla corresponent!
                System.out.println(c.toString());
            }
            System.out.println("Total de cartes trobades: " + totes.size());
        }
        
        return totes;
    }

    // =========================================================================
    // OPERACIÓ CREATE: JUGADORS I MAZOS
    // =========================================================================

    public void crearJugadorAmbMazo(String nick, int nivell, String nomMazo, List<Carta> cartesMazo) {
        em.getTransaction().begin();
        try {
            Jugador jugador = new Jugador();
            jugador.setNick(nick);
            jugador.setNivell(nivell);
            
            Mazo mazo = new Mazo();
            mazo.setNom(nomMazo);
            // La relació requereix CascadeType.PERSIST per desar les cartes [cite: 106]
            mazo.setCartes(cartesMazo); 
            
            jugador.getMazos().add(mazo);
            
            // Si Jugador té CascadeType.ALL sobre mazos, això ho guarda tot [cite: 120]
            em.persist(jugador); 
            em.getTransaction().commit();
            System.out.println("Jugador " + nick + " i el seu mazo creats correctament.");
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.err.println("Error creant jugador: " + e.getMessage());
        }
    }

    // =========================================================================
    // OPERACIÓ UPDATE: DIRTY CHECKING I MERGE
    // =========================================================================

    /**
     * Tasca A: Demostració de Dirty Checking 
     */
    public void actualitzarDescripcioManaged(long idCarta, String novaDescripcio) {
        em.getTransaction().begin();
        Carta carta = em.find(Carta.class, idCarta);
        if (carta != null) {
            carta.setDescripcio(novaDescripcio);
            // NO fem em.persist(). JPA detecta el canvi automàticament al fer commit.
            em.getTransaction().commit();
            System.out.println("Carta actualitzada via Dirty Checking.");
        } else {
            em.getTransaction().rollback();
        }
    }

    /**
     * Tasca B: Demostració de Merge amb objecte Detached 
     */
    public void actualitzarNomDetached(long idCarta, String nouNom) {
        // 1. Recuperem i tanquem (simulem que passa a una altra capa de l'app)
        Carta cartaDetached = em.find(Carta.class, idCarta);
        em.close(); 
        
        // 2. Modifiquem l'objecte (ara està Detached)
        cartaDetached.setNom(nouNom);
        
        // 3. Obrim nou EM i fem merge
        EntityManager nouEm = com.mycompany.magic.objdb_the_poly_deck_engine.utils.JPAUtil.getEntityManager();
        nouEm.getTransaction().begin();
        
        nouEm.merge(cartaDetached); // Reenganxem l'objecte al context
        
        nouEm.getTransaction().commit();
        nouEm.close();
        System.out.println("Carta actualitzada via Merge.");
    }

    // =========================================================================
    // OPERACIÓ DELETE: REMOVE I ORPHAN REMOVAL
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
        }
    }

    /**
     * Tasca B: Demostració d'Orphan Removal 
     * Requereix que a Jugador hi hagi: @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) [cite: 120]
     */
    public void eliminarMazoOrphanRemoval(long idJugador, int indexMazoAEsborrar) {
        em.getTransaction().begin();
        Jugador jugador = em.find(Jugador.class, idJugador);
        
        if (jugador != null && jugador.getMazos().size() > indexMazoAEsborrar) {
            // Només traient el mazo de la llista Java, Hibernate/ObjectDB l'esborra de la BD
            // gràcies a orphanRemoval=true[cite: 120].
            jugador.getMazos().remove(indexMazoAEsborrar); 
            em.getTransaction().commit();
            System.out.println("Mazo eliminat via Orphan Removal.");
        } else {
            em.getTransaction().rollback();
        }
    }

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
    
}
