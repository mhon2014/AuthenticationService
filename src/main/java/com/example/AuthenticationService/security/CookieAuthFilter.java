package com.example.AuthenticationService.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;


public class CookieAuthFilter extends OncePerRequestFilter {


    public final String cookieName;

    public final JWTService jwtService;

    public CookieAuthFilter(String cookieName, JWTService jwtService){
        this.cookieName = cookieName;
        this.jwtService = jwtService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        //TODO:add the resources for the signin singout and signup
        String uri = request.getRequestURI();
        System.out.println(uri);
        return uri.equals("user/verify");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Get Cookie from the request
        Optional<Cookie> auth = Stream.of(
                                Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst();

        if (auth.isPresent() && !auth.get().getValue().isEmpty()){
            // If there is a cookie then proceed with verification

            try {
                Map<String, Claim> claims = jwtService.getValuesToken(auth.get().getValue());

                claims.entrySet().forEach(x -> System.out.println(x.toString()));
                //TODO: check if sessionID and user ID matches

                //Continue the process
                filterChain.doFilter(request, response);
            }
            catch (JWTDecodeException e){
                response.setStatus(HttpStatus.FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }

            Boolean verified = false;

            // TODO: verify that the sessionID retrieved is in the service

            if(verified) {
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(auth.get().getName(), null)
                );
            }

//
        }
        else {
            //if there is no cookies, proceed with filters
            filterChain.doFilter(request, response);
        }
    }
}
