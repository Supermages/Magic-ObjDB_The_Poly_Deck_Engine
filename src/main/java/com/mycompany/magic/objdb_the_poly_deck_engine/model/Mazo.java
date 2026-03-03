package com.mycompany.magic.objdb_the_poly_deck_engine.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Mazo {

    @Id
    @GeneratedValue
    private Long id; 
    private String nom; 
    private LocalDate dataCreacio;
    @ManyToMany(cascade = CascadeType.PERSIST) 
    private List<Carta> cartes = new ArrayList<>();

    public Mazo() {}

    public Mazo(String nom, LocalDate dataCreacio) {
        this.nom = nom;
        this.dataCreacio = dataCreacio;
    }

    public Long getId() { return id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public LocalDate getDataCreacio() { return dataCreacio; }
    public void setDataCreacio(LocalDate dataCreacio) { this.dataCreacio = dataCreacio; }

    public List<Carta> getCartes() { return cartes; }
    public void setCartes(List<Carta> cartes) { this.cartes = cartes; }

    public void addCarta(Carta carta) {
        this.cartes.add(carta);
    }
    
    public void removeCarta(Carta carta) {
        this.cartes.remove(carta);
    }
    
    @Override
    public String toString() {
        return "Mazo: " + nom + " (Cartas: " + cartes.size() + ")";
    }
}