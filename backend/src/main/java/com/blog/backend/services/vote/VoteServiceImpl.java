package com.blog.backend.services.vote;

import com.blog.backend.models.vote.Vote;
import com.blog.backend.models.post.Post;
import com.blog.backend.models.user.User;
import com.blog.backend.services.vote.VoteService;
import com.blog.backend.repositories.vote.VoteRepository;
import com.blog.backend.repositories.post.PostRepository;
import com.blog.backend.repositories.user.UserRepository;
import org.springframework.stereotype.Service;
import com.blog.backend.dtos.vote.VoteResponseDTO;
import org.springframework.transaction.annotation.Transactional;
import com.blog.backend.dtos.vote.VoteRequestDTO;
import java.util.Optional;

import javax.management.RuntimeErrorException;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // public void VoteService(VoteRepository voteRepository, PostRepository postRepository,
    //         UserRepository userRepository) {
    //     this.voteRepository = voteRepository;
    //     this.postRepository = postRepository;
    //     this.userRepository = userRepository;
    // }

    @Transactional
    public VoteResponseDTO toggleVote(VoteRequestDTO vote) {

        Post post = postRepository.findById(vote.postId())
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(vote.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Vote> existingVoteOpt = voteRepository.findByPostAndUser(post, user);
        String message;
        Long voteId = null;

        if (existingVoteOpt.isPresent()) {

            Vote existingVote = existingVoteOpt.get();
            voteId = existingVote.getId();

            if (existingVote.isLiked() == vote.value()) {
                voteRepository.delete(existingVote);
                message = "Vote removed successfully (toggled off).";
            } else {
                existingVote.setLiked(vote.value());
                voteRepository.save(existingVote);
                message = "Vote changed successfully.";
            }

        } else {
            Vote newVote = new Vote(post, user, vote.value());
            voteRepository.save(newVote);
            message = "New vote successfully cast.";
        }
        return new VoteResponseDTO(voteId, true, message);
    }
}