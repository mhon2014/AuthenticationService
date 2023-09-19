package com.example.AuthenticationService.temp;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeTemp {

    @ResponseBody
    @RequestMapping("/")
    public String Hello(){
        return "Hello";
    }

    @ResponseBody
    @RequestMapping("/test")
    public String Test(){
        return "test";
    }

}

