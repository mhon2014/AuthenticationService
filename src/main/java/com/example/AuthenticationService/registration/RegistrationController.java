package com.example.AuthenticationService.registration;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    //map register function to the uri and use the body of the request
    @PostMapping("/signup")
    public String register(@RequestBody RegistrationRequest request){
        System.out.println(request.getPassword());
        return registrationService.register(request);
    }

    @GetMapping("/verify")
    public String verify(@RequestParam("token") String token){

        return registrationService.verifyToken(token);
    }

}
