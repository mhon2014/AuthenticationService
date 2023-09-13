package com.example.AuthenticationService.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    //Password is encoded;
    @GetMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response){
//        ResponseEntity<?> response = new ResponseEntity<?>();
//        return loginService.login(request);
        Authentication authentication = loginService.login(request);

        if(!authentication.isAuthenticated()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String token = UUID.randomUUID().toString();
        Cookie cookie = new Cookie("session", token);
        cookie.setPath(":/");
        response.addCookie(cookie);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
