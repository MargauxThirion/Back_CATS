package com.back_cats.controllers;

import com.back_cats.models.Carte;
import com.back_cats.services.CarteService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Carte> createCarte(@RequestPart("carte") Carte carte,
                                             @RequestPart("file") MultipartFile file) {
        try {
            Carte newCarte = carteService.createCarte(carte, file);
            return ResponseEntity.ok(newCarte);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
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
