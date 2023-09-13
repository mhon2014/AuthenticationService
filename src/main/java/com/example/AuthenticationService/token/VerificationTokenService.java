package com.example.AuthenticationService.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public int setVerify(String token, boolean verified){
        return verificationTokenRepository.updateVerify(token, verified);
    }


}
