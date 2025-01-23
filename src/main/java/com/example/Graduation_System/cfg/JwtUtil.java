package com.example.Graduation_System.cfg;

import com.example.Graduation_System.cfg.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final JwtService jwtService;

    @Autowired
    public JwtUtil(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public String generateTokenForUser(String username, String roles) {
        return jwtService.generateToken(username, roles);
    }
}
