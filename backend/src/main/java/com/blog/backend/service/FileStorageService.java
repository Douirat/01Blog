package com.blog.backend.service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;




public interface FileStorageService {
    String saveFile(MultipartFile file, String subDirectory, String[] allowedTypes);
    void deleteFile(String fileUrl);
    void validateFile(MultipartFile file, String[] allowedTypes);
}