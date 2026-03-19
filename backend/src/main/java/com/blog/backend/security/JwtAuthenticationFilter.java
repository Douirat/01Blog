package com.blog.backend.security;

import com.blog.backend.util.JwtUtil;
import com.blog.backend.security.PrincipalUser;
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

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

        System.out.println(">>> [JWT Filter] URI: " + request.getRequestURI());
        System.out.println(">>> [JWT Filter] Auth header: " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            System.out.println(">>> [JWT Filter] Token length: " + token.length());
            System.out.println(">>> [JWT Filter] validateToken: " + jwtUtil.validateToken(token));

            try {
                if (jwtUtil.validateToken(token)) {
                    Claims claims = jwtUtil.getClaims(token);

                    String email = claims.getSubject();

                    Object idClaim = claims.get("id");
                    if (idClaim == null) {
                        throw new IllegalArgumentException("Missing 'id' claim in JWT");
                    }
                    Long userId = Long.valueOf(idClaim.toString());

                    Object rawRoles = claims.get("roles");
                    System.out.println(">>> [JWT Filter] Raw roles: " + rawRoles);

                    List<String> roles = rawRoles instanceof List<?>
                            ? ((List<?>) rawRoles).stream()
                                    .filter(r -> r instanceof String)
                                    .map(Object::toString)
                                    .collect(Collectors.toList())
                            : List.of();

                    Collection<? extends GrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    System.out.println(">>> [JWT Filter] Authorities: " + authorities);

                    PrincipalUser principal = new PrincipalUser(userId, email, authorities);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    System.out.println(">>> [JWT Filter] Auth set successfully for: " + email);

                } else {
                    System.out.println(">>> [JWT Filter] validateToken returned FALSE — token rejected");
                }

            } catch (Exception e) {
                System.out.println(">>> [JWT Filter] Exception: " + e.getClass().getName() + " — " + e.getMessage());
            }

        } else {
            System.out.println(">>> [JWT Filter] No Bearer token — skipping auth");
        }

        filterChain.doFilter(request, response);
    }
}