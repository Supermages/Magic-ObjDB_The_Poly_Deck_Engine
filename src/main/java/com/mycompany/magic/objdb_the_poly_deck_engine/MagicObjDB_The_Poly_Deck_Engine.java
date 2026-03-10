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

    private static final Scanner scanner = new Scanner(System.in);
    private static EntityManager em;
    private static GestorCartas gestor;

    public static void main(String[] args) {
        System.out.println("=== ARRANCANT EL MOTOR POLY-DECK ===");
        
        em = JPAUtil.getEntityManager();
        gestor = new GestorCartas(em);
        
        try {
            boolean sortir = false;
            
            while (!sortir) {
                mostrarMenuPrincipal();
                int opcio = llegirInt("\nTria una opció: ");
                
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
                
                if (!sortir) {
                    System.out.print("\nPrem ENTER per continuar...");
                    scanner.nextLine();
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error crític: " + e.getMessage());
        } finally {
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
        
        switch (opcio) {
            case 1:
                System.out.print("Ruta del fitxer (Prem Enter per defecte: src/main/resources/data/cartes.txt): ");
                String ruta = scanner.nextLine();
                if (ruta.trim().isEmpty()) {
                    ruta = "src/main/resources/data/cartes.txt";
                }
                
                System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━( PROCESSANT IMPORTACIÓ )━━━━━━━━━━━━━━━━━━━━━━━");
                gestor.netejarBaseDades();
                gestor.importarCartes(ruta);
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                break;
                
            case 2:
                List<Carta> totes = gestor.testLlistarTotesLesCartes();
                if (totes.size() < 3) {
                    System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━( ERROR )━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                    System.out.println("Has d'importar cartes prèviament per tenir-ne almenys 3 per al mazo.");
                    System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                } else {
                    System.out.print("Nick del Jugador: ");
                    String nick = scanner.nextLine();
                    System.out.print("Nom del Mazo: ");
                    String nomMazo = scanner.nextLine();
                    
                    List<Carta> cartasMazo = totes;
                    
                    System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━( RESULTATS CREACIÓ )━━━━━━━━━━━━━━━━━━━━━━━━━");
                    gestor.crearJugadorAmbMazo(nick, 10, nomMazo, cartasMazo);
                    System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                }
                break;
            case 0:
                break;
            default:
                System.out.println("Opció no vàlida.");
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
        
        int opcio = llegirInt("\nTria una opció: ");
        
        switch (opcio) {
            case 1 -> {
                System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━( LLISTAT DE CARTES )━━━━━━━━━━━━━━━━━━━━━━━━━");
                gestor.testLlistarTotesLesCartes();
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            }
            case 2 -> {
                long idBuscar = llegirLong("Introdueix l'ID de la Carta a buscar: ");
                System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━( RESULTATS CACHÉ L1 )━━━━━━━━━━━━━━━━━━━━━━━━");
                gestor.buscarPerId(idBuscar);
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            }
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
            case 4 ->{
                System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━( CONSULTA JPQL )━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                if (mostrarJugadors()) {
                    System.out.print("\nIntrodueix el Nick del Jugador de la llista: ");
                    String nickBusqueda = scanner.nextLine();
                    Double mitjana = gestor.calcularMitjanaForcaCriaturesJugador(nickBusqueda);
                    
                    if (mitjana != null) {
                        System.out.println("La mitjana de força de les criatures de " + nickBusqueda + " és: " + mitjana);
                    }
                }
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            }
            case 5 -> {
                System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━( CONSULTA JPQL )━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                List<Encanteri> encanteris = gestor.buscarEncanterisIncolorsCars();
                if(encanteris.isEmpty()) {
                    System.out.println("No s'han trobat encanteris amb aquests criteris.");
                } else {
                    encanteris.forEach(enc -> System.out.println("- " + enc.toString()));
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
        
        int opcio = llegirInt("\nTria una opció: ");
        
        switch (opcio) {
            case 1 -> {
                long idDirty = llegirLong("Introdueix l'ID de la Carta a modificar (Dirty Checking): ");
                System.out.print("Nova descripció: ");
                String novaDesc = scanner.nextLine();
                
                System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━( RESULTATS UPDATE )━━━━━━━━━━━━━━━━━━━━━━━━━");
                gestor.actualitzarDescripcioManaged(idDirty, novaDesc);
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            }
            case 2 -> {
                long idMerge = llegirLong("Introdueix l'ID de la Carta a fer merge (Detached): ");
                System.out.print("Nou nom: ");
                String nouNom = scanner.nextLine();
                
                System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━( RESULTATS UPDATE )━━━━━━━━━━━━━━━━━━━━━━━━━");
                gestor.actualitzarNomDetached(idMerge, nouNom);
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                
                // Reobrim l'EM per a seguir utilitzant el menú després del test detached
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
        
        int opcio = llegirInt("\nTria una opció: ");
        
        switch (opcio) {
            case 1 -> {
                long idBorrar = llegirLong("Introdueix l'ID de la Carta a esborrar: ");
                
                System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━( RESULTATS ELIMINACIÓ )━━━━━━━━━━━━━━━━━━━━━━━");
                gestor.eliminarCartaPerId(idBorrar);
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            }
            case 2 -> {
                System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━( RESULTATS ELIMINACIÓ )━━━━━━━━━━━━━━━━━━━━━━━");
                if (mostrarJugadors()) {
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

    private static int llegirInt(String missatge) {
        System.out.print(missatge);
        while (!scanner.hasNextInt()) {
            scanner.next(); // Descarta l'entrada incorrecta
            System.out.print("Si us plau, introdueix un número vàlid: ");
        }
        int resultat = scanner.nextInt();
        scanner.nextLine(); // Netejar el salt de línia
        return resultat;
    }

    private static long llegirLong(String missatge) {
        System.out.print(missatge);
        while (!scanner.hasNextLong()) {
            scanner.next(); // Descarta l'entrada incorrecta
            System.out.print("Si us plau, introdueix un número vàlid: ");
        }
        long resultat = scanner.nextLong();
        scanner.nextLine(); // Netejar el salt de línia
        return resultat;
    }

    /**
     * Llista els jugadors per pantalla i retorna true si n'hi ha algun.
     */
    private static boolean mostrarJugadors() {
        List<Jugador> jugadors = gestor.obtenirTotsElsJugadors();
        if (jugadors.isEmpty()) {
            System.out.println("Actualment no hi ha cap jugador registrat.");
            return false;
        }
        
        System.out.println("--- JUGADORS DISPONIBLES ---");
        for (Jugador j : jugadors) {
            System.out.println("ID: " + j.getId() + " | Nick: " + j.getNick() + 
                               " | Nivell: " + j.getNivell() + 
                               " | Mazos creats: " + j.getMazos().size());
        }
        System.out.println("----------------------------");
        return true;
    }
}