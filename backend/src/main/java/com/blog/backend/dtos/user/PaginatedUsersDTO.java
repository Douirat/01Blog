package com.blog.backend.dtos.user;

import java.util.List;


public record PaginatedUsersDTO(
    List<UserDTO> content,
    boolean last,
    int totalPages,
    long totalElements
) {}
