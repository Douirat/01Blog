package com.blog.backend.dto;
import jakarta.validation.constraints.NotBlank; // if using Jakarta EE 10+

public class LoginRequestDTO {
    @NotBlank
    private String emailOrUsername;

    @NotBlank
    private String password;

    // getters/setters
}
