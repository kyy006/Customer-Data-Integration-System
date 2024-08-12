package com.example.cwt.repository;
import com.example.cwt.Entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByEmail(String email);
}