package com.blog.backend.dtos.report;
package com.blog.backend.types.report.ReportStatus;


public record ReportInputDTO(
Long postId,
Long reporterId,
String reason
){}

public record ReportResponseDTO(
        Long id,
        Long postId,
        Long reporterId,
        String reason,
        ReportStatus status,
        LocalDateTime createdAt
) {}
