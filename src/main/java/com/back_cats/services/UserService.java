package com.back_cats.services;

import com.back_cats.exceptions.UserException;
import com.back_cats.exceptions.VoitureException;
import com.back_cats.models.User;
import com.back_cats.models.Voiture;
import com.back_cats.repositories.UserRepository;
import com.back_cats.repositories.VoitureRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VoitureRepository voitureRepository;

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User addVoitureToUser(ObjectId userId, ObjectId voitureId) {
        // Vérifier si l'utilisateur existe
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("Utilisateur introuvable avec l'ID : " + userId));

        // Vérifier si la voiture existe
        Voiture voiture = voitureRepository.findById(voitureId)
                .orElseThrow(() -> new VoitureException("Voiture introuvable avec l'ID : " + voitureId));

        // Ajouter la voiture à l'utilisateur (en utilisant l'ID de la voiture)
        user.getVoitures().add(voitureId);  // Ajouter uniquement l'ID de la voiture à l'utilisateur
        return userRepository.save(user);
    }

    public User getUser(ObjectId userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("Utilisateur introuvable avec l'ID : " + userId));

        // Retourner l'utilisateur avec son ID sous forme de chaîne
        user.setId(user.getId());
        return user;
    }

    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }
}
