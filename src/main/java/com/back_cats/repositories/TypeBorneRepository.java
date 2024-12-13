package com.back_cats.repositories;

import com.back_cats.models.TypeBorne;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeBorneRepository extends MongoRepository<TypeBorne, ObjectId> {
}
