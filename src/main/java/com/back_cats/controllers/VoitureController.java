package com.back_cats.controllers;

import com.back_cats.exceptions.VoitureException;
import com.back_cats.models.Voiture;
import com.back_cats.services.VoitureService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/voiture")
public class VoitureController {

    @Autowired
    private VoitureService voitureService;

    @GetMapping
    public List<Voiture> getAllVoitures() {
        return voitureService.findAllVoitures();
    }

    @PostMapping
    public Voiture createVoiture(@RequestBody Voiture voiture) {
        return voitureService.saveVoiture(voiture);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Voiture> getVoitureById(@PathVariable ObjectId id) {
        try {
            Voiture voiture = voitureService.findVoitureById(id);
            return ResponseEntity.ok(voiture);
        } catch (VoitureException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Voiture> updateVoiture(@PathVariable ObjectId id, @RequestBody Voiture voitureDetails) {
        try {
            Voiture updatedVoiture = voitureService.updateVoiture(id, voitureDetails);
            return ResponseEntity.ok(updatedVoiture);
        } catch (VoitureException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVoiture(@PathVariable ObjectId id) {
        try {
            voitureService.deleteVoiture(id);
            return ResponseEntity.ok("Voiture supprimée avec succès.");
        } catch (VoitureException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
