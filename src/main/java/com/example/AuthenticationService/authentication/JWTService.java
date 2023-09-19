package com.example.AuthenticationService.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.AuthenticationService.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JWTService {

    private final Algorithm algorithm;

    public JWTService(@Value("${spring.jwt-secret-key}") String secretKey){
        this. algorithm = Algorithm.HMAC256(secretKey.getBytes());

    }


    public String createToken(UserDto userDto){

        String sessionID = UUID.randomUUID().toString();

        String token = JWT.create()
                .withSubject("userSession")
                .withClaim("userID", userDto.getEmail())
                .withClaim("sessionID", sessionID)
                .sign(algorithm);

        System.out.println(sessionID);

        return token;
    }

    public boolean verifyToken(String token){
        DecodedJWT decodedJWT;

        try {
            System.out.println("why");
            JWTVerifier verifier = JWT.require(algorithm)
                    .withSubject("userSession")
                    .build();

            System.out.println("whatthefuck");
            decodedJWT = verifier.verify(token);
            System.out.println(decodedJWT.getClaim("userID"));
            System.out.println(decodedJWT.getClaim("sessionID"));
            //check if token is in redis
            return true;
        }
        catch ( JWTVerificationException e){
            System.out.println("JWT Exception");
            return false;
        }
    }

}
