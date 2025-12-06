package com.blog.backend.dtos.vote;

import lombok.Data;

@Data
public class VoteRequestDTO {
    private Long postId;
    private Long userId;
    private boolean value;
}
