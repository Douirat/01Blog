package com.blog.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.blog.backend.model.User;
import com.blog.backend.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
   

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        return userService.registerUser(user)
                .map(registeredUser -> ResponseEntity.ok().body(registeredUser))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

}