package com.blog.backend.services.user;

import java.util.Optional;
import com.blog.backend.dtos.user.AuthResponseDTO;
import com.blog.backend.dtos.user.UserRegistrationDTO;
import com.blog.backend.dtos.user.LoginRequestDTO;

public interface UserService {

    Optional<AuthResponseDTO> registerUser(UserRegistrationDTO user);

    Optional<AuthResponseDTO> loginUser(LoginRequestDTO payload);

    Optional<AuthResponseDTO> checkStatus(String token);

    boolean banUser(long userId);
}
