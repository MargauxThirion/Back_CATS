package com.back_cats.services;

import com.back_cats.models.Borne;
import com.back_cats.models.Reservation;
import com.back_cats.models.User;
import com.back_cats.repositories.BorneRepository;
import com.back_cats.repositories.ReservationRepository;
import com.back_cats.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private BorneRepository borneRepository;

    @Autowired
    private UserRepository userRepository;

    public Reservation saveReservation(Reservation reservation) {
        System.out.println("Reservation: " + reservation.getDateDebut() + " " + reservation.getDateFin() + " " + reservation.getBorne() + " " + reservation.getUser());
        if (reservation.getBorne() != null && reservation.getBorne().getId() != null) {
            ObjectId borneId = new ObjectId(reservation.getBorne().getId());
            System.out.println("Borne ID: " + borneId);
            Borne borne = borneRepository.findById(borneId)
                    .orElseThrow(() -> new RuntimeException("Borne not found"));
            reservation.setBorne(borne);
        }

        if (reservation.getUser() != null && reservation.getUser().getId() != null) {
            ObjectId userId = new ObjectId(reservation.getUser().getId());
            System.out.println("User ID: " + userId);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            reservation.setUser(user);
        }

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
}
