package com.example.concur.Controller;
import com.example.concur.Service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.concur.repository.UserDetailsRepository;
import com.example.concur.Entity.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/user-details")
public class UserDetailsController {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @GetMapping
    public ResponseEntity<List<UserDetails>> getAllUserDetails() {
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        return ResponseEntity.ok(userDetailsList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetails> getUserDetailsById(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Optional<UserDetails> optionalUserDetails = userDetailsRepository.findById(id);
        return optionalUserDetails
                .map(userDetails -> ResponseEntity.ok().body(userDetails))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<UserDetails> createUserDetails(@RequestBody UserDetails userDetails) {
        if (userDetails.getEmail() == null || userDetails.getEmail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Optional<UserDetails> existingUserDetails = userDetailsRepository.findByEmail(userDetails.getEmail());
        if (existingUserDetails.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // or you can return a specific message
        }
        UserDetails savedUserDetails = userDetailsRepository.save(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserDetails);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDetails> updateUserDetails(@PathVariable Long id, @RequestBody UserDetails userDetails) {
        if (userDetails.getEmail() == null || userDetails.getEmail().isEmpty()|| id == null || id <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Optional<UserDetails> optionalUserDetails = userDetailsRepository.findById(id);
        if (optionalUserDetails.isPresent()) {
            UserDetails existingUserDetails = optionalUserDetails.get();
            existingUserDetails.setEmail(userDetails.getEmail());
            existingUserDetails.setFirstName(userDetails.getFirstName());
            existingUserDetails.setLastName(userDetails.getLastName());
            existingUserDetails.setUsername(userDetails.getUsername());
            existingUserDetails.setAddress(userDetails.getAddress());
            existingUserDetails.setPhone(userDetails.getPhone());
            existingUserDetails.setWebsite(userDetails.getWebsite());
            existingUserDetails.setCompany(userDetails.getCompany());
            UserDetails updatedUserDetails = userDetailsRepository.save(existingUserDetails);
            return ResponseEntity.ok(updatedUserDetails);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserDetails(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Optional<UserDetails> optionalUserDetails = userDetailsRepository.findById(id);
        if (optionalUserDetails.isPresent()) {
            userDetailsRepository.delete(optionalUserDetails.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @DeleteMapping("/delete/{email}")
    public ResponseEntity<Void> deleteUserDetails(@PathVariable String email) {
        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Optional<UserDetails> optionalUserDetails = userDetailsRepository.findByEmail(email);
        if (optionalUserDetails.isPresent()) {
            userDetailsRepository.delete(optionalUserDetails.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }



}
