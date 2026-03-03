package com.mycompany.magic.objdb_the_poly_deck_engine.model;

import javax.persistence.Entity;

import com.mycompany.magic.objdb_the_poly_deck_engine.model.enums.Rareza;

@Entity 
public class Encantamiento extends Carta { 
    private String tipus; 
    private boolean esInstantani; 

    public Encantamiento() {
        super();
    }

    public Encantamiento(String nom, String descripcio, Rareza rareza, String edicio, CosteMana cost, 
                     String tipus, boolean esInstantani) {

        super(nom, descripcio, rareza, edicio, cost);

        this.tipus = tipus;
        this.esInstantani = esInstantani;
    }

    public String getTipus() { return tipus; }
    public void setTipus(String tipus) { this.tipus = tipus; }

    public boolean isEsInstantani() { return esInstantani; }
    public void setEsInstantani(boolean esInstantani) { this.esInstantani = esInstantani; }
    
    @Override
    public String toString() {
        return "Encanteri [" + super.getNom() + "] - Tipus: " + tipus + " (Instantani: " + esInstantani + ")";
    }
}