package com.blog.backend.dtos.comment;

public record CommentResponseDTO(
    Long id,
    boolean success,
    String message
) {}
