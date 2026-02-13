package com.blog.backend.dtos.report;

public record ReportInputDTO(
Long postId,
Long reporterId,
String reason
){}
