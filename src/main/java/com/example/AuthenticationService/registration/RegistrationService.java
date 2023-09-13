package com.example.AuthenticationService.registration;

import com.example.AuthenticationService.appuser.AppUser;
import com.example.AuthenticationService.appuser.AppUserRole;
import com.example.AuthenticationService.appuser.AppUserService;
import com.example.AuthenticationService.token.VerificationToken;
import com.example.AuthenticationService.token.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegistrationService {


    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    private final VerificationTokenService verificationTokenService;



    public String register(RegistrationRequest request) {

        boolean isValidEmail = emailValidator.test(request.getEmail());

        if(!isValidEmail){
            throw new IllegalStateException("Email not valid");
        }
        return appUserService.signUpUser(
                new AppUser(
                        request.getEmail(),
                        request.getPassword(),
                        AppUserRole.USER
                )
        );
    }


    public String verifyToken(String token){
        VerificationToken verificationToken = verificationTokenService
                                        .getToken(token)
                                        .orElseThrow(() -> new IllegalStateException("Invalid token"));

        //check if account is already verified
        if (verificationToken.isVerified()){
            throw new IllegalStateException("Account already verified");
        }

        LocalDateTime expiredAt = verificationToken.getExpiredTime();

        //check if token is expired
        if(expiredAt.isBefore(LocalDateTime.now())){
            throw new IllegalStateException("Verification is expired");
        }

        //Set account to verified
        System.out.println(verificationTokenService.setVerify(token, true));

        appUserService.enableAppUser(
                verificationToken
                .getAppUser()
                .getEmail()
        );

        return "Verified!";
    }
}
