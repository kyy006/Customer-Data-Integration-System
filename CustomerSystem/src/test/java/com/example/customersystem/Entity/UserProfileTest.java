package com.example.customersystem.Entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserProfileTest {

    @Test
    public void testUserProfile() {
        UserProfile userProfile = new UserProfile();
        userProfile.setId(1L);
        userProfile.setEmail("test@example.com");
        userProfile.setFirstName("John");
        userProfile.setLastName("Doe");

        assertEquals(1L, userProfile.getId());
        assertEquals("test@example.com", userProfile.getEmail());
        assertEquals("John", userProfile.getFirstName());
        assertEquals("Doe", userProfile.getLastName());
    }
}
