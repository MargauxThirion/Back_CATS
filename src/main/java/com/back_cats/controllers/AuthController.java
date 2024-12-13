package com.back_cats.controllers;

import com.back_cats.models.User;
import com.back_cats.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
