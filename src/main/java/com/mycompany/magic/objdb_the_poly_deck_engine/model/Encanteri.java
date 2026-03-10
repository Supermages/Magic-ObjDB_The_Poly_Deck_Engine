package com.mycompany.magic.objdb_the_poly_deck_engine.model;

import javax.persistence.Entity;

import com.mycompany.magic.objdb_the_poly_deck_engine.model.enums.Raresa;

@Entity 
public class Encanteri extends Carta { 
    private String tipus; 
    private boolean esInstantani; 

    public Encanteri() {
        super();
    }

    public Encanteri(String nom, String descripcio, Raresa raresa, String edicio, CostMana cost, String tipus, boolean esInstantani) {

        super(nom, descripcio, raresa, edicio, cost);

        this.tipus = tipus;
        this.esInstantani = esInstantani;
    }

    public String getTipus() { return tipus; }
    public void setTipus(String tipus) { this.tipus = tipus; }

    public boolean isEsInstantani() { return esInstantani; }
    public void setEsInstantani(boolean esInstantani) { this.esInstantani = esInstantani; }
    
    @Override
    public String toString() {
        String base = super.toString();
        base = base.substring(0, base.lastIndexOf("╚══════════════════════════════════════╝"));
        return base +
               "╠══════════════════════════════════════╣\n" +
               "║  [ENCANTERI] " + String.format("%-24s", tipus) + "║\n" +
               "║  Instantani : " + String.format("%-23s", esInstantani ? "Sí" : "No") + "║\n" +
               "╚══════════════════════════════════════╝";
    }
}