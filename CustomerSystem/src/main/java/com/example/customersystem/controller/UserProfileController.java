package com.example.customersystem.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.customersystem.repository.UserProfileRepository;
import com.example.customersystem.Entity.UserProfile;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserProfileController {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody UserProfile userProfile) {
        if (userProfile.getEmail() == null || userProfile.getEmail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is required");
        }

        Optional<UserProfile> existingUser = userProfileRepository.findByEmail(userProfile.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with email " + userProfile.getEmail() + " already exists");
        }

        UserProfile savedUser = userProfileRepository.save(userProfile);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully with ID " + savedUser.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getUserById(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Optional<UserProfile> userProfile = userProfileRepository.findById(id);
        if (userProfile.isPresent()) {
            return ResponseEntity.ok(userProfile.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserProfile userProfile) {
        if (userProfile.getEmail() == null || userProfile.getEmail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is required");
        }
        if (id == null || id <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Optional<UserProfile> existingUser = userProfileRepository.findById(id);
        if (existingUser.isPresent()) {
            UserProfile userToUpdate = existingUser.get();
            userToUpdate.setEmail(userProfile.getEmail());
            userToUpdate.setFirstName(userProfile.getFirstName());
            userToUpdate.setLastName(userProfile.getLastName());
            userProfileRepository.save(userToUpdate);
            return ResponseEntity.ok("User updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with ID " + id + " not found");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Optional<UserProfile> existingUser = userProfileRepository.findById(id);
        if (existingUser.isPresent()) {
            userProfileRepository.deleteById(id);
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with ID " + id + " not found");
        }
    }
}
