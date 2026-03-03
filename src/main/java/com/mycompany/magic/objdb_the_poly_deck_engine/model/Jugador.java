package com.mycompany.magic.objdb_the_poly_deck_engine.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
    
@Entity
public class Jugador {

    @Id
    @GeneratedValue
    private Long id; 
    private String nick; 
    private int nivell; 
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) 
    private List<Mazo> mazos = new ArrayList<>();
    @ManyToMany 
    private List<Carta> colleccio = new ArrayList<>(); 

    public Jugador() {}

    public Jugador(String nick, int nivell) {
        this.nick = nick;
        this.nivell = nivell;
    }

    // Getters y Setters
    public Long getId() { return id; }

    public String getNick() { return nick; }
    public void setNick(String nick) { this.nick = nick; }

    public int getNivell() { return nivell; }
    public void setNivell(int nivell) { this.nivell = nivell; }

    public List<Mazo> getMazos() { return mazos; }
    public void setMazos(List<Mazo> mazos) { this.mazos = mazos; }

    public List<Carta> getColleccio() { return colleccio; }
    public void setColleccio(List<Carta> colleccio) { this.colleccio = colleccio; }

    public void addMazo(Mazo mazo) {
        this.mazos.add(mazo);
    }

    public void addCartaAColleccio(Carta carta) {
        this.colleccio.add(carta);
    }
}
