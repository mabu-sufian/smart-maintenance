package com.example.maintenance.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {

private  final PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

@Test
    void shouldEncodeAndVerifyPassword()
{
    String rawPassword="Shawon1234";
    String hash=passwordEncoder.encode(rawPassword);

    assertNotEquals(rawPassword,hash);
    assertTrue(passwordEncoder.matches(rawPassword,hash));
    assertFalse(passwordEncoder.matches("WrongPass123!", hash));
}
}