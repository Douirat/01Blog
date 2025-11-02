package com.blog.backend.util;

import org.springframework.stereotype.Component;

import com.blog.backend.dto.UserDTO;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;


@Component
public class JwtUtil {
    // @Value("${jwt.secret}")
    // private String jwtSecret;

    // @Value("${jwt.expiration}")
    // private Long jwtExpirationInMs;
        // Directly in the class
    private final String SECRET = "toBeOrNotToBeThatIsTheQuestionAnd01blog06-09-1991";
    private final long EXPIRATION = 86400000; // 1 day in milliseconds

    // Generate a signing key
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    // Generate JWT with optional custom claims
    public String generateToken(UserDTO userDTO) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION);

        JwtBuilder builder = Jwts.builder()
                .setSubject(userDTO.getEmail())
                .claim("id", userDTO.getId())
                .claim("isAdmin", userDTO.isAdmin())
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