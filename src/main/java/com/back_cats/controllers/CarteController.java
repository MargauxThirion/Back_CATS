package com.back_cats.controllers;

import com.back_cats.exceptions.CarteException;
import com.back_cats.models.Carte;
import com.back_cats.services.CarteService;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/carte")
public class CarteController {

    @Autowired
    private CarteService carteService;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @GetMapping
    public ResponseEntity<List<Carte>> getAllCartes() {
        List<Carte> cartes = carteService.getAllCartes();
        return ResponseEntity.ok(cartes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carte> getCarteById(@PathVariable ObjectId id) {
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
    public ResponseEntity<Carte> updateCarte(@PathVariable ObjectId id, @RequestBody Carte carte) {
        getCarteById(id); // Ensure the carte exists before update
        carte.setId(id);
        Carte updatedCarte = carteService.saveCarte(carte);
        return ResponseEntity.ok(updatedCarte);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCarte(@PathVariable ObjectId id) {
        carteService.deleteCarte(id);
        return ResponseEntity.ok("Carte supprimée avec succès.");
    }


    @GetMapping("/photo/{id}")
    public ResponseEntity<Resource> getCartePhoto(@PathVariable ObjectId id) {
        try {
            Carte carte = carteService.getCarteById(id);
            if (carte == null || carte.getCarte() == null) {
                return ResponseEntity.notFound().build();
            }

            GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(new ObjectId(carte.getCarte()))));
            if (gridFSFile == null) {
                return ResponseEntity.notFound().build();
            }

            GridFsResource resource = gridFsTemplate.getResource(gridFSFile);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(resource.getContentType()))
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("{id}/lastModified")
    public ResponseEntity<LocalDateTime> getLastModifiedById(@PathVariable String id) {
        try {
            Carte carte = carteService.getCarteById(new ObjectId(id));
            return ResponseEntity.ok(carte.getLastModified());
        } catch (CarteException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
