package com.blog.backend.dtos.vote;

public record VoteRequestDTO(
Long postId,
Long userId,
boolean value
) {}
