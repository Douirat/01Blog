package com.blog.backend.dtos.comment;

import java.util.List;
import com.blog.backend.dtos.comment.CommentDetailsDTO;

public record PaginatedCommentsDTO(
    List<CommentDetailsDTO> content,
    boolean last,
    int totalPages,
    long totalElements
) {}
