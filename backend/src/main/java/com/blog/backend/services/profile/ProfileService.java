package com.blog.backend.services.profile;

import com.blog.backend.dtos.user.UserDTO;
import org.springframework.data.domain.Page;

public interface ProfileService {
Page<UserDTO> fetchUsers(int page);
UserDTO fetchUserProfile(Long userId);
}