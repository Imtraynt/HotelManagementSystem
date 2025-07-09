package com.example.HotelManagementSystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api")
public class roomController {

@GetMapping("/test")
    public String getTest(String test){
    return ("Hello world");
}

}
