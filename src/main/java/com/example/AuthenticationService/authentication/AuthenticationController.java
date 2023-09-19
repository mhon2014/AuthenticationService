package com.example.AuthenticationService.authentication;

import com.example.AuthenticationService.appuser.AppUser;
import com.example.AuthenticationService.appuser.AppUserRole;
import com.example.AuthenticationService.appuser.AppUserService;
import com.example.AuthenticationService.dto.UserDto;
import com.example.AuthenticationService.security.CookieAuthFilter;
import com.example.AuthenticationService.security.config.WebSecurityConfig;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final JWTService jwtService;

    private final AppUserService appUserService;

//    @Value("{$Spring.cookie-name}")
//    private final String SessionCookie;

    @GetMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody(required = false) UserDto userDto, HttpServletResponse response){
        try {

            String token = authenticationService.signIn(userDto);
            Cookie authCookie = new Cookie("SESSION", token);
            authCookie.setHttpOnly(true);
            authCookie.setPath("/");
            response.addCookie(authCookie);
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

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOut(@RequestBody UserDto userDto, HttpServletResponse response){
        try {
            authenticationService.signOut(userDto);
            //Remove Token from Redis


        }
        catch (IllegalStateException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto, HttpServletResponse response){
        try {
            appUserService.signUpUser(
                    new AppUser(
                        userDto.getEmail(),
                        userDto.getPassword(),
                        AppUserRole.USER
                        )
                );
        }
        catch (IllegalStateException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam("token") String token, HttpServletResponse response){
//        response.addCookie();
        //Verify account param
        System.out.println(token);
        appUserService.verifyToken(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

