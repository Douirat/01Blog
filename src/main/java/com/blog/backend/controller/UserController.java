package com.blog.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.blog.backend.dto.AuthResponseDTO;
import com.blog.backend.model.User;
import com.blog.backend.service.UserService;
import com.blog.backend.dto.LoginRequestDTO;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
   

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<AuthResponseDTO> registerUser(@RequestBody User user) {
        return userService.registerUser(user)
                .map(registeredUser -> ResponseEntity.ok().body(registeredUser))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping
    public ResponseEntity<AuthResponseDTO> loginUser(@RequestBody  LoginRequestDTO payload) {
        return userService.loginUser(payload)
                .map(loggedUser -> ResponseEntity.ok.body(loggedUser))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }


}