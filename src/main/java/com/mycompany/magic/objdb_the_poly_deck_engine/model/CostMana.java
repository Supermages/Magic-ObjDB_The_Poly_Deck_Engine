package com.mycompany.magic.objdb_the_poly_deck_engine.model;

import javax.persistence.Embeddable;

@Embeddable
public class CostMana {
    
    // Los colores del maná según el documento de diseño
    private int blanc;   
    private int blau;    
    private int negre;   
    private int vermell; 
    private int verd;  
    private int incolor; 

    // Constructor vacío obligatorio para JPA
    public CostMana() {}

    public CostMana(int blanc, int blau, int negre, int vermell, int verd, int incolor) {
        this.blanc = blanc;
        this.blau = blau;
        this.negre = negre;
        this.vermell = vermell;
        this.verd = verd;
        this.incolor = incolor;
    }

    // Getters y Setters
    public int getBlanc() { return blanc; }
    public void setBlanc(int blanc) { this.blanc = blanc; }

    public int getBlau() { return blau; }
    public void setBlau(int blau) { this.blau = blau; }

    public int getNegre() { return negre; }
    public void setNegre(int negre) { this.negre = negre; }

    public int getVermell() { return vermell; }
    public void setVermell(int vermell) { this.vermell = vermell; }

    public int getVerd() { return verd; }
    public void setVerd(int verd) { this.verd = verd; }

    public int getIncolor() { return incolor; }
    public void setIncolor(int incolor) { this.incolor = incolor; }
    
    @Override
    public String toString() {
        return "{" + blau + "U, " + negre + "B, " + vermell + "R, " + verd + "G, " + blanc + "W, " + incolor + "C}";
    }
}
