package com.blog.backend.dtos.report;
import java.util.*;

public record PaginatedReportsDTO(
    List<ReportResponseDTO> content,
    boolean last,
    int totalPages,
    long totalElements
) {}