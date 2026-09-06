package com.example.maintenance.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class HealthController {
    @GetMapping("/api/health")
    public String healthTest(){
        return "Smart Maintenance Api is running";
    }

    @GetMapping("/api/about")
    public String about()
    {
        return "This is Smart maintenance platfrom";
    }
}
