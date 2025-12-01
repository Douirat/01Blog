package com.blog.backend.services.comment;


import com.blog.backend.exceptions.ResourceNotFoundException;
import com.blog.backend.models.Comment;
import com.blog.backend.models.Post;    
import com.blog.backend.models.User;   
import com.blog.backend.services.post.PostRepository;
import com.blog.backend.services.user.UserRepository;
import org.springframework.stereotype.Service;
import com.blog.backend.exceptions.ResourceNotFoundException; // If you added this custom exception



import java.time.Instant; 

@Service 
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository,
                          PostRepository postRepository,
                          UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // Assuming this method is public based on the interface context
    @Override
    public Comment createComment(CommentRequestDTO requestDTO, Long userId) {
        Post post = postRepository.findById(requestDTO.postId())
                .orElseThrow(() -> new ResourceNotFoundException("Post with ID " + requestDTO.postId() + " not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + userId + " not found"));

        // Using a builder pattern for cleaner instantiation
        Comment comment = Comment.builder()
                .content(requestDTO.content())
                .post(post)
                .user(user)
                .createdAt(Instant.now())
                .build();

        return commentRepository.save(comment);
    }
}
