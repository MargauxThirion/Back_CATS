package com.back_cats.services;

import com.back_cats.exceptions.CarteException;
import com.back_cats.models.Carte;
import com.back_cats.repositories.CarteRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class CarteService {

    @Autowired
    private CarteRepository carteRepository;

    private static final String UPLOAD_DIR = "/app/images"; // Répertoire local pour stocker les images

    public Carte createCarte(Carte carte, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            // Générer un nom unique pour le fichier basé sur le nom du lieu
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            // Construire le chemin complet
            Path filePath = Paths.get(UPLOAD_DIR, fileName);

            // Créer les répertoires si nécessaire
            Files.createDirectories(filePath.getParent());

            // Sauvegarder le fichier sur le disque
            Files.write(filePath, file.getBytes());

            // Sauvegarder le chemin dans l'objet carte
            carte.setCarte(filePath.toString());
        }

        return carteRepository.save(carte);
    }

    public List<Carte> getAllCartes() {
        return carteRepository.findAll();
    }

    public Carte getCarteById(String id) {
        return carteRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new CarteException("Carte not found with id: " + id));
    }

    public Carte saveCarte(Carte carte) {
        return carteRepository.save(carte);
    }

    public void deleteCarte(String id) {
        Carte carte = getCarteById(id); // This ensures the carte exists before attempting to delete.
        carteRepository.delete(carte);
    }
}
