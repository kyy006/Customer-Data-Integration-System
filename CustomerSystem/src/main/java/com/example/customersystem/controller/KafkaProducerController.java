package com.example.customersystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.customersystem.repository.UserProfileRepository;
import com.example.customersystem.Entity.UserProfile;
import com.example.customersystem.Service.KafkaProducerService;

import java.util.Optional;

@RestController
@RequestMapping("/usersProduce")
public class KafkaProducerController {
    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @PostMapping("/send-email")
    public ResponseEntity<String> sendUserEmail(@RequestParam(value = "email") String email) {
        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is required");
        }
        Optional<UserProfile> existingUser = userProfileRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            // Call the Kafka producer to send the email
            kafkaProducerService.sendUserEmail(email);
            return ResponseEntity.ok("User email sent to Kafka topic");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with email " + email + " not found");
        }
    }


    @PostMapping("/create-send-email")
    public ResponseEntity<String> createUser(@RequestBody UserProfile userProfile) {
        if (userProfile.getEmail() == null || userProfile.getEmail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is required");
        }

        Optional<UserProfile> existingUser = userProfileRepository.findByEmail(userProfile.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with email " + userProfile.getEmail() + " already exists");
        }

        UserProfile savedUser = userProfileRepository.save(userProfile);
        // Send user email to Kafka after saving the user
        kafkaProducerService.sendUserEmail(userProfile.getEmail());
        return ResponseEntity.ok("User created successfully with ID " + savedUser.getId());
    }
}
