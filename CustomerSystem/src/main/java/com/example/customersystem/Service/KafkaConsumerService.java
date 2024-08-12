package com.example.customersystem.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.example.customersystem.Entity.UserProfile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.customersystem.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.customersystem.Entity.UserDetails;

import java.util.Optional;

@Service
public class KafkaConsumerService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);


    @KafkaListener(topics = "user_details_topic", groupId = "concur")
    public void consume(String message) {
        logger.info("Received Message: {}", message);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            UserDetails userDetails = objectMapper.readValue(message, UserDetails.class);
            Optional<UserProfile> optionalUserProfile = userProfileRepository.findByEmail(userDetails.getEmail());
            if (optionalUserProfile.isPresent()) {
                UserProfile userProfile = optionalUserProfile.get();
                // Combine the results with first name, last name from UserProfile
                userDetails.setFirstName(userProfile.getFirstName());
                userDetails.setLastName(userProfile.getLastName());
                // Log combined details
                logger.info("Combined User Details: {}", convertToJson(userDetails));
            } else {
                logger.warn("User profile not found for email: {}", userDetails.getEmail());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String convertToJson(UserDetails userDetails) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(userDetails);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}"; // Return empty JSON if serialization fails
        }
    }
}