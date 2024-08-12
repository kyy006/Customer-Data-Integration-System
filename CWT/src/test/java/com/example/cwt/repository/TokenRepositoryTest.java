package com.example.cwt.repository;

import com.example.cwt.Entity.Token;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TokenRepositoryTest {

    @Autowired
    private TokenRepository tokenRepository;

    @Test
    public void testFindByEmail_existingEmail() {
        // Create and save a token
        Token token = new Token("test@example.com", "token123", LocalDateTime.now().plusMinutes(30));
        tokenRepository.save(token);

        // Find the token by email
        Optional<Token> foundToken = tokenRepository.findByEmail("test@example.com");
        assertTrue(foundToken.isPresent());
        assertEquals("test@example.com", foundToken.get().getEmail());
        assertEquals("token123", foundToken.get().getToken());
    }

    @Test
    public void testFindByEmail_nonExistingEmail() {
        // Try to find a token by email that doesn't exist
        Optional<Token> foundToken = tokenRepository.findByEmail("nonexistent@example.com");
        assertFalse(foundToken.isPresent());
    }

    @Test
    public void testSaveToken() {
        // Create and save a token
        Token token = new Token("new@example.com", "newToken", LocalDateTime.now().plusHours(1));
        Token savedToken = tokenRepository.save(token);

        // Verify the token was saved
        assertNotNull(savedToken.getId());
        assertEquals("new@example.com", savedToken.getEmail());
        assertEquals("newToken", savedToken.getToken());
    }

    @Test
    public void testDeleteToken() {
        // Create and save a token
        Token token = new Token("delete@example.com", "deleteToken", LocalDateTime.now().plusHours(1));
        Token savedToken = tokenRepository.save(token);

        // Delete the token
        tokenRepository.delete(savedToken);

        // Verify the token was deleted
        Optional<Token> foundToken = tokenRepository.findByEmail("delete@example.com");
        assertFalse(foundToken.isPresent());
    }
}
