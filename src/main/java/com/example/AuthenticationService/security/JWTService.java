package com.example.AuthenticationService.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.AuthenticationService.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class JWTService {

//    @Value("${spring.jwt-secret-key}")
//    private final String secretKey;

    private final Algorithm algorithm;

    public JWTService(@Value("${spring.jwt-secret-key}") String secretKey){
//        this.secretKey = secretKey;
        this.algorithm = Algorithm.HMAC256(secretKey.getBytes());

    }


    public String createToken(UserDto userDto, String sessionID){

        String token = JWT.create()
                .withSubject("userSession")
                .withClaim("userID", userDto.getEmail())
                .withClaim("sessionID", sessionID)
                .sign(algorithm);

        System.out.println(sessionID);

        return token;
    }

    public Map<String, Claim> getValuesToken(String token){
        DecodedJWT decodedJWT;

//        try {
        JWTVerifier verifier = JWT.require(algorithm)
                .withSubject("userSession")
                .build();

        decodedJWT = verifier.verify(token);

        return decodedJWT.getClaims();
//        }
//        catch (JWTVerificationException e){
//            System.out.println("JWT Exception");
//        }
    }

}
