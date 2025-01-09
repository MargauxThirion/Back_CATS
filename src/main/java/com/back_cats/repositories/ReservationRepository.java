package com.back_cats.repositories;

import com.back_cats.models.Reservation;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, ObjectId> {

    @Query("{ $or: [ { dateDebut: { $lte: ?1 } , dateFin: { $gte: ?0 } }, { dateDebut: { $lte: ?0 } , dateFin: { $gte: ?0 } }, { dateDebut: { $lte: ?1 } , dateFin: { $gte: ?1 } } ] }")
    List<Reservation> findReservationsThatOverlap(Date start, Date end);

    @Query("{ 'borne': ?0, $or: [ { 'dateDebut': { $lt: ?2 }, 'dateFin': { $gt: ?1 } }, { 'dateDebut': { $lte: ?1 }, 'dateFin': { $gte: ?2 } } ] }")
    List<Reservation> findOverlappingReservations(ObjectId borneId, Date startDate, Date endDate);




}
