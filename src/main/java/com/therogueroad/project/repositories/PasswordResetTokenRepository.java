package com.therogueroad.project.repositories;

import com.therogueroad.project.models.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
   Optional<PasswordResetToken> findByToken(String token);
}
