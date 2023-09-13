package com.example.AuthenticationService.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);

    @Modifying
    @Query("UPDATE VerificationToken t " +
            "SET t.verified = ?2 " +
            "WHERE t.token = ?1")
    int updateVerify(String token, boolean verified);
}
