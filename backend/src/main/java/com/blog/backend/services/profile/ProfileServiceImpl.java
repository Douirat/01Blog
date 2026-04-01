package com.blog.backend.services.profile;

import com.blog.backend.services.profile.ProfileService;

import jakarta.transaction.Transactional;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import com.blog.backend.dtos.user.PaginatedUsersDTO;
import com.blog.backend.dtos.user.UserDTO;
import com.blog.backend.models.user.User;
import com.blog.backend.repositories.user.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<UserDTO> fetchUsersContains(int page, String value) {
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.searchByName(value, pageable);
        return users.map(user -> new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getAvatar(),
                user.getNickname(),
                user.getDateOfBirth(),
                user.isAdmin(),
                user.isBanned()));
    }

    @Override
    public Page<UserDTO> fetchUsers(int page) {
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.findAll(pageable);
        return users.map(user -> new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getAvatar(),
                user.getNickname(),
                user.getDateOfBirth(),
                user.isAdmin(),
                user.isBanned()));
    }

    @Override
    public UserDTO fetchUserProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getAvatar(),
                user.getNickname(),
                user.getDateOfBirth(),
                user.isAdmin(),
                user.isBanned());
    }

    @Transactional
    public boolean banUser(long userId) {
        Optional<User> optUser = this.userRepository.findById(userId);
        if (optUser.isEmpty())
            return false;
        User user = optUser.get();
        if (user.isBanned())
            return true;
        user.setBanned(true);
        return true;
    }
}