package com.example.cwt.Controller;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import com.example.cwt.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.cwt.Service.TokenService;
import  com.example.cwt.Entity.Token;



@RestController
@RequestMapping("/token")
public class TokenController {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private TokenService tokenService;

    @GetMapping("/get/{email}")
    public ResponseEntity<Object> getToken(@PathVariable String email) {
        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is required");
        }
        Optional<Token> optionalToken = tokenRepository.findByEmail(email);
        Token token;
        if (optionalToken.isPresent()) {
            token = optionalToken.get();
            if (token.getExpirationTimestamp().isBefore(LocalDateTime.now().plusMinutes(1))) {
                token = tokenService.refreshToken(email);
            }
        } else {
            token = tokenService.refreshToken(email);
        }
        return ResponseEntity.ok(token);
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<String> deleteTokenByEmail(@PathVariable String email) {
        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is required");
        }

        Optional<Token> optionalToken = tokenRepository.findByEmail(email);
        if (optionalToken.isPresent()) {
            tokenRepository.delete(optionalToken.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token with email " + email + " not found");
        }
    }
}