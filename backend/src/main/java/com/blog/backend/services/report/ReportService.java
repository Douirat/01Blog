package com.blog.backend.services.report;



import com.blog.backend.dtos.report.ReportInputDTO;
import com.blog.backend.dtos.report.ReportResponseDTO;
import org.springframework.data.domain.Page;


public interface ReportService {
  ReportResponseDTO save(ReportInputDTO report);
  long getReportsCount();
  Page<ReportResponseDTO> getAllReports(int page);

  Page<ReportResponseDTO> getUserReports(int page, Long userId);
}