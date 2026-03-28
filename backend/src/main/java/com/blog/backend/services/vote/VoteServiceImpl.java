package com.blog.backend.services.vote;

import com.blog.backend.models.vote.Vote;
import com.blog.backend.models.post.Post;
import com.blog.backend.models.user.User;
import com.blog.backend.repositories.vote.VoteRepository;
import com.blog.backend.repositories.post.PostRepository;
import com.blog.backend.repositories.user.UserRepository;
import org.springframework.stereotype.Service;
import com.blog.backend.dtos.vote.VoteResponseDTO;
import com.blog.backend.dtos.post.PostDetailDTO;
import org.springframework.transaction.annotation.Transactional;
import com.blog.backend.dtos.vote.VoteRequestDTO;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // public void VoteService(VoteRepository voteRepository, PostRepository
    // postRepository,
    // UserRepository userRepository) {
    // this.voteRepository = voteRepository;
    // this.postRepository = postRepository;
    // this.userRepository = userRepository;
    // }

    @Transactional
    public VoteResponseDTO toggleVote(VoteRequestDTO vote) {

        Post post = postRepository.findById(vote.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(vote.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Vote> existingVoteOpt = voteRepository.findByPostAndUser(post, user);
        String message;
        Long voteId = null;

        if (existingVoteOpt.isPresent()) {

            Vote existingVote = existingVoteOpt.get();
            voteId = existingVote.getId();

            if (existingVote.isLiked() == vote.isValue()) {
                voteRepository.delete(existingVote);
                message = "Vote removed successfully (toggled off).";
            } else {
                existingVote.setLiked(vote.isValue());
                voteRepository.save(existingVote);
                message = "Vote changed successfully.";
            }

        } else {
            Vote newVote = new Vote(post, user, vote.isValue());
            voteRepository.save(newVote);
            message = "New vote successfully cast.";
        }

        int likes = (int) post.getVotes().stream().filter(Vote::isLiked).count();
        int dislikes = (int) post.getVotes().stream().filter(v -> !v.isLiked()).count();

        System.out.printf("The number of likes is: %d and the number of dilikes is: %d\n", likes, dislikes);
        PostDetailDTO postDTO = new PostDetailDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getMediaType(),
                post.getMediaUrl(),
                null,
                likes,
                dislikes,
                0,
                post.getCreatedAt(),
                post.isBanned()
            );
        return new VoteResponseDTO(postDTO, true, message);
    }
}