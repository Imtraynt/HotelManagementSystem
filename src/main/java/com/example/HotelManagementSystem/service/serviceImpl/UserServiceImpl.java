package com.example.HotelManagementSystem.service.serviceImpl;

import com.example.HotelManagementSystem.entity.User;
import com.example.HotelManagementSystem.repository.UserRepository;
import com.example.HotelManagementSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

    @Service
    public class UserServiceImpl implements UserService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        @Autowired
        public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
        }

        @Override
        public User registerUser(String username, String password, String role) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(role);
            return userRepository.save(user);
        }

        @Override
        public Optional<User> findByUsername(String username) {
            return userRepository.findByUsername(username);
        }
    }