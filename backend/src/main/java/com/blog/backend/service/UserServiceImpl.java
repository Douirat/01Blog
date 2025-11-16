package com.blog.backend.service;

import org.springframework.stereotype.Service;
import com.blog.backend.repository.UserRepository;
import com.blog.backend.dto.AuthResponseDTO;
import com.blog.backend.dto.user.UserDTO;
import com.blog.backend.dto.user.LoginRequestDTO;

import com.blog.backend.model.User;
import com.blog.backend.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

@Service
class UserServiceImpl implements UserService {

    // You would typically inject UserRepository here
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final JwtUtil jwtUtil;

    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.jwtUtil = new JwtUtil();
        this.encoder = encoder;
    }

    @Override
    public Optional<AuthResponseDTO> registerUser(User user) {
        // Implementation for registering a user
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return Optional.empty(); // User already exists
        }
        if (user.getEmail() == null || user.getPassword() == null || user.getFirstName() == null
                || user.getLastName() == null || user.getNickname() == null || user.getDateOfBirth() == null) {
            return Optional.empty(); // Required fields are missing
        }

        User newUser = new User();
        newUser.setEmail(user.getEmail());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        newUser.setPassword(encoder.encode(user.getPassword()));
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setDateOfBirth(user.getDateOfBirth()); // .atStartOfDay()
        newUser.setAvatar(user.getAvatar());
        newUser.setNickname(user.getNickname());
        newUser.setAdmin(false); // default to normal user

        User savedUser = userRepository.save(newUser);
        UserDTO userDTO = new UserDTO(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getAvatar(),
                savedUser.getNickname(),
                savedUser.getDateOfBirth(),
                savedUser.isAdmin());

        String token = jwtUtil.generateToken(userDTO);

        AuthResponseDTO authResponse = new AuthResponseDTO();
        authResponse.setUser(userDTO);
        authResponse.setToken(token);

        return Optional.of(authResponse);
    }

    @Override
    public Optional<AuthResponseDTO> loginUser(LoginRequestDTO payload) {
        Optional<User> userOpt = userRepository.findByEmail(payload.getEmailOrUsername())
                .or(() -> userRepository.findByNickname(payload.getEmailOrUsername()));

        if (userOpt.isEmpty() || !encoder.matches(payload.getPassword(), userOpt.get().getPassword())) {
            return Optional.empty(); // User not found or password mismatch
        }

        return userOpt.map(user -> {
            UserDTO userDTO = new UserDTO(
                    user.getId(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getAvatar(),
                    user.getNickname(),
                    user.getDateOfBirth(),
                    user.isAdmin());

            String token = jwtUtil.generateToken(userDTO);

            AuthResponseDTO authResponse = new AuthResponseDTO();
            authResponse.setUser(userDTO);
            authResponse.setToken(token);

            return authResponse;
        });
    };

    // @Override
    // public Optional<User> logoutUser(Long userId) {
    // // Implementation for logging out a user
    // return Optional.empty();
    // }

    @Override
    public Optional<AuthResponseDTO> checkStatus(String token) {
        // the request will uphold the JWT token in the Authorization header as a Bearer
        if(!jwtUtil.validateToken(token)) {
            return Optional.empty();
        }
        String email = jwtUtil.getUserEmailFromToken(token);
        Optional<User> userOpt = userRepository.findByEmail(email);
        return userOpt.map(user -> {
            UserDTO userDTO = new UserDTO(
                    user.getId(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getAvatar(),
                    user.getNickname(),
                    user.getDateOfBirth(),
                    user.isAdmin());

            String newToken = jwtUtil.generateToken(userDTO);

            AuthResponseDTO authResponse = new AuthResponseDTO();
            authResponse.setUser(userDTO);
            authResponse.setToken(newToken);

            return authResponse;
        });
    }
}