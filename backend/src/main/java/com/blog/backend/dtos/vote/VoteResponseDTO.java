package com.blog.backend.dtos.vote;

public record VoteResponseDTO(
    Long id,
    boolean success,
    String message
) {}
