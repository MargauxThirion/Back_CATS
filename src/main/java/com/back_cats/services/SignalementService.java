package com.back_cats.services;

import com.back_cats.models.Borne;
import com.back_cats.models.Signalement;
import com.back_cats.models.User;
import com.back_cats.repositories.BorneRepository;
import com.back_cats.repositories.SignalementRepository;
import com.back_cats.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignalementService {

    @Autowired
    private SignalementRepository signalementRepository;

    @Autowired
    private BorneRepository borneRepository;

    @Autowired
    private UserRepository userRepository;

    public Signalement saveSignalement(Signalement signalement){
        System.out.println("Signalement: " + signalement.getDate() + " " + signalement.getMotif() + " " + signalement.getBorne() + " " + signalement.getUser());
        if(signalement.getBorne() != null && signalement.getBorne().getId() != null){
            ObjectId borneId = new ObjectId(signalement.getBorne().getId());
            System.out.println("Borne ID: " + borneId);
            Borne borne = borneRepository.findById(borneId)
                    .orElseThrow(() -> new RuntimeException("Borne not found"));
            signalement.setBorne(borne);
        }

        if(signalement.getUser() != null && signalement.getUser().getId() != null){
            ObjectId userId = new ObjectId(signalement.getUser().getId());
            System.out.println("User ID: " + userId);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            signalement.setUser(user);
        }

        return signalementRepository.save(signalement);
    }

    public Signalement getSignalement(ObjectId id){
        return signalementRepository.findById(id)
                .orElse(null);
    }

    public Signalement updateSignalement(ObjectId id, Signalement signalement){
        Signalement existingSignalement = signalementRepository.findById(id)
                .orElse(null);
        if(existingSignalement != null){
            existingSignalement.setMotif(signalement.getMotif());
            existingSignalement.setDate(signalement.getDate());
            if(signalement.getBorne() != null && signalement.getBorne().getId() != null){
                ObjectId borneId = new ObjectId(signalement.getBorne().getId());
                Borne borne = borneRepository.findById(borneId)
                        .orElseThrow(() -> new RuntimeException("Borne not found"));
                existingSignalement.setBorne(borne);
            }
            if(signalement.getUser() != null && signalement.getUser().getId() != null){
                ObjectId userId = new ObjectId(signalement.getUser().getId());
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                existingSignalement.setUser(user);
            }
            return signalementRepository.save(existingSignalement);
        }
        return null;
    }

    public Iterable<Signalement> getSignalements() {
        return signalementRepository.findAll();
    }
}

