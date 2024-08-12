package com.example.cwt.Service;

import com.example.cwt.Entity.Token;
import com.example.cwt.repository.TokenRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TokenServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private TokenService tokenService;

    public TokenServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerateToken_newToken() {
        when(tokenRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        tokenService.generateToken("test@example.com");

        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    public void testGenerateToken_existingToken() {
        Token existingToken = new Token("test@example.com", "oldToken", LocalDateTime.now().plusMinutes(5));
        when(tokenRepository.findByEmail("test@example.com")).thenReturn(Optional.of(existingToken));

        tokenService.generateToken("test@example.com");

        verify(tokenRepository, times(1)).save(existingToken);
    }

    @Test
    public void testRefreshToken_newToken() {
        when(tokenRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        Token refreshedToken = tokenService.refreshToken("test@example.com");

        assertNotNull(refreshedToken);
        assertEquals("test@example.com", refreshedToken.getEmail());
        verify(tokenRepository, times(1)).save(refreshedToken);
    }

    @Test
    public void testRefreshToken_existingToken() {
        Token existingToken = new Token("test@example.com", "oldToken", LocalDateTime.now().minusMinutes(5));
        when(tokenRepository.findByEmail("test@example.com")).thenReturn(Optional.of(existingToken));

        Token refreshedToken = tokenService.refreshToken("test@example.com");

        assertNotNull(refreshedToken);
        assertEquals("test@example.com", refreshedToken.getEmail());
        verify(tokenRepository, times(1)).save(existingToken);
    }
}