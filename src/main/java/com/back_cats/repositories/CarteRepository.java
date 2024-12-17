package com.back_cats.repositories;

import com.back_cats.models.Carte;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CarteRepository extends MongoRepository<Carte, ObjectId> {
    Carte findByNom(String nom);
}
