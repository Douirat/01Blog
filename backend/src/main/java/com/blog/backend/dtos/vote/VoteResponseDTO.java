package com.blog.backend.dtos.vote;

import com.blog.backend.dtos.post.PostDetailDTO;

public record VoteResponseDTO(
    PostDetailDTO postDTO,
    boolean success,
    String message
) {}
