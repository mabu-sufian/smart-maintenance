package com.example.maintenance.dto;

import com.example.maintenance.entity.Enum.Role;

public class LoginResponseDTO {
   String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LoginResponseDTO(String token) {
        this.token = token;
    }
}
