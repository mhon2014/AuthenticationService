package com.example.AuthenticationService.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {

    @NotEmpty
    @Email
    private final String email;
    @NotEmpty
    @Size(min = 8, message = "password should have at least 8 characters")
    private final String password;
}
