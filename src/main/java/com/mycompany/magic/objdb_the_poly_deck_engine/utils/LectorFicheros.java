package com.mycompany.magic.objdb_the_poly_deck_engine.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.magic.objdb_the_poly_deck_engine.model.Carta;
import com.mycompany.magic.objdb_the_poly_deck_engine.model.CostMana;
import com.mycompany.magic.objdb_the_poly_deck_engine.model.Criatura;
import com.mycompany.magic.objdb_the_poly_deck_engine.model.Encanteri;
import com.mycompany.magic.objdb_the_poly_deck_engine.model.Terra;
import com.mycompany.magic.objdb_the_poly_deck_engine.model.enums.Color;
import com.mycompany.magic.objdb_the_poly_deck_engine.model.enums.Raresa;

public class LectorFicheros {

    // Funcio per a llegir el fitxer amb les cartes
    public static List<Carta> llegirCartes(String rutaArxiu) {
        List<Carta> cartesLlegides = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArxiu))) {
            String línia;
            String líniaAcumulada = "";

            while ((línia = br.readLine()) != null) {
                línia = línia.trim();
                
                // Ignorar comentaris i línies buides
                if (línia.startsWith("#") || línia.isEmpty()) {
                    continue;
                }

                // Si detecta una nova carta, processa l'anterior (si existeix)
                if (línia.startsWith("CRIATURA") || línia.startsWith("TERRA") || línia.startsWith("ENCANTERI")) {
                    if (!líniaAcumulada.isEmpty()) {
                        cartesLlegides.add(parsejarLinia(líniaAcumulada));
                    }
                    líniaAcumulada = línia;
                } else {
                    // Uneix la línia trencada (ex. la descripció de l'Espectre)
                    líniaAcumulada += " " + línia;
                }
            }
            
            // Afegir l'última carta
            if (!líniaAcumulada.isEmpty()) {
                cartesLlegides.add(parsejarLinia(líniaAcumulada));
            }

        } catch (IOException e) {
            System.err.println("Error físic llegint el fitxer: " + e.getMessage());
        }
        
        return cartesLlegides;
    }

    // Metode per a rebre una linea de un fitxer, y procesarla.
    private static Carta parsejarLinia(String línia) {
        String[] dades = línia.split("\\|");
        for (int i = 0; i < dades.length; i++) {
            dades[i] = dades[i].trim();
        }

        // Aqui agafem totes les dades
        String tipus = dades[0];
        String nom = dades[1];
        String desc = dades[2];
        Raresa raresa = Raresa.valueOf(dades[3].toUpperCase());
        String edicioDefecte = "Core Set 2026";

        // I aqui, segons el seu tipus, creem una clase o una altra
        switch (tipus) {
            case "CRIATURA" -> {
                return new Criatura(nom, desc, raresa, edicioDefecte, parsejarCost(dades[4]),
                        Integer.parseInt(dades[5]), Integer.parseInt(dades[6]),
                        dades[7], Boolean.parseBoolean(dades[8]));
            }
            case "TERRA" -> {
                return new Terra(nom, desc, raresa, edicioDefecte, new CostMana(0,0,0,0,0,0),
                        Color.valueOf(dades[4].toUpperCase()), Boolean.parseBoolean(dades[5]));
            }
            case "ENCANTERI" -> {
                return new Encanteri(nom, desc, raresa, edicioDefecte, parsejarCost(dades[4]),dades[5], Boolean.parseBoolean(dades[6]));
            }
            default -> throw new IllegalArgumentException("Tipus de carta desconegut: " + tipus);
        }
    }

    // Aquesta funcio es exclusivament per formatejar el cost de mana.
    private static CostMana parsejarCost(String costString) {
        String[] c = costString.split(",");
        // Blau, Negre, Vermell, Verd, Blanc, Incolor
        return new CostMana(
            Integer.parseInt(c[4].trim()), Integer.parseInt(c[0].trim()), Integer.parseInt(c[1].trim()), 
            Integer.parseInt(c[2].trim()), Integer.parseInt(c[3].trim()), Integer.parseInt(c[5].trim())
        );
    }
}
