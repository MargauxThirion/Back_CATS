package com.back_cats.controllers;

import com.back_cats.models.Reservation;
import com.back_cats.services.ReservationService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody Reservation reservation) {
        try {
            Reservation savedReservation = reservationService.saveReservation(reservation);
            return ResponseEntity.ok(savedReservation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable ObjectId id) {
        Reservation reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable ObjectId id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.ok("Reservation supprimée avec succès.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable ObjectId id, @RequestBody Reservation reservation) {
        Reservation updatedReservation = reservationService.updateReservation(id, reservation);
        return ResponseEntity.ok(updatedReservation);
    }

    @GetMapping("/occupied-bornes")
    public Set<String> getOccupiedBornes(
            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") Date startDate,
            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") Date endDate) {
        return reservationService.getOccupiedBornesIds(startDate, endDate);
    }
}
