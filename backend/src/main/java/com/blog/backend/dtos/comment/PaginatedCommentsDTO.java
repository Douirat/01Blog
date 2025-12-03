package com.blog.backend.dtos.comment;

import java.util.List;

public record PaginatedCommentsDTO(
    List<CommentDetailsDTO> content,
    boolean last,
    int totalPages,
    long totalElements
) {}
