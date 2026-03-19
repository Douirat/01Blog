package com.blog.backend.repositories.report;

import com.blog.backend.models.report.Report;
import com.blog.backend.types.report.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long>{
// SELECT COUNT(*) FROM report WHERE status = ?
long countByStatus(ReportStatus status);
}