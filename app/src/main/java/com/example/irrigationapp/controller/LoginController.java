package com.example.irrigationapp.controller;

import com.example.irrigationapp.model.User;

public class LoginController {

    public boolean checkLogin(User user) {
        // Remplace ceci par une vraie vérification (via RMI ou réseau plus tard)
        return user.getUsername().equals("admin") && user.getPassword().equals("admin");
    }
}