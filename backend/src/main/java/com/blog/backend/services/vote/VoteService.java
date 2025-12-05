package com.blog.backend.services.vote;

import com.blog.backend.models.vote.Vote;
import com.blog.backend.models.post.Post;
import com.blog.backend.models.user.User;
import com.blog.backend.dtos.vote.VoteResponseDTO;
import com.blog.backend.dtos.vote.VoteRequestDTO;

public interface VoteService {
public VoteResponseDTO toggleVote(VoteRequestDTO vote);
}