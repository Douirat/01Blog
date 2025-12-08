package com.blog.backend.services.profile;

import com.blog.backend.dto.PaginatedUsersDTO;
import org.springframework.data.domain.Page;

public interface ProfileService {
Page<PaginatedUsersDTO> fetchUsers(int page);
}