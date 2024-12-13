package com.back_cats.controllers;

import com.back_cats.exceptions.BorneException;
import com.back_cats.models.Borne;
import com.back_cats.services.BorneService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/borne")
public class BorneController {
    @Autowired
    private BorneService borneService;

    @GetMapping
    public List<Borne> getAllBornes() {
        return borneService.findAllBornes();
    }

    @PostMapping
    public Borne createBorne(@RequestBody Borne borne) {
        return borneService.saveBorne(borne);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Borne> getBorneById(@PathVariable ObjectId id) {
        try {
            Borne borne = borneService.findBorneById(id);
            return ResponseEntity.ok(borne);
        } catch (BorneException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Borne> updateBorne(@PathVariable ObjectId id, @RequestBody Borne borneDetails) {
        try {
            Borne updatedBorne = borneService.updateBorne(id, borneDetails);
            return ResponseEntity.ok(updatedBorne);
        } catch (BorneException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBorne(@PathVariable ObjectId id) {
        try {
            borneService.deleteBorne(id);
            return ResponseEntity.ok().build();
        } catch (BorneException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
