package com.back_cats.models;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "voiture")
public class Voiture {
    @Id
    @JsonProperty("_id")
    private ObjectId id;
    private String marque;
    private String modele;
    private Integer annee;
    private Integer conso;

    // Getters et Setters
    public String getId() {
        return id.toHexString();
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public Integer getConso() {
        return conso;
    }

    public void setConso(Integer conso) {
        this.conso = conso;
    }
}
