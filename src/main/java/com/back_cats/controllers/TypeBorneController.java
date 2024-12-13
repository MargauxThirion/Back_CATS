package com.back_cats.controllers;

import com.back_cats.exceptions.TypeBorneException;
import com.back_cats.models.TypeBorne;
import com.back_cats.services.TypeBorneService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/type_borne")
public class TypeBorneController {

    @Autowired
    private TypeBorneService typeBorneService;

    @GetMapping
    public List<TypeBorne> getAllTypeBornes() {
        return typeBorneService.findAllTypes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeBorne> getTypeBorneById(@PathVariable String id) {
        try {
            TypeBorne typeBorne = typeBorneService.findTypeBorneById(new ObjectId(id));
            return ResponseEntity.ok(typeBorne);
        } catch (TypeBorneException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public TypeBorne createTypeBorne(@RequestBody TypeBorne typeBorne) {
        return typeBorneService.saveTypeBorne(typeBorne);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TypeBorne> updateTypeBorne(@PathVariable String id, @RequestBody TypeBorne updatedTypeBorne) {
        try {
            TypeBorne typeBorne = typeBorneService.updateTypeBorne(new ObjectId(id), updatedTypeBorne);
            return ResponseEntity.ok(typeBorne);
        } catch (TypeBorneException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeBorne(@PathVariable String id) {
        try {
            typeBorneService.deleteTypeBorne(new ObjectId(id));
            return ResponseEntity.ok().build();
        } catch (TypeBorneException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
