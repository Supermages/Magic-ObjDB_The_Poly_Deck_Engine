package com.mycompany.magic.objdb_the_poly_deck_engine.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.mycompany.magic.objdb_the_poly_deck_engine.model.enums.Color;
import com.mycompany.magic.objdb_the_poly_deck_engine.model.enums.Rareza;

@Entity
public class Tierra extends Carta {

    @Enumerated(EnumType.STRING) 
    private Color produccio; 
    
    private boolean esBasica; 

    public Tierra() {
        super();
    }

    // Constructor completo
    public Tierra(String nom, String descripcio, Rareza raresa, String edicio, CosteMana cost, 
                 Color produccio, boolean esBasica) {
        
        super(nom, descripcio, raresa, edicio, cost); 
        this.produccio = produccio;
        this.esBasica = esBasica;
    }

    // Getters y Setters
    public Color getProduccio() { return produccio; }
    public void setProduccio(Color produccio) { this.produccio = produccio; }

    public boolean isEsBasica() { return esBasica; }
    public void setEsBasica(boolean esBasica) { this.esBasica = esBasica; }
    
    @Override
    public String toString() {
        String base = super.toString();
        base = base.substring(0, base.lastIndexOf("╚══════════════════════════════════════╝"));
        return base +
               "╠══════════════════════════════════════╣\n" +
               "║  [TERRA] Produeix : " + String.format("%-17s", produccio) + "║\n" +
               "║  Bàsica : " + String.format("%-27s", esBasica ? "Sí" : "No") + "║\n" +
               "╚══════════════════════════════════════╝";
    }
}