package com.blog.backend.services.user;

import org.springframework.stereotype.Service;
import com.blog.backend.repositories.user.UserRepository;
import com.blog.backend.dtos.user.AuthResponseDTO;
import com.blog.backend.dtos.user.UserDTO;
import com.blog.backend.dtos.user.LoginRequestDTO;
import com.blog.backend.dtos.user.UserRegistrationDTO;
import com.blog.backend.models.user.User;
import com.blog.backend.util.JwtUtil;
import com.blog.backend.services.file.FileStorageService;
import com.blog.backend.constants.FileTypeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

@Service
class UserServiceImpl implements UserService {

    private final FileStorageService fileStorage;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(FileStorageService fileStorage,
            UserRepository userRepository,
            JwtUtil jwtUtil,
            PasswordEncoder encoder) {
        this.fileStorage = fileStorage;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.encoder = encoder;
    }

    @Override
    public Optional<AuthResponseDTO> registerUser(UserRegistrationDTO user) {
        // Implementation for registering a user
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return Optional.empty(); // User already exists
        }
        if (user.getEmail() == null || user.getPassword() == null || user.getFirstName() == null
                || user.getLastName() == null || user.getNickname() == null || user.getDateOfBirth() == null) {
            return Optional.empty(); // Required fields are missing
        }

        String avatarPath = null;
        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            avatarPath = fileStorage.saveFile(user.getAvatar(), FileTypeConstants.AVATAR_DIR,
                    FileTypeConstants.IMAGE_TYPES);
        } else {
            avatarPath = "http://localhost:8080/uploads/default-avatar.jpg";
        }

        User newUser = new User();
        newUser.setEmail(user.getEmail());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        newUser.setPassword(encoder.encode(user.getPassword()));
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setDateOfBirth(user.getDateOfBirth());
        newUser.setAvatar(avatarPath);
        newUser.setNickname(user.getNickname());
        // Determine the admin by first insertion:
        if (userRepository.count() == 0) {
            newUser.setAdmin(true);
        } else {
            newUser.setAdmin(false);
        }

        User savedUser = userRepository.save(newUser);
        UserDTO userDTO = new UserDTO(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getAvatar(),
                savedUser.getNickname(),
                savedUser.getDateOfBirth(),
                savedUser.isAdmin(),
                savedUser.isBanned());

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
        User u = userOpt.get();
        // Check if user is banned
        if (u.isBanned()) {
            throw new IllegalStateException("User is banned and cannot log in.");
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
                    user.isAdmin(),
                    user.isBanned());

            String token = jwtUtil.generateToken(userDTO);

            AuthResponseDTO authResponse = new AuthResponseDTO();
            authResponse.setUser(userDTO);
            authResponse.setToken(token);

            return authResponse;
        });
    };

    @Override
    public Optional<AuthResponseDTO> checkStatus(String token) {
        // the request will uphold the JWT token in the Authorization header as a Bearer
        if (!jwtUtil.validateToken(token)) {
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
                    user.isAdmin(),
                    user.isBanned());

            String newToken = jwtUtil.generateToken(userDTO);

            AuthResponseDTO authResponse = new AuthResponseDTO();
            authResponse.setUser(userDTO);
            authResponse.setToken(newToken);

            return authResponse;
        });
    }

}