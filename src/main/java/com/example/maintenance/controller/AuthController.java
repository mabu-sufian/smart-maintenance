package com.example.maintenance.controller;


import com.example.maintenance.dto.LoginRequestDTO;
import com.example.maintenance.dto.LoginResponseDTO;
import com.example.maintenance.dto.RegisterRequestDTO;
import com.example.maintenance.dto.RegisteredResponseDTO;
import com.example.maintenance.entity.User;
import com.example.maintenance.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService)
    {
        this.authService=authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisteredResponseDTO>register(@Valid @RequestBody RegisterRequestDTO requestDTO)
    {
        RegisteredResponseDTO responseDTO=authService.register(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO requestDTO)
    {
        LoginResponseDTO responseDTO=authService.Login(requestDTO);

        return ResponseEntity
                .ok()
                .body(responseDTO);
    }
}
