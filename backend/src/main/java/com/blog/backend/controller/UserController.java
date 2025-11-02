package com.blog.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.blog.backend.dto.AuthResponseDTO;
import com.blog.backend.model.User;
import com.blog.backend.service.UserService;

import jakarta.validation.Valid;

import com.blog.backend.dto.LoginRequestDTO;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
   

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> registerUser(@RequestBody User user) {
        return userService.registerUser(user)
                .map(registeredUser -> ResponseEntity.ok().body(registeredUser))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> loginUser(@Valid @RequestBody  LoginRequestDTO payload) {
        return userService.loginUser(payload)
                .map(authResponse -> ResponseEntity.ok().body(authResponse))
                .orElseGet(() -> ResponseEntity.status(401).build());
    }

    // logout can be handled client-side by deleting the JWT token.
    // If server-side invalidation is needed, a token blacklist can be implemented.
    @DeleteMapping("/logout")
    public ResponseEntity<Void> logoutUser() {
        // In a stateless JWT setup, logout is typically handled on the client 
        // by deleting the token. Here, we just return a success response.
        return ResponseEntity.ok().build();
    }

    // An endpoint to check the authentication status using the JWT token:
    @GetMapping
    public ResponseEntity<AuthResponseDTO> checkStatus( @RequestHeader("Authorization") String authHeader) {
        return userService.checkStatus(authHeader.replace("Bearer ", ""))
                .map(authResponse -> ResponseEntity.ok().body(authResponse))
                .orElseGet(() -> ResponseEntity.status(401).build());
    }
}