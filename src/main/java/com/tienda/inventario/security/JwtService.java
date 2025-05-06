package com.tienda.inventario.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        return false;
    }

    public String extractUsername(String jwt) {
        return jwt;
    }
}
