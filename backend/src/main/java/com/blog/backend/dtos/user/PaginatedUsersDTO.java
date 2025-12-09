package com.blog.backend.dtos.user;

import java.util.List;
import com.blog.backend.dtos.user.UserDTO;

public record PaginatedUsersDTO(
    List<UserDTO> content,
    boolean last,
    int totalPages,
    long totalElements
) {}
