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

import com.mycompany.magic.objdb_the_poly_deck_engine.model.enums.Rareza;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) 
public abstract class Carta {

    @Id 
    @GeneratedValue
    private Long id;

    private String nom; 
    
    @Column(length = 1000) 
    private String descripcio;

    @Enumerated(EnumType.STRING)
    private Rareza rareza; 

    private String edicion; 

    @Embedded
    private CosteMana coste; 

    public Carta() {}

    public Carta(String nom, String descripcio, Rareza rareza, String edicion, CosteMana coste) {
        this.nom = nom;
        this.descripcio = descripcio;
        this.rareza = rareza;
        this.edicion = edicion;
        this.coste = coste;
    }

    // Getters y Setters
    public Long getId() { return id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescripcio() { return descripcio; }
    public void setDescripcio(String descripcio) { this.descripcio = descripcio; }

    public Rareza getRareza() { return rareza; }
    public void setRareza(Rareza rareza) { this.rareza = rareza; }

    public String getEdicion() { return edicion; }
    public void setEdicion(String edicion) { this.edicion = edicion; }

    public CosteMana getCoste() { return coste; }
    public void setCoste(CosteMana coste) { this.coste = coste; }

    @Override
    public String toString() {
        String s = "";
        s += "╔══════════════════════════════════════╗\n"
        + "║  " + String.format("%-36s", nom) + "║\n"
        + "╠══════════════════════════════════════╣\n"
        + "║  Edició : " + String.format("%-27s", edicion) + "║\n"
        + "║  Raresa : " + String.format("%-27s", rareza) + "║\n"
        + "║  Cost   : " + String.format("%-27s", coste) + "║\n"
        + "╠══════════════════════════════════════╣\n";

        if (descripcio != null && !descripcio.isEmpty()) {
            String[] lines = descripcio.split("(?<=\\G.{36})");
            for (String line : lines) {
                s += "║  " + String.format("%-36s", line) + "║\n";
            }
        } else {
            s += "║  " + String.format("%-36s", "") + "║\n";
        }

        s += "╚══════════════════════════════════════╝";
        return s;
    }
}
