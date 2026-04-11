package com.blog.backend.services.file;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Value("${file.base-url:http://localhost:8080}")
    private String baseUrl;

    private Path rootPath;

    // ─────────────────────────────────────────────
    // Magic byte signatures
    // ─────────────────────────────────────────────

    private static final byte[] PNG  = { (byte)0x89, 0x50, 0x4E, 0x47 };
    private static final byte[] JPG  = { (byte)0xFF, (byte)0xD8 };
    private static final byte[] WEBM = { 0x1A, 0x45, (byte)0xDF, (byte)0xA3 };
    private static final byte[] RIFF = { 0x52, 0x49, 0x46, 0x46 }; // AVI / WEBP base
    // MP4 / MOV: "ftyp" sits at offset 4, not 0 — match() handles the offset
    private static final byte[] FTYP = { 0x66, 0x74, 0x79, 0x70 };

    @PostConstruct
    public void init() {
        try {
            rootPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(rootPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize upload directory", e);
        }
    }

    // ─────────────────────────────────────────────
    // SAVE FILE
    // ─────────────────────────────────────────────

    @Override
    public String saveFile(MultipartFile file, String subDirectory, String[] allowedTypes) {
        validateFile(file, allowedTypes);

        try {
            byte[] data = file.getBytes();

            Path targetDir = resolveSafeDirectory(subDirectory);
            Files.createDirectories(targetDir);

            String mime = detectType(data);
            String extension = extensionFromMime(mime);

            String filename = UUID.randomUUID() + extension;
            Path filePath = targetDir.resolve(filename);

            Files.write(filePath, data, StandardOpenOption.CREATE_NEW);

            return baseUrl + "/" + uploadDir + "/" + subDirectory + "/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    // ─────────────────────────────────────────────
    // DELETE FILE
    // ─────────────────────────────────────────────

    @Override
    public void deleteFile(String fileUrl) {
        try {
            String relative = fileUrl.replace(baseUrl + "/", "");

            Path filePath = rootPath.resolve(relative).normalize();

            if (!filePath.startsWith(rootPath)) {
                throw new SecurityException("Invalid file path");
            }

            Files.deleteIfExists(filePath);

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }

    // ─────────────────────────────────────────────
    // VALIDATION
    // ─────────────────────────────────────────────

    @Override
    public void validateFile(MultipartFile file, String[] allowedTypes) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        try {
            byte[] data = file.getBytes();

            String detectedType = detectType(data);

            boolean allowed = false;
            for (String type : allowedTypes) {
                if (detectedType.startsWith(type)) {
                    allowed = true;
                    break;
                }
            }

            if (!allowed) {
                throw new IllegalArgumentException(
                    "Invalid file type: " + detectedType + ". Allowed: " + String.join(", ", allowedTypes)
                );
            }

            if (detectedType.startsWith("image/")) {
                long max = 5L * 1024 * 1024; // 5 MB
                if (file.getSize() > max) {
                    throw new IllegalArgumentException("Image exceeds 5 MB limit");
                }
            } else {
                long max = 200L * 1024 * 1024; // 200 MB
                if (file.getSize() > max) {
                    throw new IllegalArgumentException("Video exceeds 200 MB limit");
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Validation failed", e);
        }
    }

    // ─────────────────────────────────────────────
    // TYPE DETECTION (MAGIC BYTES)
    // ─────────────────────────────────────────────

    /**
     * Detects the real MIME type by inspecting binary magic bytes.
     * Order matters — more specific signatures are checked before generic ones.
     *
     * Supported formats:
     *   Images : JPEG, PNG, WEBP
     *   Videos : MP4/MOV, WebM, AVI
     */
    private String detectType(byte[] data) {

        if (data.length < 12) {
            throw new IllegalArgumentException("File is too small to be a valid media file");
        }

        // ── Images ────────────────────────────────────────────────

        // PNG: 89 50 4E 47 at offset 0
        if (match(data, PNG, 0)) return "image/png";

        // JPEG: FF D8 at offset 0
        if (match(data, JPG, 0)) return "image/jpeg";

        // WEBP: "RIFF" at offset 0 AND "WEBP" at offset 8
        // Must be checked before the generic RIFF block below
        if (match(data, RIFF, 0) &&
            data[8] == 0x57 && data[9] == 0x45 &&
            data[10] == 0x42 && data[11] == 0x50) {
            return "image/webp";
        }

        // ── Videos ────────────────────────────────────────────────

        // MP4 / MOV: "ftyp" at offset 4 (size field occupies bytes 0-3)
        if (match(data, FTYP, 4)) return "video/mp4";

        // WebM: 1A 45 DF A3 at offset 0
        if (match(data, WEBM, 0)) return "video/webm";

        // AVI: "RIFF" at offset 0 AND "AVI " at offset 8
        if (match(data, RIFF, 0) &&
            data[8] == 0x41 && data[9] == 0x56 &&
            data[10] == 0x49 && data[11] == 0x20) {
            return "video/avi";
        }

        throw new IllegalArgumentException("Unsupported or corrupted file format");
    }

    /**
     * Returns true if {@code signature} matches {@code data} starting at {@code offset}.
     */
    private boolean match(byte[] data, byte[] signature, int offset) {
        if (data.length < offset + signature.length) return false;
        for (int i = 0; i < signature.length; i++) {
            if (data[offset + i] != signature[i]) return false;
        }
        return true;
    }

    // ─────────────────────────────────────────────
    // SAFE DIRECTORY RESOLUTION
    // ─────────────────────────────────────────────

    private Path resolveSafeDirectory(String subDirectory) {
        Path dir = rootPath.resolve(subDirectory).normalize();
        if (!dir.startsWith(rootPath)) {
            throw new SecurityException("Invalid directory path");
        }
        return dir;
    }

    // ─────────────────────────────────────────────
    // EXTENSION MAPPING
    // ─────────────────────────────────────────────

    private String extensionFromMime(String mime) {
        return switch (mime) {
            case "image/jpeg" -> ".jpg";
            case "image/png"  -> ".png";
            case "image/webp" -> ".webp";
            case "video/mp4"  -> ".mp4";
            case "video/webm" -> ".webm";
            case "video/avi"  -> ".avi";
            default -> "";
        };
    }
}