package com.back_cats.services;

import com.back_cats.exceptions.CarteException;
import com.back_cats.models.Borne;
import com.back_cats.models.Carte;
import com.back_cats.repositories.BorneRepository;
import com.back_cats.repositories.CarteRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CarteService {

    @Autowired
    private CarteRepository carteRepository;

    @Autowired
    private BorneRepository borneRepository;

    private static final String UPLOAD_DIR = "images"; // Répertoire local pour stocker les images

    @Autowired
    private GridFsTemplate gridFsTemplate;

    public void addPhotoToCarte(ObjectId id, MultipartFile file) throws IOException {
        Optional<Carte> carteOpt = carteRepository.findById(id);
        if (carteOpt.isPresent() && !file.isEmpty()) {
            // Enregistrer le fichier dans GridFS
            ObjectId fileId = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());

            // Sauvegarder l'ID du fichier dans la base de données Carte
            Carte carte = carteOpt.get();
            carte.setCarte(fileId.toHexString());
            carteRepository.save(carte);
        } else {
            throw new IllegalArgumentException("Carte not found or file is empty");
        }
    }

    public Carte createCarte(Carte carte) {
        return carteRepository.save(carte);
    }

    public List<Carte> getAllCartes() {
        return carteRepository.findAll();
    }

    public Carte getCarteById(ObjectId id) {
        return carteRepository.findById(id)
                .orElseThrow(() -> new CarteException("Carte not found with id: " + id));
    }



    public Carte saveCarte(Carte carte) {
        return carteRepository.save(carte);
    }

    public void deleteCarte(ObjectId id) {
        Carte carte = getCarteById(id); // This ensures the carte exists before attempting to delete.
        carteRepository.delete(carte);
    }

    public Carte getNom(String nom) {
        return carteRepository.findByNom(nom);
    }

}
