package com.blog.backend.util;

import org.springframework.stereotype.Component;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.blog.backend.dtos.user.UserDTO;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;

@Component // https://www.geeksforgeeks.org/springboot/spring-component-annotation-with-example/
public class JwtUtil {

    
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    
    
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // Generate JWT with optional custom claims
    public String generateToken(UserDTO userDTO) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        List<String> roles = userDTO.isAdmin()
                ? List.of("ROLE_ADMIN")
                : List.of("ROLE_USER");

        JwtBuilder builder = Jwts.builder()
                .setSubject(userDTO.getEmail())
                .claim("id", userDTO.getId())
                .claim("roles", roles)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256);

        return builder.compact();
    }

    // Extract all claims from token
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extract specific claim (like email)
    public String getEmail(String token) {
        return getClaims(token).getSubject();
    }

    public String extractUserId(String token) {
        return String.valueOf(getClaims(token).get("id"));
    }

    // Validate token
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // expired, malformed, tampered, etc.
        }
    }

    // get user email from token
    public String getUserEmailFromToken(String token) {
        return getEmail(token);
    }
}