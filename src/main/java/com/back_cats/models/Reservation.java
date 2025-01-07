package com.back_cats.models;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "reservation")
public class Reservation {

        @Id
        @JsonProperty("_id")
        private ObjectId id;
        @DBRef
        private Borne borne;
        @DBRef
        private User user;
        private Date dateDebut;
        private Date dateFin;

        // Getters et Setters
        public String getId() {
            return id.toHexString();
        }
        public void setId(ObjectId id) {
        this.id = id;
    }

        public Borne getBorne() {
            return borne;
        }

        public void setBorne(Borne borne) {
            this.borne = borne;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Date getDateDebut() {
            return dateDebut;
        }

        public void setDateDebut(Date dateDebut) {
            this.dateDebut = dateDebut;
        }

        public Date getDateFin() {
            return dateFin;
        }

        public void setDateFin(Date dateFin) {
            this.dateFin = dateFin;
        }

    }


