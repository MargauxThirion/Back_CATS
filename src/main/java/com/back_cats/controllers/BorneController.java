package com.back_cats.controllers;

import com.back_cats.models.Borne;
import com.back_cats.services.BorneService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<Borne> updateBorne(@PathVariable String id, @RequestBody Borne newBorne) {
        return borneService.getBorneById(new ObjectId(id))
                .map(borne -> {
                    borne.setStatus(newBorne.getStatus());
                    borne.setCoord_x(newBorne.getCoord_x());
                    borne.setCoord_y(newBorne.getCoord_y());
                    borne.setTypeBorne(newBorne.getTypeBorne());
                    return ResponseEntity.ok(borneService.saveBorne(borne));
                }).orElseGet(() -> ResponseEntity.notFound().build());
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
    public ResponseEntity<Void> deleteBorne(@PathVariable String id) {
        Optional<Borne> borne = borneService.getBorneById(new ObjectId(id));
        if (borne.isPresent()) {
            borneService.deleteBorne(new ObjectId(id));
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
