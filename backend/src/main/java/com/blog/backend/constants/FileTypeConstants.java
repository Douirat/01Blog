package com.blog.backend.constants;

public class FileTypeConstants {
    
    // Image types only (for avatars)
    public static final String[] IMAGE_TYPES = {
        "image/jpeg",
        "image/jpg", 
        "image/png",
        "image/gif",
        "image/webp"
    };
    
    // Images and videos (for posts and comments)
    public static final String[] MEDIA_TYPES = {
        "image/jpeg",
        "image/jpg",
        "image/png", 
        "image/gif",
        "image/webp",
        "video/mp4",
        "video/mpeg",
        "video/quicktime",
        "video/webm"
    };

    // Subdirectories
    public static final String AVATAR_DIR = "avatars";
    public static final String POST_MEDIA_DIR = "post-media";
    public static final String COMMENT_MEDIA_DIR = "comment-media";
}