package com.blog.backend.service;

import java.util.Optional;
import com.blog.backend.dto.AuthResponseDTO;
import com.blog.backend.dto.user.LoginRequestDTO;
import com.blog.backend.model.User;



public interface UserService {
    // Define service methods here, e.g., createUser, getUserById, etc.
    Optional<AuthResponseDTO> registerUser(User user);

    Optional<AuthResponseDTO> loginUser(LoginRequestDTO payload);

    Optional<AuthResponseDTO> checkStatus(String token);

}
