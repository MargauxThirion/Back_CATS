package com.back_cats.controllers;

import com.back_cats.models.Signalement;
import com.back_cats.services.SignalementService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/signalement")
public class SignalementController {


    @Autowired
    private SignalementService signalementService;

    @PostMapping
    public Signalement saveSignalement(@RequestBody Signalement signalement){
        return signalementService.saveSignalement(signalement);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Signalement> getSignalement(@PathVariable ObjectId id){
        Signalement signalement = signalementService.getSignalement(id);
        if (signalement != null) {
            return ResponseEntity.ok(signalement);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Signalement> updateSignalement(@PathVariable ObjectId id, @RequestBody Signalement signalement){
        Signalement updatedSignalement = signalementService.updateSignalement(id, signalement);
        if (updatedSignalement != null) {
            return ResponseEntity.ok(updatedSignalement);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Iterable<Signalement>> getSignalements() {
        Iterable<Signalement> signalements = signalementService.getSignalements();
        return ResponseEntity.ok(signalements);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSignalement(@PathVariable ObjectId id) {
        signalementService.deleteSignalement(id);
        return ResponseEntity.ok("Signalement supprimé avec succès.");
    }

    @PutMapping("/{signalementId}/close")
    public ResponseEntity<Signalement> closeSignalement(@PathVariable String signalementId) {
        try {
            Signalement updatedSignalement = signalementService.closeSignalement(signalementId);
            return ResponseEntity.ok(updatedSignalement);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/en-attente")
    public ResponseEntity<List<Signalement>> getSignalementsEnAttente() {
        List<Signalement> signalements = signalementService.getSignalementsByEtat("En attente");
        return ResponseEntity.ok(signalements);
    }

    @GetMapping("/carte/{carteId}/en-attente")
    public ResponseEntity<List<Signalement>> getSignalementsEnAttenteByCarte(@PathVariable ObjectId carteId) {
        List<Signalement> signalements = signalementService.getSignalementsByEtatAndCarteId("En attente", carteId);
        return ResponseEntity.ok(signalements);
    }


    @GetMapping("/ferme")
    public ResponseEntity<List<Signalement>> getSignalementsFermes() {
        List<Signalement> signalements = signalementService.getSignalementsByEtat("Fermé");
        return ResponseEntity.ok(signalements);
    }


}
