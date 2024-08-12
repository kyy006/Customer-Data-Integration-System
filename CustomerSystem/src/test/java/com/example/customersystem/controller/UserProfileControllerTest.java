package com.example.customersystem.controller;

import com.example.customersystem.Entity.UserProfile;
import com.example.customersystem.repository.UserProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserProfileControllerTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private UserProfileController userProfileController;

    @Test
    public void testCreateUser_UserAlreadyExists() {
        UserProfile userProfile = new UserProfile();
        userProfile.setEmail("test@example.com");

        when(userProfileRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userProfile));

        ResponseEntity<String> response = userProfileController.createUser(userProfile);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("User with email test@example.com already exists", response.getBody());
    }

    @Test
    public void testCreateUser_Success() {
        UserProfile userProfile = new UserProfile();
        userProfile.setEmail("test@example.com");

        when(userProfileRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(userProfile);

        ResponseEntity<String> response = userProfileController.createUser(userProfile);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testGetUserById_UserExists() {
        UserProfile userProfile = new UserProfile();
        userProfile.setId(1L);

        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(userProfile));

        ResponseEntity<UserProfile> response = userProfileController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userProfile, response.getBody());
    }

    @Test
    public void testGetUserById_UserNotFound() {
        when(userProfileRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<UserProfile> response = userProfileController.getUserById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateUser_UserExists() {
        UserProfile userProfile = new UserProfile();
        userProfile.setId(1L);
        userProfile.setEmail("test@example.com");

        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(userProfile));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(userProfile);

        ResponseEntity<String> response = userProfileController.updateUser(1L, userProfile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User updated successfully", response.getBody());
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        UserProfile userProfile = new UserProfile();
        userProfile.setEmail("test@example.com");

        when(userProfileRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<String> response = userProfileController.updateUser(1L, userProfile);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User with ID 1 not found", response.getBody());
    }


    @Test
    public void testDeleteUser_UserExists() {
        UserProfile userProfile = new UserProfile();
        userProfile.setId(1L);

        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(userProfile));

        ResponseEntity<String> response = userProfileController.deleteUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User deleted successfully", response.getBody());
        verify(userProfileRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        when(userProfileRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<String> response = userProfileController.deleteUser(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
