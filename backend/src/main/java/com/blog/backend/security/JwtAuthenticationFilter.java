package com.blog.backend.security;

import com.blog.backend.util.JwtUtil;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.GrantedAuthority;
import java.util.stream.Collectors;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                if (jwtUtil.validateToken(token)) {
                    Claims claims = jwtUtil.getClaims(token);
                    String email = claims.getSubject();
                    Long userId = Long.valueOf(claims.get("id").toString());

                    // ⬅ FIXED: Extract roles from JWT
                    List<String> roles = (List<String>) claims.get("roles");

                    // Extract roles from JWT
                    Collection<? extends GrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    // Create PrincipalUser
                    PrincipalUser principal = new PrincipalUser(userId, email, authorities);

                    // authenticate the user
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            principal, null, principal.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    System.out.println("JWT Filter passed: email=" + email + ", userId=" + userId);
                }
            } catch (Exception e) {
                System.out.println("Invalid JWT: " + e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
}
/**
 * 
 * In controllers/services, you can safely do:
 * 
 * PrincipalUser currentUser = (PrincipalUser) SecurityContextHolder
 * .getContext()
 * .getAuthentication()
 * .getPrincipal();
 * 
 * boolean isAdmin = currentUser.getAuthorities().stream()
 * .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
 */