package com.blog.backend.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import com.blog.backend.repository.UserRepository;
import com.blog.backend.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public interface UserService {
    // Define service methods here, e.g., createUser, getUserById, etc.
    Optional<User> registerUser(User user);
    // Optional<User> loginUser(String email, String password);
    // Optional<User> logoutUser(Long userId);
    // Optional<User> getUserByEmail(String email);
}

@Service
class UserServiceImpl implements UserService {

    // You would typically inject UserRepository here
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> registerUser(User user) {
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
        newUser.setDateOfBirth(user.getDateOfBirth());
        newUser.setAvatar(user.getAvatar());
        newUser.setNickname(user.getNickname());
        newUser.setAdmin(false); // default to normal user

        return Optional.of(userRepository.save(newUser));

    }

    // @Override
    // public Optional<User> loginUser(String email, String password) {
    // // Implementation for logging in a user
    // return Optional.empty();
    // }

    // @Override
    // public Optional<User> logoutUser(Long userId) {
    // // Implementation for logging out a user
    // return Optional.empty();
    // }

    // @Override
    // public Optional<User> getUserByEmail(String email) {
    // // Implementation for retrieving a user by email
    // return Optional.empty();
    // }
}