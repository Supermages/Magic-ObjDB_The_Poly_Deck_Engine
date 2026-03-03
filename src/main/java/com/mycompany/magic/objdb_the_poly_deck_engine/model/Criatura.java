package com.mycompany.magic.objdb_the_poly_deck_engine.model;
import javax.persistence.Entity;

import com.mycompany.magic.objdb_the_poly_deck_engine.model.enums.Rareza;

@Entity
public class Criatura extends Carta{
    private int strength;
    private int resistence;
    private String type;
    private boolean flying;

    public Criatura() {
        super();
    }

    public Criatura(String nom, String descripcio, Rareza rareza, String edicion, CosteMana coste,int strength, int resistence, String type, boolean flying) {
        super(nom, descripcio, rareza, edicion, coste);
        this.strength = strength;
        this.resistence = resistence;
        this.type = type;
        this.flying = flying;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getResistence() {
        return resistence;
    }

    public void setResistence(int resistence) {
        this.resistence = resistence;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isFlying() {
        return flying;
    }

    public void setFlying(boolean flying) {
        this.flying = flying;
    }
      @Override
    public String toString() {
        return "Criatura [" + super.getNom() + "] - Tipus: " + type + " (Voladora: " + flying + ")";
    }
    
}
