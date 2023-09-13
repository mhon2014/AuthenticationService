package com.example.AuthenticationService.login;

import com.example.AuthenticationService.appuser.AppUserService;
import com.example.AuthenticationService.registration.EmailValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;

    private final AuthenticationManager authenticationManager;

    public Authentication login(LoginRequest request){

        boolean isValidEmail = emailValidator.test(request.getEmail());

        if(!isValidEmail){
            throw new IllegalStateException("Email not valid");
        }

        UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()
        );

//        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authenticationManager.
                authenticate(user);

    }

}
