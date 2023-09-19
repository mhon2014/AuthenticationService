package com.example.AuthenticationService.appuser;

import com.example.AuthenticationService.email.EmailService;
import com.example.AuthenticationService.verification.VerificationToken;
import com.example.AuthenticationService.verification.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;

    //Find username
    //THIS IS NEEDED FOR AUTHENTICATIONMANAGER IN THE SECURITY CONFIG
    //THIS FUNCTION IS WHAT LOADS THE USER FROM THE DATABASE
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    //insert user into database
    public void signUpUser(AppUser appUser){
        //Find if user exists first, throw error if it exists

        boolean userExists = appUserRepository
                            .findByEmail(appUser.getEmail())
                            .isPresent();
        if (userExists){
            throw new IllegalStateException("Email already taken");
        }

        //encode password for user
        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());

        //set password for appuser
        appUser.setPassword(encodedPassword);
        //save user in repo
        appUserRepository.save(appUser);

        //generate token for verification
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(24),
                appUser
        );

        //save token into another table for verification
        verificationTokenService.saveConfirmationToken(verificationToken);

        //Send email verification
        emailService.sendVerificationEmail(appUser.getEmail(), token);

    }

    public void verifyToken(String token){
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
            //resend verification
            throw new IllegalStateException("Verification is expired");
        }

        //Set account to verified
        verificationTokenService.setVerify(token, true);

        //enable the user
        appUserRepository.enableAppUser(
                verificationToken
                        .getAppUser()
                        .getEmail()
        );
    }

    //enable the user.
//    public void enableAppUser(String email){
//         appUserRepository.enableAppUser(email);
//    }

}
