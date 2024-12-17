package com.back_cats.services;

import com.back_cats.exceptions.CarteException;
import com.back_cats.models.Borne;
import com.back_cats.models.Carte;
import com.back_cats.repositories.BorneRepository;
import com.back_cats.repositories.CarteRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
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


    public void addPhotoToCarte(ObjectId id, MultipartFile file) throws IOException {
        Optional<Carte> carteOpt = carteRepository.findById(id);
        if (carteOpt.isPresent() && !file.isEmpty()) {
            // Générer un nom unique pour le fichier
            String fileName = carteOpt.get().getNom() + "_" + UUID.randomUUID() + ".jpg";

            // Construire le chemin complet
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            System.out.println("Full path: " + filePath.toAbsolutePath());

            // Créer les répertoires si nécessaire
            Files.createDirectories(filePath.getParent());

            // Sauvegarder le fichier sur le disque
            Files.write(filePath, file.getBytes());

            // Mettre à jour le chemin de la carte dans l'objet Carte
            Carte carte = carteOpt.get();
            carte.setCarte(filePath.toString());
            carteRepository.save(carte); // Sauvegarde l'objet Carte mis à jour
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

    public Carte addBorneToCarte(ObjectId carteId, ObjectId borneId) {
        Carte carte = carteRepository.findById(carteId)
                .orElseThrow(() -> new IllegalArgumentException("Carte not found"));
        Borne borneToAdd = borneRepository.findById(borneId)
                .orElseThrow(() -> new IllegalArgumentException("Borne not found"));

        // Vérifiez si la borne est déjà dans la liste des bornes de la carte
        boolean alreadyExists = carte.getBornes().stream()
                .anyMatch(borne -> borne.getId().equals(borneToAdd.getId()));

        if (!alreadyExists) {
            carte.getBornes().add(borneToAdd);
            carteRepository.save(carte);
        } else {
            throw new IllegalStateException("La borne est déjà associée à cette carte");
        }

        return carte;
    }


}
