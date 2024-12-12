package com.back_cats.repositories;

import com.back_cats.models.Voiture;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VoitureRepository extends MongoRepository<Voiture, ObjectId> {

}
