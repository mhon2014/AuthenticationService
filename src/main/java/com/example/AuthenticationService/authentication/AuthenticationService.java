package com.example.AuthenticationService.authentication;

import com.example.AuthenticationService.dto.UserDto;
import com.example.AuthenticationService.email.EmailValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    //Holds the authentications from the DB
    private final AuthenticationManager authenticationManager;

//    private final SessionService sessionService;
    private final RedisTemplate<?, ?> redisTemplate;

    private final JWTService jwtService;



    public String signIn(UserDto userDto) throws Exception {

        UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                userDto.getEmail(), userDto.getPassword()
        );

        Authentication user = authenticationManager.authenticate(userToken);
        SecurityContext context;
        String token;

        if (user.isAuthenticated()) {
            //use context to avoid race conditions, ie thread accessing the same data
            context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(userToken);
//            STORE SESSION TOKEN ON REDIS DB

            token = jwtService.createToken(userDto);
            return token;
        } else {
            throw new IllegalStateException("Not Authenticated");
        }

    }

    public void signOut() {

        //invalidate the token.
        SecurityContextHolder.clearContext();
    }
    //get header ->jwt, hs256
    //get payload -> user + password
    //encode both first
    // then use secret key with the hash algorithm
//    HMACSHA256(
//    base64UrlEncode(header) + '.' +
//    base64UrlEncode(payload),
//    secret)
}