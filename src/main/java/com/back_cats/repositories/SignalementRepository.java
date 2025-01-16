package com.back_cats.repositories;

import com.back_cats.models.Signalement;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SignalementRepository extends MongoRepository<Signalement, ObjectId> {
    List<Signalement> findByEtat(String etat);

    List<Signalement> findByIdborneIn(List<ObjectId> borneIds);

}
