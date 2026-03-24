package com.mycompany.magic.objdb_the_poly_deck_engine.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.mycompany.magic.objdb_the_poly_deck_engine.model.enums.Raresa;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) 
public abstract class Carta {

    // Variables de la clase
    @Id 
    @GeneratedValue
    private Long id;

    private String nom; 
    
    @Column(length = 1000) 
    private String descripcio;

    @Enumerated(EnumType.STRING)
    private Raresa raresa; 

    private String edicio; 

    @Embedded
    private CostMana cost; 

    // Constructors de la clase
    public Carta() {}

    public Carta(String nom, String descripcio, Raresa raresa, String edicio, CostMana coste) {
        this.nom = nom;
        this.descripcio = descripcio;
        this.raresa = raresa;
        this.edicio = edicio;
        this.cost = coste;
    }

    // Getters y Setters
    public Long getId() { return id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescripcio() { return descripcio; }
    public void setDescripcio(String descripcio) { this.descripcio = descripcio; }

    public Raresa getRaresa() { return raresa; }
    public void setRaresa(Raresa raresa) { this.raresa = raresa; }

    public String getEdicio() { return edicio; }
    public void setEdicio(String edicio) { this.edicio = edicio; }

    public CostMana getCoste() { return cost; }
    public void setCoste(CostMana coste) { this.cost = coste; }

    // toString de la carta
    @Override
    public String toString() {
        String s = "";
        s += "╔══════════════════════════════════════╗\n"
        + "║  " + String.format("%-36s", nom) + "║\n"
        + "╠══════════════════════════════════════╣\n"
        + "║  Id     : " + String.format("%-27s", (id == null ? "" : id.toString())) + "║\n"
        + "║  Edició : " + String.format("%-27s", edicio) + "║\n"
        + "║  Raresa : " + String.format("%-27s", raresa) + "║\n"
        + "║  Cost   : " + String.format("%-27s", cost) + "║\n"
        + "╠══════════════════════════════════════╣\n";

        if (descripcio != null && !descripcio.isEmpty()) {
            String[] lines = descripcio.split(" ");
            String line = "";
            for (String word : lines) {
                if (line.length() + word.length() + 1 > 36) {
                    s += "║  " + String.format("%-36s", line) + "║\n";
                    line = word;
                } else {
                    if (line.length() > 0) line += " ";
                    line += word;
                }
            }
            if (line.length() > 0) {
                s += "║  " + String.format("%-36s", line) + "║\n";
            }
        } else {
            s += "║  " + String.format("%-36s", "") + "║\n";
        }

        s += "╚══════════════════════════════════════╝";
        return s;
    }
}
