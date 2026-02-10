package com.blog.backend.services.report;

import java.util.Optional;
import com.blog.backend.models.report.Report;

public interface ReportService {
    Optional<Report>save(Report report);
}