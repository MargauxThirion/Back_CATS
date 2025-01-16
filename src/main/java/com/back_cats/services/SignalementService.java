package com.back_cats.services;

import com.back_cats.models.Borne;
import com.back_cats.models.Carte;
import com.back_cats.models.Signalement;
import com.back_cats.models.User;
import com.back_cats.repositories.BorneRepository;
import com.back_cats.repositories.CarteRepository;
import com.back_cats.repositories.SignalementRepository;
import com.back_cats.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SignalementService {

    @Autowired
    private SignalementRepository signalementRepository;

    @Autowired
    private BorneRepository borneRepository;

    @Autowired
    private CarteRepository carteRepository;

    @Autowired
    private UserRepository userRepository;

    public Signalement saveSignalement(Signalement signalement){
        if(signalement.getBorne() != null && signalement.getBorne().getId() != null){
            ObjectId borneId = new ObjectId(signalement.getBorne().getId());
            System.out.println("Borne ID: " + borneId);
            Borne borne = borneRepository.findById(borneId)
                    .orElseThrow(() -> new RuntimeException("Borne not found"));
            signalement.setBorne(borne);

            borne.setStatus("Signalée");
            borneRepository.save(borne);
        }

        if(signalement.getUser() != null && signalement.getUser().getId() != null){
            ObjectId userId = new ObjectId(signalement.getUser().getId());
            System.out.println("User ID: " + userId);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            signalement.setUser(user);
        }
        signalement.setEtat("En attente");
        signalement.setDate(new Date());


        return signalementRepository.save(signalement);
    }

    public Signalement getSignalement(ObjectId id){
        return signalementRepository.findById(id)
                .orElse(null);
    }

    public Signalement updateSignalement(ObjectId id, Signalement signalement){
        // Recherche de l'instance existante de Signalement
        Signalement existingSignalement = signalementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Signalement not found"));

        // Mise à jour du motif si présent dans la requête
        if (signalement.getMotif() != null) {
            existingSignalement.setMotif(signalement.getMotif());
        }

        // Mise à jour de la date si présente dans la requête
        if (signalement.getDate() != null) {
            existingSignalement.setDate(signalement.getDate());
        }
        if (signalement.getEtat() != null) {
            existingSignalement.setEtat(signalement.getEtat());
        }

        // Mise à jour de la borne associée si présente dans la requête
        if (signalement.getBorne() != null && signalement.getBorne().getId() != null) {
            ObjectId borneId = new ObjectId(signalement.getBorne().getId());
            Borne borne = borneRepository.findById(borneId)
                    .orElseThrow(() -> new RuntimeException("Borne not found"));
            existingSignalement.setBorne(borne);
        }

        // Mise à jour de l'utilisateur associé si présent dans la requête
        if (signalement.getUser() != null && signalement.getUser().getId() != null) {
            ObjectId userId = new ObjectId(signalement.getUser().getId());
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            existingSignalement.setUser(user);
        }

        // Sauvegarde de l'instance mise à jour de Signalement
        return signalementRepository.save(existingSignalement);
    }


    public Iterable<Signalement> getSignalements() {
        return signalementRepository.findAll();
    }

    public void deleteSignalement(ObjectId id) {
        signalementRepository.deleteById(id);
    }

    public Signalement closeSignalement(String signalementId) {
        ObjectId id = new ObjectId(signalementId);
        Signalement signalement = signalementRepository.findById(id).orElseThrow(() -> new RuntimeException("Signalement not found"));
        signalement.setEtat("Fermé");
        return signalementRepository.save(signalement);
    }

    public List<Signalement> getSignalementsByEtat(String etat) {
        return signalementRepository.findByEtat(etat);
    }

    public List<Signalement> getSignalementsByEtatAndCarteId(String etat, ObjectId carteId) {

        List<Borne> bornes = borneRepository.findByCarteId(carteId);

        List<ObjectId> borneIds = bornes.stream()
                .map(Borne::getId)  // Récupère les IDs comme String
                .map(ObjectId::new) // Convertit les Strings en ObjectId
                .collect(Collectors.toList());

        List<Signalement> signalements = signalementRepository.findByIdborneIn(borneIds);
        return signalements.stream()
                .filter(signalement -> signalement.getEtat().equals(etat))
                .collect(Collectors.toList());
    }


}

