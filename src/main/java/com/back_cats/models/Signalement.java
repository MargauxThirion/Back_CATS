package com.back_cats.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "signalement")
public class Signalement {

    @Id
    @JsonProperty("_id")
    private ObjectId id;

    @DBRef
    private Borne idborne;

    @DBRef
    private User iduser;

    private String motif;
    private Date date;
    private String etat = "En attente";

    public Borne getBorne() {
        return idborne;
    }

    public String getId() {
        return id.toHexString();
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setBorne(Borne idborne) {
        this.idborne = idborne;
    }

    public User getUser() {
        return iduser;
    }

    public void setUser(User iduser) {
        this.iduser = iduser;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }
}
