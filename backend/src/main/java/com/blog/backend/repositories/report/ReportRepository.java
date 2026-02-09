package com.blog.backend.repositories.report;

import com.blog.backend.models.report.Report;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long>{

}