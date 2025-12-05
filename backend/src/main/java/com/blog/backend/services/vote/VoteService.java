package com.blog.backend.services.vote;

import com.blog.backend.models.vote.Vote;
import com.blog.backend.models.post.Post;
import com.blog.backend.models.user.User;

public interface VoteService {
public boolean toggleVote(Long postId, User user, boolean vote);
}