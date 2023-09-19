package com.example.AuthenticationService.security;

import com.example.AuthenticationService.authentication.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

public class CookieAuthFilter extends OncePerRequestFilter {


    public final String cookieName;
    private final JWTService jwtService;

    public CookieAuthFilter(String cookieName, JWTService jwtService){
        this.cookieName = cookieName;
        this.jwtService = jwtService;
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return super.shouldNotFilter(request);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Optional<Cookie> auth = Stream.of(
                                Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst();

        if (auth.isPresent()){
            System.out.println(auth.get().getName());
            jwtService.verifyToken(auth.get().getValue());

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(auth.get().getName(), null)
            );
        }

    filterChain.doFilter(request, response);

    }
}
