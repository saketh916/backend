package com.example.backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    // Create token (just like jwt.sign({email, id}, SECRET))
    public String createToken(String email) {
        return Jwts.builder()
                .setSubject(email)                       // token content
                .setIssuedAt(new Date())                 // now
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24h
                .signWith(SignatureAlgorithm.HS256, secretKey) // sign token
                .compact();
    }

    // Read token (like jwt.verify)
    public String decodeEmail(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // gives us the email
    }
}
