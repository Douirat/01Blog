package com.blog.backend.repositories.report;

import com.blog.backend.models.report.Report;
import com.blog.backend.types.report.ReportStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReportRepository extends JpaRepository<Report, Long> {

    long countByStatus(ReportStatus status);

    // All reports ordered by priority (PENDING first)
    @Query("""
        SELECT r FROM Report r
        ORDER BY 
            CASE 
                WHEN r.status = com.blog.backend.types.report.ReportStatus.PENDING THEN 0
                WHEN r.status = com.blog.backend.types.report.ReportStatus.REVIEWED THEN 1
                WHEN r.status = com.blog.backend.types.report.ReportStatus.APPROVED THEN 2
                WHEN r.status = com.blog.backend.types.report.ReportStatus.REJECTED THEN 3
                ELSE 4
            END,
            r.createdAt DESC
    """)
    Page<Report> findAllOrderByPriority(Pageable pageable);

    //  Reports by reporter (user) ordered by priority
    @Query("""
        SELECT r FROM Report r
        WHERE r.reporter.id = :userId
        ORDER BY 
            CASE 
                WHEN r.status = com.blog.backend.types.report.ReportStatus.PENDING THEN 0
                WHEN r.status = com.blog.backend.types.report.ReportStatus.REVIEWED THEN 1
                WHEN r.status = com.blog.backend.types.report.ReportStatus.APPROVED THEN 2
                WHEN r.status = com.blog.backend.types.report.ReportStatus.REJECTED THEN 3
                ELSE 4
            END,
            r.createdAt DESC
    """)
    Page<Report> findAllByReporterIdOrderByPriority(Long userId, Pageable pageable);
}