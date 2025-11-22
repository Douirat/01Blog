package com.blog.backend.service;

import java.io.IOException;

import javax.annotation.PostConstruct;
import org.springframework.web.multipart.MultipartFile;

@service
public class FileStorageServiceImpl {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Value("${file.base-url:http://localhost:8080}")
    private String baseUrl;

    @PostConstruct
    public void init() {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);

        }
    }

    /**
     * Save a file to a specific subdiretory:
     * 
     * @param file         the file to save.
     * @param subdirectory (e.g, "avatars", "post-media", "comments-media").
     * @param allowedTypes array of allowed MIME types.
     * @return full URL to the saved file.
     */
    public String saveFile(MultipartFile file, String subDirectory, String[] allowedTypes) {

    }

    /**
     * Delete a file by Url:
     */
    public void deleteFile(String fileUrl){

    }

    /**
     * Validate file against allowed types:
     */
    private void validateFile(MultipartFile file, String[] allowedTypes){
        
    }
}