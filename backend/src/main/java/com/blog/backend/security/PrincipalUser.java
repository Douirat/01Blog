package com.blog.backend.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;
import com.blog.backend.models.dtos.user.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

public class PrincipalUser implements UserDetails {

    private final Long id;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;

    public PrincipalUser(Long id, String email, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.authorities = authorities;
    }

    public static PrincipalUser fromUserDTO(UserDTO dto) {
        // Example: map isAdmin to ROLE_ADMIN or ROLE_USER
        List<SimpleGrantedAuthority> authorities = dto.isAdmin()
                ? List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                : List.of(new SimpleGrantedAuthority("ROLE_USER"));

        return new PrincipalUser(dto.getId(), dto.getEmail(), authorities);
    }

    // getters
    public Long getId() { return id; }
    public String getEmail() { return email; }

    // UserDetails methods
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public String getPassword() { return null; } // JWT used
    @Override public String getUsername() { return email; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
