package com.example.AuthenticationService.session;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sessions")
public class SessionController {

//    private final SessionService sessionService;
    private final RedisTemplate<String, String> redisTemplate;


//    @GetMapping("/all")
//    public List<Session> getAll(){
//        return sessionRepository.findAll();
//    }

    @GetMapping("/all")
    public int all(){

        //TODO: List all logins on redis
        Set<String> keys = redisTemplate.keys("*");
        return keys.size();
    }


}
