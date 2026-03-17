package com.mycompany.magic.objdb_the_poly_deck_engine;

import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;

import com.mycompany.magic.objdb_the_poly_deck_engine.manager.GestorCartas;
import com.mycompany.magic.objdb_the_poly_deck_engine.model.Carta;
import com.mycompany.magic.objdb_the_poly_deck_engine.model.Encanteri;
import com.mycompany.magic.objdb_the_poly_deck_engine.model.Jugador;
import com.mycompany.magic.objdb_the_poly_deck_engine.utils.JPAUtil;

/**
 * 
 * @author Grupo X
 */
public class MagicObjDB_The_Poly_Deck_Engine {
    // Scanner per llegir l'entrada de l'usuari
    private static final Scanner scanner = new Scanner(System.in);
    private static EntityManager em;
    private static GestorCartas gestor;

    public static void main(String[] args) {
        System.out.println("=== ARRANCANT EL MOTOR POLY-DECK ===");
        // Inicialitzem l'EntityManager i el Gestor de Cartes
        em = JPAUtil.getEntityManager();
        gestor = new GestorCartas(em);
        
        try {
            // Inicialitzem el gestor de cartes
            boolean sortir = false;
            // Bucle principal
            while (!sortir) {
                // Mostrem el menú principal
                mostrarMenuPrincipal();
                int opcio = llegirInt("\nTria una opció: ");
                // Executem l'opció seleccionada
                switch (opcio) {
                    case 1 -> menuCreate();
                    case 2 -> menuRead();
                    case 3 -> menuUpdate();
                    case 4 -> menuDelete();
                    case 0 -> sortir = true;
                    default -> {
                        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━( ERROR )━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                        System.out.println("Opció no vàlida.");
                        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                    }
                }
                // Esperem l'entrada de l'usuari
                if (!sortir) {
                    System.out.print("\nPrem ENTER per continuar...");
                    scanner.nextLine();
                }
            }
            
        } 
        // Captura d'errors
        catch (Exception e) {
            System.err.println("Error crític: " + e.getMessage());
        } 
        // Captura d'errors
        finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
            JPAUtil.shutdown();
            scanner.close();
            System.out.println("\n=== MOTOR APAGAT CORRECTAMENT ===");
        }
    }

    // ==================== DISSENY DELS MENÚS ====================

    private static void mostrarMenuPrincipal() {
        System.out.println( 
            "\n╔══════════════════════════════════════════════╗"+
            "\n║                MENÚ PRINCIPAL                ║"+
            "\n╠══════════════════════════════════════════════╣"+
            "\n║  1. Operacions de Creació (CREATE)           ║"+
            "\n║  2. Consultes i Llistats (READ)              ║"+
            "\n║  3. Operacions d'Actualització (UPDATE)      ║"+
            "\n║  4. Operacions d'Eliminació (DELETE)         ║"+
            "\n║  0. Sortir                                   ║"+
            "\n╚══════════════════════════════════════════════╝"
        );
    }

    private static void menuCreate() {
        System.out.println( 
            "\n╔═══════════════════════════════════════════╗"+
            "\n║                MENÚ CREATE                ║"+
            "\n╠═══════════════════════════════════════════╣"+
            "\n║  1. Importar Cartes (Neteja BD prèvia)    ║"+
            "\n║  2. Crear Jugador i Mazo                  ║"+
            "\n║  0. Tornar                                ║"+
            "\n╚═══════════════════════════════════════════╝"
        );
        
        int opcio = llegirInt("\nTria una opció: ");
        // Executem l'opció seleccionada
        switch (opcio) {
            case 1 -> {
                // Importar cartes
                System.out.print("Ruta del fitxer (Prem Enter per defecte: src/main/resources/data/cartes.txt): ");
                String ruta = scanner.nextLine();
                if (ruta.trim().isEmpty()) {
                    ruta = "src/main/resources/data/cartes.txt";
                }
                System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━( PROCESSANT IMPORTACIÓ )━━━━━━━━━━━━━━━━━━━━━━━");
                // Netejar la base de dades
                gestor.netejarBaseDades();
                // Importar cartes
                gestor.importarCartes(ruta);
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            }
            // Crear Jugador i Mazo
            case 2 -> {
                // Comprovem que hi hagi cartes disponibles
                List<Carta> totes = gestor.obtenirTotesLesCartes();
                if (totes.size() < 3) {
                    System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━( ERROR )━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                    System.out.println("Has d'importar cartes prèviament per tenir-ne almenys 3 per al mazo.");
                    System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                } 
                // Si hi ha cartes disponibles, continuem amb la creació
                else {
                    System.out.println("━━━━━━━━━━━━━━━━━━━━━━( CREANT JUGADOR I MAZO )━━━━━━━━━━━━━━━━━━━━━━");
                    List<Jugador> jugadors = gestor.obtenirTotsElsJugadors();
                    // Comprovem si hi ha jugadors registrats
                    if (jugadors.isEmpty()) {
                        System.out.println("No hi ha jugadors registrats actualment.");
                    } 
                    // Si hi ha jugadors registrats, els mostrem
                    else {
                        System.out.println("Jugadors existents:");
                        for (Jugador j : jugadors) {
                            System.out.println(" - " + j.getNick());
                        }
                    }
                    // Demanem el Nick i Nom del Mazo
                    System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                    // Comprovem si el jugador és nou o existent
                    System.out.print("Nick del Jugador (Nou o Existent): ");
                    String nick = scanner.nextLine();
                    System.out.print("Nom del Mazo: ");
                    String nomMazo = scanner.nextLine();
                    // Asignem les cartes al mazo
                    List<Carta> cartasMazo = totes;
                    System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━( RESULTATS CREACIÓ )━━━━━━━━━━━━━━━━━━━━━━━━━");
                    // Mostrem els detalls del nou jugador i el seu mazo
                    gestor.crearJugadorAmbMazo(nick, 10, nomMazo, cartasMazo);
                    System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                }
            }
            case 0 -> {
            }
            default -> System.out.println("Opció no vàlida.");
        }
    }

    private static void menuRead() {
        System.out.println( 
            "\n╔═════════════════════════════════════════════════════════╗"+
            "\n║                        MENÚ READ                        ║"+
            "\n╠═════════════════════════════════════════════════════════╣"+
            "\n║  1. Llistar Totes les Cartes                            ║"+
            "\n║  2. Test Caché L1 (Buscar per ID)                       ║"+
            "\n║  3. Buscar: Noms de Criatures Voladores Fosques         ║"+
            "\n║  4. Buscar: Mitjana de Força de Criatures d'un Jugador  ║"+
            "\n║  5. Buscar: Encanteris Incolors Cars                    ║"+
            "\n║  0. Tornar                                              ║"+
            "\n╚═════════════════════════════════════════════════════════╝"
        );
        // Llegim l'opció seleccionada
        int opcio = llegirInt("\nTria una opció: ");
        // Validem l'opció seleccionada
        switch (opcio) {
            // Llistar Totes les Cartes
            case 1 -> {
                System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━( LLISTAT DE CARTES )━━━━━━━━━━━━━━━━━━━━━━━━━");
                gestor.testLlistarTotesLesCartes();
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            }
            // Buscar per ID
            case 2 -> {
                long idBuscar = llegirLong("Introdueix l'ID de la Carta a buscar: ");
                System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━( RESULTATS CACHÉ L1 )━━━━━━━━━━━━━━━━━━━━━━━━");
                gestor.buscarPerId(idBuscar);
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            }
            // Buscar: Noms de Criatures Voladores Fosques
            case 3 -> {
                System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━( CONSULTA JPQL )━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                List<String> noms = gestor.buscarNomsCriaturesVoladoresFosques();
                if(noms.isEmpty()) {
                    System.out.println("No s'han trobat criatures amb aquests criteris.");
                } else {
                    noms.forEach(nom -> System.out.println("- " + nom));
                }
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            }
            // Buscar: Mitjana de Força de Criatures d'un Jugador
            case 4 ->{
                System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━( CONSULTA JPQL )━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                if (mostrarJugadors()) {
                    // Comprovem si el jugador és nou o existent
                    System.out.print("\nIntrodueix el Nick del Jugador de la llista: ");
                    String nickBusqueda = scanner.nextLine();
                    Double mitjana = gestor.calcularMitjanaForcaCriaturesJugador(nickBusqueda);
                    
                    if (mitjana != null) {
                        System.out.println("La mitjana de força de les criatures de " + nickBusqueda + " és: " + mitjana);
                    }
                }
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            }
            // Buscar: Encanteris Incolors Cars
            case 5 -> {
                // Comprovem si el jugador és nou o existent
                System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━( CONSULTA JPQL )━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                List<Encanteri> encanteris = gestor.buscarEncanterisIncolorsCars();
                if(encanteris.isEmpty()) {
                    System.out.println("No s'han trobat encanteris amb aquests criteris.");
                } 
                // Si s'han trobat encanteris, els mostrem
                else {
                    encanteris.forEach(enc -> System.out.println(enc.toString()));
                }
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            }
            case 0 -> {
            }
            default -> System.out.println("Opció no vàlida.");
        }
    }

    private static void menuUpdate() {
        System.out.println( 
            "\n╔═════════════════════════════════════════╗"+
            "\n║               MENÚ UPDATE               ║"+
            "\n╠═════════════════════════════════════════╣"+
            "\n║  1. Provar Dirty Checking (Managed)     ║"+
            "\n║  2. Provar Merge (Detached)             ║"+
            "\n║  0. Tornar                              ║"+
            "\n╚═════════════════════════════════════════╝"
        );
        // Llegim l'opció seleccionada
        int opcio = llegirInt("\nTria una opció: ");
        
        switch (opcio) {
            // Provar Dirty Checking (Managed)
            case 1 -> {
                System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━( LLISTAT DE CARTES )━━━━━━━━━━━━━━━━━━━━━━━━━");
                gestor.testLlistarTotesLesCartes();
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━( INTRODUEIX DADES )━━━━━━━━━━━━━━━━━━━━━━━━━");
                // Llegim les dades per a fer el dirty checking
                long idDirty = llegirLong("Introdueix l'ID de la Carta a modificar (Dirty Checking): ");
                System.out.print("Nova descripció: ");
                String novaDesc = scanner.nextLine();
                
                System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━( RESULTATS UPDATE )━━━━━━━━━━━━━━━━━━━━━━━━━");
                gestor.actualitzarDescripcioManaged(idDirty, novaDesc);
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            }
            // Provar Merge (Detached)
            case 2 -> {
                System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━( LLISTAT DE CARTES )━━━━━━━━━━━━━━━━━━━━━━━━━");
                gestor.testLlistarTotesLesCartes();
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━( INTRODUEIX DADES )━━━━━━━━━━━━━━━━━━━━━━━━━");
                // Llegim les dades per a fer el merge
                long idMerge = llegirLong("Introdueix l'ID de la Carta a fer merge (Detached): ");
                System.out.print("Nou nom: ");
                String nouNom = scanner.nextLine();
                
                System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━( RESULTATS UPDATE )━━━━━━━━━━━━━━━━━━━━━━━━━");
                gestor.actualitzarNomDetached(idMerge, nouNom);
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                
                em = JPAUtil.getEntityManager();
                gestor = new GestorCartas(em);
            }
            case 0 -> {
            }
            default -> System.out.println("Opció no vàlida.");
        }
    }

    private static void menuDelete() {
        System.out.println( 
            "\n╔═══════════════════════════════════════════════╗"+
            "\n║                  MENÚ DELETE                  ║"+
            "\n╠═══════════════════════════════════════════════╣"+
            "\n║  1. Esborrar Carta per ID (Remove)            ║"+
            "\n║  2. Provar Orphan Removal en Mazo             ║"+
            "\n║  0. Tornar                                    ║"+
            "\n╚═══════════════════════════════════════════════╝"
        );
        // Llegim l'opció seleccionada
        int opcio = llegirInt("\nTria una opció: ");
        // Validem l'opció seleccionada
        switch (opcio) {
            // Esborrar Carta per ID (Remove)
            case 1 -> {
                long idBorrar = llegirLong("Introdueix l'ID de la Carta a esborrar: ");
                
                System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━( RESULTATS ELIMINACIÓ )━━━━━━━━━━━━━━━━━━━━━━━");
                // Realitzem l'eliminació
                gestor.eliminarCartaPerId(idBorrar);
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            }
            // Provar Orphan Removal en Mazo
            case 2 -> {
                System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━( RESULTATS ELIMINACIÓ )━━━━━━━━━━━━━━━━━━━━━━━");
                if (mostrarJugadors()) {
                    // Llegim les dades per a fer l'orphan removal
                    long idJugador = llegirLong("\nIntrodueix l'ID del Jugador propietari del mazo: ");
                    int indexMazo = llegirInt("Introdueix l'índex del mazo a esborrar (ex: 0 pel primer): ");
                    
                    gestor.eliminarMazoOrphanRemoval(idJugador, indexMazo);
                }
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            }
            case 0 -> {
            }
            default -> System.out.println("Opció no vàlida.");
        }
    }

    // ==================== UTILITATS DE LECTURA ====================
    // Mètodes per llegir dades de l'entrada
    private static int llegirInt(String missatge) {
        // Llegim un número sencer de l'entrada
        System.out.print(missatge);
        while (!scanner.hasNextInt()) {
            scanner.next(); 
            System.out.print("Si us plau, introdueix un número vàlid: ");
        }
        // Llegim el resultat
        int resultat = scanner.nextInt();
        scanner.nextLine(); 
        return resultat;
    }

    // Mètodes per llegir dades de l'entrada
    private static long llegirLong(String missatge) {
        // Llegim un número llarg de l'entrada
        System.out.print(missatge);
        while (!scanner.hasNextLong()) {
            scanner.next(); // Descarta l'entrada incorrecta
            System.out.print("Si us plau, introdueix un número vàlid: ");
        }
        long resultat = scanner.nextLong();
        scanner.nextLine(); // Netejar el salt de línia
        return resultat;
    }

    // Llista els jugadors per pantalla i retorna true si n'hi ha algun.
    private static boolean mostrarJugadors() {
        // Obtenim la llista de jugadors
        List<Jugador> jugadors = gestor.obtenirTotsElsJugadors();
        if (jugadors.isEmpty()) {
            System.out.println("Actualment no hi ha cap jugador registrat.");
            return false;
        }
        // Mostrem la llista de jugadors
        System.out.println("--- JUGADORS DISPONIBLES ---");
        for (Jugador j : jugadors) {
            System.out.println("ID: " + j.getId() + " | Nick: " + j.getNick() + 
                               " | Nivell: " + j.getNivell() + 
                               " | Mazos creats: " + j.getMazos().size());
        }
        // Mostrem el nombre total de jugadors
        System.out.println("----------------------------");
        return true;
    }
}