package com.back_cats.controllers;

import com.back_cats.models.Borne;
import com.back_cats.models.Reservation;
import com.back_cats.services.BorneService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/borne")
public class BorneController {

    @Autowired
    private BorneService borneService;

    @GetMapping
    public List<Borne> getAllBornes() {
        return borneService.getAllBornes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Borne> getBorneById(@PathVariable String id) {
        Optional<Borne> borne = borneService.getBorneById(new ObjectId(id));
        return borne.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Borne> createBorne(@RequestBody Borne borne) {
        Borne newBorne = borneService.saveBorne(borne);
        return ResponseEntity.ok(newBorne);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Borne> updateBorne (@PathVariable ObjectId id, @RequestBody Borne borne){
        Borne updatedBorne = borneService.updateBorne(id, borne);
        return ResponseEntity.ok(updatedBorne);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Borne> patchBorne(@PathVariable String id, @RequestBody Borne newBorne) {
        return borneService.getBorneById(new ObjectId(id))
                .map(existingBorne -> {
                    if (newBorne.getStatus() != null) {
                        existingBorne.setStatus(newBorne.getStatus());
                    }
                    if (newBorne.getCoord_x() != null) {
                        existingBorne.setCoord_x(newBorne.getCoord_x());
                    }
                    if (newBorne.getCoord_y() != null) {
                        existingBorne.setCoord_y(newBorne.getCoord_y());
                    }
                    if (newBorne.getTypeBorne() != null) {
                        existingBorne.setTypeBorne(newBorne.getTypeBorne());
                    }
                    return ResponseEntity.ok(borneService.saveBorne(existingBorne));
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBorne(@PathVariable String id) {
        Optional<Borne> borne = borneService.getBorneById(new ObjectId(id));
        if (borne.isPresent()) {
            borneService.deleteBorne(new ObjectId(id));
            return ResponseEntity.ok("Borne supprimée avec succès.");
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/etat")
    public ResponseEntity<Map<String, List<Borne>>> getBornesStatus() {
        Date instant = new Date(); // Utilise la date et l'heure actuelles
        Map<String, List<Borne>> statusMap = borneService.getBornesStatus();
        return ResponseEntity.ok(statusMap);
    }
    @GetMapping("/etat-velo")
    public ResponseEntity<Map<String, List<Borne>>> getBornesVeloStatus() {
        Date instant = new Date(); // Utilise la date et l'heure actuelles
        Map<String, List<Borne>> statusMap = borneService.getBornesVeloStatus();
        return ResponseEntity.ok(statusMap);
    }

    @GetMapping("/etat-date")
    public ResponseEntity<Map<String, List<Borne>>> getBornesStatus(
            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") Date start,
            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") Date end) {
        try {
            Map<String, List<Borne>> statusMap = borneService.getBornesStatusByDate(start, end);
            return ResponseEntity.ok(statusMap);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(null); // Handle parse exceptions here
        }
    }

    @GetMapping("/etat-velo-date")
    public ResponseEntity<Map<String, List<Borne>>> getBorneVelosStatus(
            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") Date start,
            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") Date end) {
        try {
            Map<String, List<Borne>> statusMap = borneService.getBornesVeloStatusByDate(start, end);
            return ResponseEntity.ok(statusMap);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(null); // Handle parse exceptions here
        }
    }

    @GetMapping("/status")
    public ResponseEntity<List<Borne>> getBornesByStatus(@RequestParam String status) {
        List<Borne> bornes = borneService.getBornesByStatus(status);
        if (bornes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(bornes, HttpStatus.OK);
    }






}
