package com.blog.backend.dto;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private UserDTO user;
    private String token;
}
