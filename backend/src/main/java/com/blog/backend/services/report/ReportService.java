package com.blog.backend.services.report;


import com.blog.backend.dtos.report.ReportInputDTO;
import com.blog.backend.dtos.report.ReportResponseDTO;


public interface ReportService {
  ReportResponseDTO save(ReportInputDTO report);
  long getReportsCount();
}