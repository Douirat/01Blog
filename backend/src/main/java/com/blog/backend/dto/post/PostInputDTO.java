package com.blog.backend.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostInputDTO {
    private String title;
    private String content;
    private String mediaType;
    private MultipartFile media;
}
