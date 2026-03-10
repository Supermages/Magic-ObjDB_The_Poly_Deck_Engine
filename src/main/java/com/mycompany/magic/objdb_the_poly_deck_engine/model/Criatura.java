package com.mycompany.magic.objdb_the_poly_deck_engine.model;
import javax.persistence.Entity;

import com.mycompany.magic.objdb_the_poly_deck_engine.model.enums.Rareza;

@Entity
public class Criatura extends Carta{
    private int forca;
    private int resistencia;
    private String tipusCriatura;
    private boolean vola;

    public Criatura() {
        super();
    }

    public Criatura(String nom, String descripcio, Rareza rareza, String edicion, CosteMana coste,int forca, int resistencia, String tipusCriatura, boolean vola) {
        super(nom, descripcio, rareza, edicion, coste);
        this.forca = forca;
        this.resistencia = resistencia;
        this.tipusCriatura = tipusCriatura;
        this.vola = vola;
    }

    public int getForca() {
        return forca;
    }

    public void setForca(int forca) {
        this.forca = forca;
    }

    public int getResistencia() {
        return resistencia;
    }

    public void setResistencia(int resistencia) {
        this.resistencia = resistencia;
    }

    public String getTipusCriatura() {
        return tipusCriatura;
    }

    public void setTipusCriatura(String tipusCriatura) {
        this.tipusCriatura = tipusCriatura;
    }

    public boolean isVola() {
        return vola;
    }

    public void setVola(boolean vola) {
        this.vola = vola;
    }
    @Override
    public String toString() {
        String base = super.toString();
        // Remove the last closing line and append creature-specific lines
        base = base.substring(0, base.lastIndexOf("╚══════════════════════════════════════╝"));
        return base +
               "╠══════════════════════════════════════╣\n" +
               "║  [CRIATURA] " + String.format("%-25s", tipusCriatura) + "║\n" +
               "║  Força/Resistència : " + String.format("%-16s", forca + " / " + resistencia) + "║\n" +
               "║  Vola  : " + String.format("%-28s", vola ? "Sí" : "No") + "║\n" +
               "╚══════════════════════════════════════╝";
    }
    
}
