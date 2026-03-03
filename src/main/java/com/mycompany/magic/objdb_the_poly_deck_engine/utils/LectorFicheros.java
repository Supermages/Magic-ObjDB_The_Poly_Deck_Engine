package com.mycompany.magic.objdb_the_poly_deck_engine.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.magic.objdb_the_poly_deck_engine.model.Carta;
import com.mycompany.magic.objdb_the_poly_deck_engine.model.CosteMana;
import com.mycompany.magic.objdb_the_poly_deck_engine.model.Criatura;
import com.mycompany.magic.objdb_the_poly_deck_engine.model.Encantamiento;
import com.mycompany.magic.objdb_the_poly_deck_engine.model.Tierra;
import com.mycompany.magic.objdb_the_poly_deck_engine.model.enums.Color;
import com.mycompany.magic.objdb_the_poly_deck_engine.model.enums.Rareza;

public class LectorFicheros {

    public static List<Carta> llegirCartes(String rutaArxiu) {
        List<Carta> cartesLlegides = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArxiu))) {
            String línia;
            String líniaAcumulada = "";

            while ((línia = br.readLine()) != null) {
                línia = línia.trim();
                
                // Ignorar comentarios y líneas vacías
                if (línia.startsWith("#") || línia.isEmpty()) {
                    continue;
                }

                // Si detecta una nueva carta, procesa la anterior (si existe)
                if (línia.startsWith("CRIATURA") || línia.startsWith("TERRA") || línia.startsWith("ENCANTERI")) {
                    if (!líniaAcumulada.isEmpty()) {
                        cartesLlegides.add(parsejarLinia(líniaAcumulada));
                    }
                    líniaAcumulada = línia;
                } else {
                    // Une la línea rota (ej. la descripción del Espectre)
                    líniaAcumulada += " " + línia;
                }
            }
            
            // Añadir la última carta
            if (!líniaAcumulada.isEmpty()) {
                cartesLlegides.add(parsejarLinia(líniaAcumulada));
            }

        } catch (IOException e) {
            System.err.println("Error físic llegint el fitxer: " + e.getMessage());
        }
        
        return cartesLlegides;
    }

    private static Carta parsejarLinia(String línia) {
        String[] dades = línia.split("\\|");
        for (int i = 0; i < dades.length; i++) {
            dades[i] = dades[i].trim();
        }

        String tipus = dades[0];
        String nom = dades[1];
        String desc = dades[2];
        Rareza raresa = Rareza.valueOf(dades[3].toUpperCase());
        String edicioDefecte = "Core Set 2026";

        switch (tipus) {
            case "CRIATURA" -> {
                return new Criatura(nom, desc, raresa, edicioDefecte, parsejarCost(dades[4]),
                        Integer.parseInt(dades[5]), Integer.parseInt(dades[6]),
                        dades[7], Boolean.parseBoolean(dades[8]));
            }
            case "TERRA" -> {
                return new Tierra(nom, desc, raresa, edicioDefecte, new CosteMana(0,0,0,0,0,0),
                        Color.valueOf(dades[4].toUpperCase()), Boolean.parseBoolean(dades[5]));
            }
            case "ENCANTERI" -> {
                return new Encantamiento(nom, desc, raresa, edicioDefecte, parsejarCost(dades[4]),
                        dades[5], Boolean.parseBoolean(dades[6]));
            }
            default -> throw new IllegalArgumentException("Tipus de carta desconegut: " + tipus);
        }
    }

    private static CosteMana parsejarCost(String costString) {
        String[] c = costString.split(",");
        // Blau, Negre, Vermell, Verd, Blanc, Incolor
        return new CosteMana(
            Integer.parseInt(c[4].trim()), Integer.parseInt(c[0].trim()), Integer.parseInt(c[1].trim()), 
            Integer.parseInt(c[2].trim()), Integer.parseInt(c[3].trim()), Integer.parseInt(c[5].trim())
        );
    }
}
