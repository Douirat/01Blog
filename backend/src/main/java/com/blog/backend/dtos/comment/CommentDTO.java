package com.blog.backend.dtos.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentDTO(
        @NotBlank String title,
        @NotBlank String content,
        @NotNull Long postId
) {}
