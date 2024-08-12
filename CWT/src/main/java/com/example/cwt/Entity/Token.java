package com.example.cwt.Entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String token;
    private LocalDateTime expirationTimestamp;

    // Default constructor
    public Token() {}

    // Parameterized constructor
    public Token(String email, String token, LocalDateTime expirationTimestamp) {
        this.email = email;
        this.token = token;
        this.expirationTimestamp = expirationTimestamp;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpirationTimestamp() {
        return expirationTimestamp;
    }

    public void setExpirationTimestamp(LocalDateTime expirationTimestamp) {
        this.expirationTimestamp = expirationTimestamp;
    }
}
