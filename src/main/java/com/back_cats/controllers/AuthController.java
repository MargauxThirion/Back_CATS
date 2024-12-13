package com.back_cats.controllers;

import com.back_cats.models.User;
import com.back_cats.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginUser) {
        User user = userService.findByMail(loginUser.getMail());
        if (user != null ) {
            // Connecter l'utilisateur
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(401).body("Echec de la connexion");
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> userDetails) {
        String mail = userDetails.get("mail");
        if (mail == null || mail.isEmpty()) {
            return ResponseEntity.badRequest().body("Le mail est requis.");
        }
        User user = userService.registerNewUser(mail);
        return ResponseEntity.ok(user);
    }
}
