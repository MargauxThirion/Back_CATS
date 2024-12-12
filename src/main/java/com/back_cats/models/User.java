package com.back_cats.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

import java.util.List;

@Document(collection = "user")
public class User {
    @Id
    private ObjectId id;
    private String mail;
    private String mot_de_passe;
    private String role;
    private List<ObjectId> voitures;

    // Getters et Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMotDePasse() {
        return mot_de_passe;
    }

    public void setMotDePasse(String motDePasse) {
        this.mot_de_passe = motDePasse;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<ObjectId> getVoitures() {
        return voitures;
    }

    public void setVoitures(List<ObjectId> voitures) {
        this.voitures = voitures;
    }
}