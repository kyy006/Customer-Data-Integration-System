package com.example.customersystem.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.customersystem.Entity.UserProfile;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByEmail(String email);
}
