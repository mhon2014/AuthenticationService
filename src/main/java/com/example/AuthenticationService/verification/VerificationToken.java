package com.example.AuthenticationService.verification;

import com.example.AuthenticationService.appuser.AppUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/*
 *Verification Token entity for mapping to database
 *
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class VerificationToken {

    @Id
    @SequenceGenerator(
            name = "verification_token_sequence",
            sequenceName = "verification_token_sequence",
            allocationSize = 1
    )

    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "verification_sequence"
    )
    private Long id;
    private String token;
    private LocalDateTime createdTime;
    private LocalDateTime expiredTime;
    private boolean verified = false;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "app_user_id"
    )
    private AppUser appUser;

    public VerificationToken(String token,
                             LocalDateTime createdTime,
                             LocalDateTime expiredTime,
                             AppUser appUser) {
        this.token = token;
        this.createdTime = createdTime;
        this.expiredTime = expiredTime;
        this.appUser = appUser;
    }
}
