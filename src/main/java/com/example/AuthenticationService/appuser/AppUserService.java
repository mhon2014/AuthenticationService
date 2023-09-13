package com.example.AuthenticationService.appuser;

import com.example.AuthenticationService.email.EmailService;
import com.example.AuthenticationService.token.VerificationToken;
import com.example.AuthenticationService.token.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;

    //Find username
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String signUpUser(AppUser appUser){
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

        //TODO Send email
        emailService.sendVerificationEmail(appUser.getEmail(), token);

        return token;
    }

    public int enableAppUser(String email){
        return appUserRepository.enableAppUser(email);
    }

}
