package com.example.customersystem.controller;

import com.example.customersystem.Entity.UserProfile;
import com.example.customersystem.Service.KafkaProducerService;
import com.example.customersystem.repository.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class KafkaProducerControllerTest {

    @Mock
    private KafkaProducerService kafkaProducerService;

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private KafkaProducerController kafkaProducerController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testCreateUser_EmailIsRequired() {
        UserProfile userProfile = new UserProfile();

        ResponseEntity<String> response = kafkaProducerController.createUser(userProfile);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email is required", response.getBody());
        verify(userProfileRepository, times(0)).save(any(UserProfile.class));
        verify(kafkaProducerService, times(0)).sendUserEmail(anyString());
    }

    @Test
    public void testCreateUser_UserAlreadyExists() {
        UserProfile userProfile = new UserProfile();
        userProfile.setEmail("test@example.com");

        when(userProfileRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userProfile));

        ResponseEntity<String> response = kafkaProducerController.createUser(userProfile);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("User with email test@example.com already exists", response.getBody());
        verify(userProfileRepository, times(0)).save(any(UserProfile.class));
        verify(kafkaProducerService, times(0)).sendUserEmail(anyString());
    }

    @Test
    public void testSendUserEmail_EmailIsRequired() {
        ResponseEntity<String> response = kafkaProducerController.sendUserEmail("");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email is required", response.getBody());
        verify(userProfileRepository, times(0)).findByEmail(anyString());
        verify(kafkaProducerService, times(0)).sendUserEmail(anyString());
    }

    @Test
    public void testSendUserEmail_UserNotFound() {
        when(userProfileRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        ResponseEntity<String> response = kafkaProducerController.sendUserEmail("test@example.com");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User with email test@example.com not found", response.getBody());
        verify(userProfileRepository, times(1)).findByEmail("test@example.com");
        verify(kafkaProducerService, times(0)).sendUserEmail(anyString());
    }

    @Test
    public void testSendUserEmail_UserEmailSent() {
        UserProfile userProfile = new UserProfile();
        userProfile.setEmail("test@example.com");

        when(userProfileRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userProfile));

        ResponseEntity<String> response = kafkaProducerController.sendUserEmail("test@example.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User email sent to Kafka topic", response.getBody());
        verify(userProfileRepository, times(1)).findByEmail("test@example.com");
        verify(kafkaProducerService, times(1)).sendUserEmail("test@example.com");
    }
}

