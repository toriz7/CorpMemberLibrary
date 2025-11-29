package com.david.CorpMemberLibrary.web;

import org.springframework.web.bind.annotation.GetMapping;

import static org.junit.jupiter.api.Assertions.*;

class HelloControllerTest {

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}