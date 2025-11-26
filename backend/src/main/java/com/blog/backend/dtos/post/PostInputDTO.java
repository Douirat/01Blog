package com.blog.backend.dtos.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostInputDTO {
    private String title;
    private String content;
    private String mediaType;
    private MultipartFile media;
}
