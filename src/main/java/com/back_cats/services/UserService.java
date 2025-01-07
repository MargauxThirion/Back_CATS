package com.back_cats.services;

import com.back_cats.exceptions.UserException;
import com.back_cats.exceptions.VoitureException;
import com.back_cats.models.User;
import com.back_cats.models.Voiture;
import com.back_cats.repositories.UserRepository;
import com.back_cats.repositories.VoitureRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VoitureRepository voitureRepository;

    public User addUser(User user) {
        try {
            return userRepository.save(user);
        } catch (DataAccessException e) {
            throw new UserException("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }

    public User findByMail(String mail) {
        User user = userRepository.findByMail(mail);
        if (user == null) {
            throw new UserException("Aucun utilisateur trouvé avec l'adresse mail : " + mail);
        }
        return user;
    }

    public User addVoitureToUser(ObjectId userId, ObjectId voitureId) {
        // Vérifier si l'utilisateur existe
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("Utilisateur introuvable avec l'ID : " + userId));

        // Vérifier si la voiture existe
        Voiture voiture = voitureRepository.findById(voitureId)
                .orElseThrow(() -> new VoitureException("Voiture introuvable avec l'ID : " + voitureId));

        // Initialiser la liste si elle est nulle
        if (user.getVoitures() == null) {
            user.setVoitures(new ArrayList<>());
        }

        // Ajouter la voiture entière à la liste des voitures de l'utilisateur
        user.getVoitures().add(voiture);

        try {
            return userRepository.save(user);
        } catch (DataAccessException e) {
            throw new UserException("Erreur lors de l'ajout de la voiture à l'utilisateur : " + e.getMessage());
        }
    }

    public User getUser(ObjectId userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException("Utilisateur introuvable avec l'ID : " + userId));
    }

    public Iterable<User> getUsers() {
        try {
            return userRepository.findAll();
        } catch (DataAccessException e) {
            throw new UserException("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }
    }

    public User registerNewUser(String mail) {
        try {
            User newUser = new User();
            newUser.setMail(mail);
            newUser.setMotDePasse(null); // Il est fortement conseillé de gérer le mot de passe de manière sécurisée.
            newUser.setRole("User");
            newUser.setVoitures(new ArrayList<>());
            return userRepository.save(newUser);
        } catch (DataAccessException e) {
            throw new UserException("Erreur lors de l'enregistrement de l'utilisateur: " + e.getMessage());
        }
    }

    public User getUserByMail(String mail) {
        return userRepository.findByMail(mail);
    }

    public void deleteUser(ObjectId userId) {
        try {
            userRepository.deleteById(userId);
        } catch (DataAccessException e) {
            throw new UserException("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
        }
    }
}
