package com.blog.backend.services.vote;

import com.blog.backend.models.vote.Vote;
import com.blog.backend.models.post.Post;
import com.blog.backend.models.user.User;
import com.blog.backend.services.vote.VoteService;
import com.blog.backend.repositories.vote.VoteRepository;
import com.blog.backend.repositories.post.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

import javax.management.RuntimeErrorException;

@Service
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;

    public VoteService(VoteRepository voteRepository, PostRepository postRepository) {
        this.voteRepository = voteRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public boolean toggleVote(Long postId, User user, boolean vote) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> RuntimeException("Post not found with ID: " + postId));

        Optional<Vote> existingVoteOpt = voteRepository.findByPostAndUser(post, user);
        if (existingVoteOpt.isPresent()) {

            Vote existingVote = existingVoteOpt.get();
            if (existingVote.isLiked() == vote) {
                voteRepository.delete(existingVote);
                return false;
            } else {
                existingVote.setLiked(isLiking);
                voteRepository.save(existingVote);
                return true;
            }

        } else {
            Vote newVote = new Vote(post, user, isLiking);
            voteRepository.save(newVote);
            return true;
        }
    }
}