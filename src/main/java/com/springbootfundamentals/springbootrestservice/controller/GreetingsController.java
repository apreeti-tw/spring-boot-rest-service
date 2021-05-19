package com.springbootfundamentals.springbootrestservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingsController {
    @Autowired
    Greetings greetings;

    AtomicLong aLong = new AtomicLong();

    @GetMapping("/greetings")
    public Greetings getGreeting(@RequestParam String name){
        greetings.setId(aLong.incrementAndGet());
        greetings.setContext("Hi "+name+"!!!!");
        return greetings;
    }
}
