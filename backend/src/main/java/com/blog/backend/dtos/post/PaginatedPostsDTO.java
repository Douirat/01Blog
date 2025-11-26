package com.blog.backend.dtos.post;

import java.util.List;  // ← THIS IS REQUIRED
import com.blog.backend.dtos.post.PostDetailDTO;

public record PaginatedPostsDTO(
    List<PostDetailDTO> content,
    boolean last,
    int totalPages,
    long totalElements
) {}
