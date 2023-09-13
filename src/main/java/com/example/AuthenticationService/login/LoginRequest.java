package com.example.AuthenticationService.login;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class LoginRequest {


    private final String email;
    private final String password;
}


