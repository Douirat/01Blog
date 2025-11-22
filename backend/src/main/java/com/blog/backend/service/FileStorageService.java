package com.blog.backend.service;

@service
public interface FileStorageService {
    String saveFile(MultipartFile file, String subDirectory, String[] allowedTypes);
    void deleteFile(String fileUrl);
    void validateFile(MultipartFile file, String[] allowedTypes);
    
}