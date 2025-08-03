package com.example.HotelManagementSystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // Authenticate user and return JWT or session details
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Optional: invalidate token/session
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(Principal principal) {
        // Return user details based on authenticated principal
    }
}
