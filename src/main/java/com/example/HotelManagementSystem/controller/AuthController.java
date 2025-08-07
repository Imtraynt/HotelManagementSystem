package com.example.HotelManagementSystem.controller;

import com.example.HotelManagementSystem.dto.request.LoginRequestDTO;
import com.example.HotelManagementSystem.entity.User;
import com.example.HotelManagementSystem.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping("/hello")
    public String hello() {
        return "Hello from Spring Boot!";
    }
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestParam String username, @RequestParam String password, @RequestParam String role) {
        if (userService.findByUsername(username).isPresent()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "username already taken");
            return ResponseEntity.ok(errorResponse);
        }

        User user = userService.registerUser(username, password, role);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered with ID: " + user.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDTO loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        User user = userService.findByUsername(username).orElse(null);

        if (user == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid username");
            return ResponseEntity.ok(errorResponse);
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid password");
            return ResponseEntity.ok(errorResponse);
        }

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 hours
                .signWith(SignatureAlgorithm.HS512, "your-secret-key") // Replace with secure key
                .compact();

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }
}