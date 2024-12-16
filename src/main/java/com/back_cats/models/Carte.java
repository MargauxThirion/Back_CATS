package com.back_cats.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "carte")
public class Carte {
    @Id
    private ObjectId id;
    private String carte; // Chemin vers la photo jpeg
    private String nom;

    @DBRef
    private List<Borne> bornes; // Liste des bornes associées

    public String getId() {
        return id.toHexString();
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getCarte() {
        return carte;
    }

    public void setCarte(String carte) {
        this.carte = carte;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Borne> getBornes() {
        return bornes;
    }

    public void setBornes(List<Borne> bornes) {
        this.bornes = bornes;
    }
}
