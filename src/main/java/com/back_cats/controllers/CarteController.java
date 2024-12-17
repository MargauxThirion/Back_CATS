package com.back_cats.controllers;

import com.back_cats.models.Carte;
import com.back_cats.services.CarteService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/carte")
public class CarteController {

    @Autowired
    private CarteService carteService;

    @GetMapping
    public ResponseEntity<List<Carte>> getAllCartes() {
        List<Carte> cartes = carteService.getAllCartes();
        return ResponseEntity.ok(cartes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carte> getCarteById(@PathVariable String id) {
        Carte carte = carteService.getCarteById(id);
        return ResponseEntity.ok(carte);
    }

    @PostMapping
    public ResponseEntity<?> createCarte(@RequestBody Carte carte) {
        if (carteService.getNom(carte.getNom()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Une carte du même nom existe déjà");
        }
        Carte newCarte = carteService.createCarte(carte);
        if (newCarte != null) {
            return ResponseEntity.ok(newCarte);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Impossible de créer la carte");
    }

    @PatchMapping("photo/{id}")
    public ResponseEntity<String> addPhotoToCarte(
            @PathVariable String id, // Assurez-vous que le type correspond (String ou ObjectId)
            @RequestParam("photo") MultipartFile photo) {
        try {
            carteService.addPhotoToCarte(new ObjectId(id), photo); // Convertir en ObjectId si nécessaire
            return ResponseEntity.status(HttpStatus.CREATED).body("Photo ajoutée avec succès");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'ajout de la photo: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



    @PutMapping("/{id}")
    public ResponseEntity<Carte> updateCarte(@PathVariable String id, @RequestBody Carte carte) {
        getCarteById(id); // Ensure the carte exists before update
        carte.setId(new ObjectId(id));
        Carte updatedCarte = carteService.saveCarte(carte);
        return ResponseEntity.ok(updatedCarte);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarte(@PathVariable String id) {
        carteService.deleteCarte(id);
        return ResponseEntity.ok().build();
    }
}
