package com.example.concur.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.concur.Entity.UserDetails;

import java.util.Optional;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    Optional<UserDetails> findByEmail(String email);
}

