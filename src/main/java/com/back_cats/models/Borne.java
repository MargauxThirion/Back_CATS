package com.back_cats.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "borne")
public class Borne {
    @Id
    @JsonProperty("_id")
    private ObjectId id;
    private String status = "Fonctionnelle";
    private Integer coord_x;
    private Integer coord_y;
    private Integer numero;
    @DBRef
    private TypeBorne typeBorne;
    @DBRef
    private Carte carte;

    public String getId() {
        return id.toHexString();
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCoord_x() {
        return coord_x;
    }

    public void setCoord_x(Integer coord_x) {
        this.coord_x = coord_x;
    }

    public Integer getCoord_y() {
        return coord_y;
    }

    public void setCoord_y(Integer coord_y) {
        this.coord_y = coord_y;
    }

    public TypeBorne getTypeBorne() {
        return typeBorne;
    }

    public void setTypeBorne(TypeBorne typeBorne) {
        this.typeBorne = typeBorne;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Carte getCarte() {
        return carte;
    }

    public void setCarte(Carte carte) {
        this.carte = carte;
    }
}
