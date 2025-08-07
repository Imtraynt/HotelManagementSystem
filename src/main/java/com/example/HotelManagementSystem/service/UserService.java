package com.example.HotelManagementSystem.service;

import com.example.HotelManagementSystem.entity.User;

import java.util.Optional;

public interface UserService {
    User registerUser(String username, String password, String role);
    Optional<User> findByUsername(String username);
}