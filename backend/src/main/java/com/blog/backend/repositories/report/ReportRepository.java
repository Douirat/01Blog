package com.blog.backend.repositories.report;

import com.blog.backend.models.post.Post;
import com.blog.backend.models.report.Report;
import com.blog.backend.types.report.ReportStatus;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportRepository extends JpaRepository<Report, Long> {

    long countByStatus(ReportStatus status);

    // All reports ordered by priority (PENDING first)
    @Query("""
                SELECT r FROM Report r
                ORDER BY
                    CASE
                        WHEN r.status = com.blog.backend.types.report.ReportStatus.PENDING THEN 0
                        WHEN r.status = com.blog.backend.types.report.ReportStatus.APPROVED THEN 1
                        WHEN r.status = com.blog.backend.types.report.ReportStatus.REJECTED THEN 3
                        ELSE 4
                    END,
                    r.createdAt DESC
            """)
    Page<Report> findAllOrderByPriority(Pageable pageable);

    // Reports by reporter (user) ordered by priority
    @Query("""
                SELECT r FROM Report r
                WHERE r.reporter.id = :userId
                ORDER BY
                    CASE
                        WHEN r.status = com.blog.backend.types.report.ReportStatus.PENDING THEN 0
                        WHEN r.status = com.blog.backend.types.report.ReportStatus.APPROVED THEN 1
                        WHEN r.status = com.blog.backend.types.report.ReportStatus.REJECTED THEN 2
                        ELSE 4
                    END,
                    r.createdAt DESC
            """)
    Page<Report> findAllByReporterIdOrderByPriority(Long userId, Pageable pageable);

    @Query("SELECT r.post FROM Report r WHERE r.post.user.id = :userId")
    Page<Post> findAllReportedPosts(Pageable pageable, long userId);
    // Page<Post> findDistinctByReportsIsNotEmptyAndUserId(Pageable pageable, long
    // userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Report r WHERE r.post.id = :postId")
    int deleteByPostId(@Param("postId") Long postId);

    @Modifying
    @Transactional
    @Query("UPDATE Report r SET r.status = :status WHERE r.post.id = :postId")
    int updateStatusByPostId(@Param("postId") Long postId,
            @Param("status") ReportStatus status);

}