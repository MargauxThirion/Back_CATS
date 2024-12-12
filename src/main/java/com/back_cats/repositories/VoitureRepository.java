package com.back_cats.repositories;

import com.back_cats.models.Voiture;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoitureRepository extends MongoRepository<Voiture, ObjectId> {

}
