package com.example.AuthenticationService.authentication;

import com.example.AuthenticationService.appuser.AppUser;
import com.example.AuthenticationService.appuser.AppUserRole;
import com.example.AuthenticationService.appuser.AppUserService;
import com.example.AuthenticationService.dto.UserDto;
import com.example.AuthenticationService.email.EmailService;
import com.example.AuthenticationService.security.JWTService;
import com.example.AuthenticationService.verification.VerificationToken;
import com.example.AuthenticationService.verification.VerificationTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthenticationController {

//    private final AuthenticationService authenticationService;

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;

    private final AppUserService appUserService;

    private final VerificationTokenService verificationTokenService;

    private final EmailService emailService;

//    @Value("{$Spring.cookie-name}")
//    private final String SessionCookie;

    @GetMapping("/login")
    public ResponseEntity<String> signIn(@Valid @RequestBody UserDto userDto, HttpServletResponse response){
        try {

            UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                    userDto.getEmail(), userDto.getPassword()
            );

            Authentication user = authenticationManager.authenticate(userToken);
            SecurityContext context;
            String sessionId = UUID.randomUUID().toString();
            System.out.println(user.isAuthenticated());

            if (user.isAuthenticated()) {
                //use context to avoid race conditions, ie thread accessing the same data
                context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(userToken);
                String token = jwtService.createToken(userDto, sessionId);
                // STORE SESSION TOKEN ON REDIS DB


                Cookie authCookie = new Cookie("SESSION", token);
                authCookie.setHttpOnly(true);
                authCookie.setPath("/");

                response.addCookie(authCookie);
            }
            else {
                throw new IllegalStateException("Not Authenticated");
            }
        }
        catch (BadCredentialsException e){
            return new ResponseEntity<String>("User does not exist", HttpStatus.NOT_FOUND);
        }
        catch (DisabledException e){
            return new ResponseEntity<String>("User is not verified", HttpStatus.BAD_REQUEST);
        }
        catch (AuthenticationException e){
            return new ResponseEntity<String>("User not authenticated", HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<String>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> signOut(HttpServletResponse response){
        try {
            SecurityContextHolder.clearContext();
//            authenticationService.signOut();
            //Remove Token from Redis
        }
        catch (IllegalStateException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto, HttpServletResponse response){
        try {

            //register user
            appUserService.registerUser(userDto);
        }
        catch (IllegalStateException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam("token") String token, HttpServletResponse response){
        //Verify account param
        try {
            verificationTokenService.verifyToken(token);
            response.sendRedirect("/login");
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<String>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //TODO: Add redirect to login page
        //Set body response that user is verified
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

