package com.blog.backend.services.comment;

import com.blog.backend.exceptions.ResourceNotFoundException;
import com.blog.backend.models.comment.Comment;
import com.blog.backend.dtos.comment.CommentDTO;
import com.blog.backend.dtos.comment.CommentDetailsDTO;
import com.blog.backend.models.post.Post;
import com.blog.backend.models.user.User;

import org.springframework.transaction.annotation.Transactional;


import com.blog.backend.repositories.post.PostRepository;
import com.blog.backend.repositories.user.UserRepository;
import com.blog.backend.repositories.comment.CommentRepository;
import org.springframework.stereotype.Service;
import com.blog.backend.exceptions.ResourceNotFoundException; // If you added this custom exception
import com.blog.backend.mappers.comment.CommentMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;

@Service
public class CommentServiceImpl implements CommentService {

        private final CommentRepository commentRepository;
        private final PostRepository postRepository;
        private final UserRepository userRepository;
        private final CommentMapper commentMapper;

        public CommentServiceImpl(CommentRepository commentRepository,
                        PostRepository postRepository,
                        UserRepository userRepository,
                        CommentMapper commentMapper) {
                this.commentRepository = commentRepository;
                this.postRepository = postRepository;
                this.userRepository = userRepository;
                this.commentMapper = commentMapper;
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

        /**
         * Fetches a page of comments for a specific post.
         * 
         * @param page   The page number (0-indexed).
         * @param postId The ID of the post.
         * @return A Page of CommentDetailsDTOs.
         */
        @Override
        @Transactional(readOnly = true)
        public Page<CommentDetailsDTO> getPostComments(int page, Long postId) {
                int pageSize = 10;
                Pageable pageable = PageRequest.of(page, pageSize);
                // Fetch comments for the post
                Page<Comment> commentPage = commentRepository.findByPostId(postId, pageable);

                // Map Comment entities to CommentDetailsDTOs using MapStruct
                return commentPage.map(commentMapper::toDto);
        }
}
