package com.blog.backend.services.post;

import com.blog.backend.models.post.Post;
import com.blog.backend.dtos.post.PostDetailDTO;
import com.blog.backend.dtos.post.PostInputDTO;
import com.blog.backend.dtos.user.UserDTO;
import org.springframework.data.domain.Page;

public interface PostService {

    Page<PostDetailDTO> getAllPosts(Long userId, int page);

    Page<PostDetailDTO> getUserPosts(int page, long userId);

    Page<PostDetailDTO> getReportedPosts(int page, long userId);

    Post createPost(Long userId, PostInputDTO post);

    Post updatePost(Long userId, Long postId, PostInputDTO dto);;

    UserDTO getUserByPostId(long postId);

    boolean banPost(long id);

    boolean unbanPost(long id);

    boolean deletePost(long postId);

}
