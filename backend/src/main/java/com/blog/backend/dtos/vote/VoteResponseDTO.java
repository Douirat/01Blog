package com.blog.backend.dtos.vote;

public record VoteResponseDTO(
    Long postId,
    boolean success,
    String message
) {}
