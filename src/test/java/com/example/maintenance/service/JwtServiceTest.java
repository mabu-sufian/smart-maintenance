package com.example.maintenance.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    private final JwtService jwtService=new JwtService("this-is-a-very-long-secret-key-for-testing-123456",
            3600000);

    @Test
    void shouldCreateAndValidateToken()
    {
        String token= jwtService.generateToken(15L,"user");
        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    void shouldExtractUserId() {

        String token =
                jwtService.generateToken(15L, "USER");

        Long userId =
                jwtService.extractUserId(token);

        assertEquals(15L, userId);
    }

    @Test
    void shouldExtractRole() {

        String token =
                jwtService.generateToken(15L, "USER");

        String role =
                jwtService.extractRole(token);

        assertEquals("USER", role);
    }

    @Test
    void shouldRejectInvalidToken() {

        String token =
                jwtService.generateToken(15L, "USER");

        String invalidToken =
                token + "abc";

        assertFalse(
                jwtService.isTokenValid(invalidToken)
        );
    }

}