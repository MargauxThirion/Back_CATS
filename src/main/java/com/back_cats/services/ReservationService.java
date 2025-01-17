package com.back_cats.services;

import com.back_cats.exceptions.ReservationException;
import com.back_cats.models.Borne;
import com.back_cats.models.Reservation;
import com.back_cats.models.User;
import com.back_cats.repositories.BorneRepository;
import com.back_cats.repositories.ReservationRepository;
import com.back_cats.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private BorneRepository borneRepository;

    @Autowired
    private UserRepository userRepository;

    public Reservation saveReservation(Reservation reservation) throws ReservationException {
        ObjectId userId = new ObjectId(reservation.getUser().getId());
        ObjectId borneId = new ObjectId(reservation.getBorne().getId());

        // Vérification des réservations chevauchantes pour la borne
        List<Reservation> overlappingReservations = reservationRepository.findOverlappingReservations(borneId, reservation.getDateDebut(), reservation.getDateFin());
        if (!overlappingReservations.isEmpty()) {
            throw new ReservationException("Cette horaire est déjà réservé pour cette borne.");
        }

        // Vérification des réservations chevauchantes pour l'utilisateur
        overlappingReservations = reservationRepository.findUserOverlappingReservations(userId, reservation.getDateDebut(), reservation.getDateFin());
        if (!overlappingReservations.isEmpty()) {
            throw new ReservationException("L'utilisateur a déjà une réservation pour cette horaire.");
        }

        // Récupération et validation de la borne et de l'utilisateur
        Borne borne = borneRepository.findById(borneId).orElseThrow(() -> new RuntimeException("Borne not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        reservation.setBorne(borne);
        reservation.setUser(user);

        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(ObjectId id) {
        return reservationRepository.findById(id).orElseThrow(() -> new RuntimeException("Reservation not found"));
    }

    public void deleteReservation(ObjectId id) {
        reservationRepository.deleteById(id);
    }

    public Reservation updateReservation (ObjectId id, Reservation reservation) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (reservation.getDateDebut() != null) {
            existingReservation.setDateDebut(reservation.getDateDebut());
        }
        if (reservation.getDateFin() != null) {
            existingReservation.setDateFin(reservation.getDateFin());
        }
        if (reservation.getBorne() != null) {
            existingReservation.setBorne(reservation.getBorne());
        }
        if (reservation.getUser() != null) {
            existingReservation.setUser(reservation.getUser());
        }

        return reservationRepository.save(existingReservation);
    }

    public List<Reservation> getActiveReservationsForDate(Date now, Date inThreeHours) {
        List<Reservation> activeReservations = reservationRepository.findReservationsThatOverlap(now, inThreeHours);
        return activeReservations;
    }

    public Set<String> getOccupiedBornesIds(Date startDate, Date endDate) {
        List<Reservation> overlappingReservations = reservationRepository.findReservationsThatOverlap(startDate, endDate);
        Set<String> occupiedBornes = new HashSet<>();
        for (Reservation reservation : overlappingReservations) {
            occupiedBornes.add(reservation.getBorne().getId());
        }
        return occupiedBornes;
    }
    public List<Reservation> getReservationsByUser(String userId) {
        ObjectId userObjectId = new ObjectId(userId);
        return reservationRepository.findByUserId(userObjectId);
    }


}
