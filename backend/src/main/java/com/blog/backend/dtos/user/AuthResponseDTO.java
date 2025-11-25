package com.blog.backend.dtos.user;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.blog.backend.dtos.user.UserDTO;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {
    private UserDTO user;
    private String token;
}
