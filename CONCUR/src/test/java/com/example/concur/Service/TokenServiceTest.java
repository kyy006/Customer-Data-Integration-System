package com.example.concur.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

class TokenServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsTokenExpired() {
        String token = createMockToken(LocalDateTime.now().minusDays(1));
        assertTrue(tokenService.isTokenExpired(token));

        token = createMockToken(LocalDateTime.now().plusDays(1));
        assertFalse(tokenService.isTokenExpired(token));
    }

    @Test
    void testDecodeEmailFromToken() {
        String email = "test@example.com";
        String token = createMockToken(LocalDateTime.now().plusDays(1), email);
        assertEquals(email, tokenService.decodeEmailFromToken(token));
    }

    @Test
    void testIsValidToken() {
        String token = createMockToken(LocalDateTime.now().plusDays(1));
        assertTrue(tokenService.isValidToken(token));

        String invalidToken = "invalidToken";
        assertFalse(tokenService.isValidToken(invalidToken));
    }

    @Test
    void testFetchNewToken() {
        String email = "test@example.com";
        String newToken = "newToken";
        String url = "http://localhost:8082/token/get/" + email;

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), eq(null), eq(new ParameterizedTypeReference<Map<String, String>>() {})))
                .thenReturn(new ResponseEntity<>(Map.of("token", newToken), OK));

        assertEquals(newToken, tokenService.fetchNewToken(email));
    }

    private String createMockToken(LocalDateTime expirationDateTime) {
        String email = "test@example.com";
        return createMockToken(expirationDateTime, email);
    }

    private String createMockToken(LocalDateTime expirationDateTime, String email) {
        String expirationString = expirationDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String token = email + ":" + expirationString;
        return Base64.getEncoder().encodeToString(token.getBytes());
    }
}