package com.blog.backend.dtos.report;
import com.blog.backend.types.report.ReportStatus;
import java.time.LocalDateTime;


public record ReportResponseDTO(
        Long id,
        Long postId,
        Long reporterId,
        String reason,
        ReportStatus status,
        LocalDateTime createdAt
) {}
