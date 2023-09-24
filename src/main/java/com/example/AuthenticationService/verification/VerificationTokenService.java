package com.example.AuthenticationService.verification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;

    public void saveConfirmationToken(VerificationToken verificationToken){
        verificationTokenRepository.save(verificationToken);
    }

    public Optional<VerificationToken> getToken(String token){
        return verificationTokenRepository.findByToken(token);
    }

    public void setVerify(String token, boolean verified){
        verificationTokenRepository.updateVerify(token, verified);
    }

    public void verifyToken(String token){
        VerificationToken verificationToken = getToken(token)
                                .orElseThrow(() -> new IllegalStateException("Invalid token"));

        //check if account is already verified
        if (verificationToken.isVerified()){
            throw new IllegalStateException("Account already verified");
        }

        LocalDateTime expiredAt = verificationToken.getExpiredTime();

        //check if token is expired
        if(expiredAt.isBefore(LocalDateTime.now())){
            //resend verification
            throw new IllegalStateException("Verification is expired");
        }

        //Set account to verified
        setVerify(token, true);

        //enable the user

    }

}
