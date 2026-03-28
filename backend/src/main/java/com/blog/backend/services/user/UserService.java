package com.blog.backend.services.user;

import java.util.Optional;
import com.blog.backend.dtos.user.AuthResponseDTO;
import com.blog.backend.dtos.user.UserRegistrationDTO;
import com.blog.backend.dtos.user.LoginRequestDTO;
import com.blog.backend.models.user.User;
import org.springframework.web.multipart.MultipartFile;



public interface UserService {
    // Define service methods here, e.g., createUser, getUserById, etc.
    Optional<AuthResponseDTO> registerUser(UserRegistrationDTO user);

    Optional<AuthResponseDTO> loginUser(LoginRequestDTO payload);

    Optional<AuthResponseDTO> checkStatus(String token);

    

}
