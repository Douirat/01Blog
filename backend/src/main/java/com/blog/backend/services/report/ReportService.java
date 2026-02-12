package com.blog.backend.services.report;

import com.blog.backend.models.report.Report;
import com.blog.backend.dtos.report.ReportInputDTO;


public interface ReportService {
    Report save(ReportInputDTO report);
}