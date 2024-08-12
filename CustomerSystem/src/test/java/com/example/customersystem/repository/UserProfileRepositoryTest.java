package com.example.customersystem.repository;

import com.example.customersystem.Entity.UserProfile;
import com.example.customersystem.controller.UserProfileController;
import com.example.customersystem.repository.UserProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserProfileRepositoryTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private UserProfileController userProfileController;

    @Test
    public void testFindByEmail() {
        UserProfile user = new UserProfile();
        user.setEmail("test@example.com");

        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(user);
        when(userProfileRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        userProfileRepository.save(user);

        Optional<UserProfile> foundUser = userProfileRepository.findByEmail("test@example.com");

        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.get().getEmail());
    }
}
