package com.blog.backend.services.comment;

import com.blog.backend.exceptions.ResourceNotFoundException;
import com.blog.backend.models.comment.Comment;
import com.blog.backend.dtos.comment.CommentDTO;
import com.blog.backend.models.post.Post;
import com.blog.backend.models.user.User;
import com.blog.backend.repositories.post.PostRepository;
import com.blog.backend.repositories.user.UserRepository;
import com.blog.backend.repositories.comment.CommentRepository;
import org.springframework.stereotype.Service;
import com.blog.backend.exceptions.ResourceNotFoundException; // If you added this custom exception

import java.time.LocalDate;

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
        public Comment createComment(CommentDTO requestDTO, Long userId) {
                Post post = postRepository.findById(requestDTO.postId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Post with ID " + requestDTO.postId() + " not found"));

                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "User with ID " + userId + " not found"));

                // Using a builder pattern for cleaner instantiation
                Comment comment = new Comment();
                comment.setContent(requestDTO.content());
                comment.setPost(post);
                comment.setUser(user);
                comment.setCreatedAt(LocalDate.now());
                return commentRepository.save(comment);
        }
}
