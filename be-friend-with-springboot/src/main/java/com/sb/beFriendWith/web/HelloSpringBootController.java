package com.sb.beFriendWith.web;

import com.sb.beFriendWith.web.dto.HelloSpringBootResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloSpringBootController {

    @GetMapping("/hello")
    public String helloSB(){
        return "hello";
    }

    @GetMapping("/hello/dto")
    public HelloSpringBootResponseDto helloSBbyDto(@RequestParam("name") String name, @RequestParam("amount") int amount) {
        return new HelloSpringBootResponseDto(name, amount);
    }
}
