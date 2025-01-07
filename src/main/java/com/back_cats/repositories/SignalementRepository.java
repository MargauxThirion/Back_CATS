package com.back_cats.repositories;

import com.back_cats.models.Signalement;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignalementRepository extends MongoRepository<Signalement, ObjectId> {
}
