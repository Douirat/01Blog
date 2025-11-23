package com.blog.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
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
     * Save a file to a specific subdirectory
     * @param file The file to save
     * @param subDirectory Subdirectory (e.g., "avatars", "post-media", "comment-media")
     * @param allowedTypes Array of allowed MIME types
     * @return Full URL to the saved file
     */
    public String saveFile(MultipartFile file, String subDirectory, String[] allowedTypes) {
        // Validation
        validateFile(file, allowedTypes);

        try {
            // Create subdirectory path
            Path subDirPath = Paths.get(uploadDir, subDirectory);
            if (!Files.exists(subDirPath)) {
                Files.createDirectories(subDirPath);
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            String filename = UUID.randomUUID() + extension;

            // Save file
            Path filePath = subDirPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Return full URL
            return String.format("%s/%s/%s/%s", baseUrl, uploadDir, subDirectory, filename);

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + file.getOriginalFilename(), e);
        }
    }

    /**
     * Delete a file by its URL
     */
    public void deleteFile(String fileUrl) {
        try {
            // Extract relative path from URL
            String relativePath = fileUrl.replace(baseUrl + "/", "");
            Path filePath = Paths.get(relativePath);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + fileUrl, e);
        }
    }

    /**
     * Validate file against allowed types
     */
    private void validateFile(MultipartFile file, String[] allowedTypes) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        String contentType = file.getContentType();
        if (contentType == null) {
            throw new IllegalArgumentException("File type cannot be determined");
        }

        boolean isAllowed = false;
        for (String allowedType : allowedTypes) {
            if (contentType.startsWith(allowedType)) {
                isAllowed = true;
                break;
            }
        }

        if (!isAllowed) {
            throw new IllegalArgumentException(
                "Invalid file type: " + contentType + ". Allowed types: " + String.join(", ", allowedTypes)
            );
        }

        // Optional: Check file size (e.g., max 10MB)
        long maxSize = 10 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("File size exceeds maximum limit of 10MB");
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null) return "";
        int lastDot = filename.lastIndexOf('.');
        return (lastDot == -1) ? "" : filename.substring(lastDot);
    }
}