package com.example.cwt.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.cwt.repository.TokenRepository;
import  com.example.cwt.Entity.Token;
import java.time.LocalDateTime;
import java.util.Optional;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
public class TokenService {


    @Autowired
    private TokenRepository tokenRepository;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public void generateToken(String email) {
        String tokenString = encodeToken(email, LocalDateTime.now().plusHours(1));
        Optional<Token> optionalToken = tokenRepository.findByEmail(email);
        Token token;
        if (optionalToken.isPresent()) {
            token = optionalToken.get();
            token.setToken(tokenString);
            token.setExpirationTimestamp(LocalDateTime.now().plusHours(1));
        } else {
            token = new Token();
            token.setEmail(email);
            token.setToken(tokenString);
            token.setExpirationTimestamp(LocalDateTime.now().plusHours(1));
        }
        tokenRepository.save(token);
    }

    public Token refreshToken(String email) {
        Optional<Token> optionalToken = tokenRepository.findByEmail(email);
        Token token;
        if (optionalToken.isPresent()) {
            token = optionalToken.get();
            if (token.getExpirationTimestamp().isBefore(LocalDateTime.now().plusMinutes(1))) {
                String tokenString = encodeToken(email, LocalDateTime.now().plusHours(1));
                token.setToken(tokenString);
                token.setExpirationTimestamp(LocalDateTime.now().plusHours(1));
                tokenRepository.save(token);
            }
        } else {
            token = new Token();
            token.setEmail(email);
            String tokenString = encodeToken(email, LocalDateTime.now().plusHours(1));
            token.setToken(tokenString);
            token.setExpirationTimestamp(LocalDateTime.now().plusHours(1));
            tokenRepository.save(token);
        }
        return token;
    }

    private String encodeToken(String email, LocalDateTime expirationTimestamp) {
        String tokenContent = email + ":" + expirationTimestamp.format(DATE_TIME_FORMATTER);
        return Base64.getEncoder().encodeToString(tokenContent.getBytes());
    }

}