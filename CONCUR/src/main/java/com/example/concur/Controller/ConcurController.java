package com.example.concur.Controller;
import com.example.concur.Service.KafkaProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.example.concur.Service.TokenService;
import com.example.concur.repository.UserDetailsRepository;
import com.example.concur.Entity.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/concur")
public class ConcurController {



    @Autowired
    private TokenService tokenService;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping(value = "/user-details/{token}", produces = "application/json")
    public ResponseEntity<String> getUserDetails(@PathVariable String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is required");
        }
        if (!tokenService.isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }
        String email = tokenService.decodeEmailFromToken(token);

        if (tokenService.isTokenExpired(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired. Please refresh your token.");
            // Fetch new token from CWT
//            token = tokenService.fetchNewToken(email);
//            if (tokenService.isTokenExpired(token)) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to fetch a valid token");
//            }
        }

        Optional<UserDetails> optionalUserDetails = userDetailsRepository.findByEmail(email);
        if (optionalUserDetails.isPresent()) {
            UserDetails userDetails = optionalUserDetails.get();
            String userDetailsJson = convertToJson(userDetails);
            kafkaProducerService.sendUserDetails(userDetailsJson);
            return ResponseEntity.ok(userDetailsJson);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User details not found");
        }

    }

    @GetMapping(value = "/refresh-token/{token}")
    public ResponseEntity<String> refreshToken(@PathVariable String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is required");
        }
        if (!tokenService.isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }
        String email = tokenService.decodeEmailFromToken(token);
        try {
            String newToken = tokenService.fetchNewToken(email);
            if (tokenService.isTokenExpired(newToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to fetch a valid token");
            }
            return ResponseEntity.ok(newToken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching new token");
        }
    }

    private String convertToJson(UserDetails userDetails) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(userDetails);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }
//    private String convertToJson(UserDetails userDetails) {
//        // Convert UserDetails to JSON
//        // This is a simple example, you can use a library like Jackson
//        return "{ \"email\": \"" + userDetails.getEmail() + "\", " +
//                "\"firstName\": \"" + userDetails.getFirstName() + "\", " +
//                "\"lastName\": \"" + userDetails.getLastName() + "\", " +
//                "\"additionalDetails\": \"" + userDetails.getAdditionalDetails() + "\" }";
//    }


}
