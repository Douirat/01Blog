package com.blog.backend.dtos.user;

import jakarta.validation.constraints.NotBlank; // if using Jakarta EE 10+
import lombok.AllArgsConstructor;
import lombok.Data;


@Data // Lombok annotation to generate getters, setters, toString, etc.
// this the data structure frontend sends with server controlled data flow.
@AllArgsConstructor
public class LoginRequestDTO {
    @NotBlank
    private String emailOrUsername;

    @NotBlank
    private String password;
}
