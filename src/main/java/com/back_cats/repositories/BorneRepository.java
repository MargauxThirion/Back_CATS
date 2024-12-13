package com.back_cats.repositories;

import com.back_cats.models.Borne;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorneRepository extends MongoRepository<Borne, ObjectId> {
}
