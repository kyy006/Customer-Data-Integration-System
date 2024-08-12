package com.example.concur.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;
import org.springframework.http.ResponseEntity;

@Service
public class TokenService {
    @Autowired
    private RestTemplate restTemplate;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;


    public boolean isTokenExpired(String token) {
        try {
            String decodedToken = new String(Base64.getDecoder().decode(token));
            String[] parts = decodedToken.split(":");
            if (parts.length < 4) {
                throw new IllegalArgumentException("Invalid token format");
            }
            String expirationTimestampStr = parts[1] + ":" + parts[2] + ":" + parts[3];

            LocalDateTime expirationTimestamp = LocalDateTime.parse(expirationTimestampStr);
            return expirationTimestamp.isBefore(LocalDateTime.now());
        } catch (Exception e) {
            e.printStackTrace();
            return true; // Consider the token expired if any error occurs
        }
    }


    public String decodeEmailFromToken(String token) {
        try {
            String decodedToken = new String(Base64.getDecoder().decode(token));
            String[] parts = decodedToken.split(":");
            if (parts.length < 1) {
                throw new IllegalArgumentException("Invalid token format");
            }
            return parts[0];
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Failed to decode email from token");
        }
    }


    public boolean isValidToken(String token) {
        try {
            String decodedToken = new String(Base64.getDecoder().decode(token));
            String[] parts = decodedToken.split(":");
            return parts.length >= 4;
        } catch (Exception e) {
            return false; // Token is invalid if any error occurs during decoding
        }
    }
    public String fetchNewToken(String email) {
        String url = "http://localhost:8082/token/get/" + email;  // URL of the CWT Refresh Token API
        ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, String>>() {}
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody().get("token");
        }
        throw new RuntimeException("Failed to fetch new token from CWT");
    }

}
