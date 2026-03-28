package com.blog.backend.controllers.report;

import java.util.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import com.blog.backend.dtos.report.ReportInputDTO;
import com.blog.backend.dtos.report.ReportResponseDTO;
import com.blog.backend.dtos.report.PaginatedReportsDTO;
import com.blog.backend.services.report.ReportService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    // Create a report:
    @PostMapping
    public ResponseEntity<ReportResponseDTO> createReport(@RequestBody ReportInputDTO reportInput) {
        ReportResponseDTO saved = this.reportService.save(reportInput);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    // Get reports' count based on pending reports.
    @GetMapping("/count")
    public Map<String, Long> getReportsCount() {
        return Map.of("count", this.reportService.getReportsCount());
    }

    @GetMapping
    public ResponseEntity<PaginatedReportsDTO> getAllReports(@RequestParam(defaultValue = "0") int page) {
        if (page < 0) {
            return ResponseEntity.badRequest().build();
        }
        Page<ReportResponseDTO> reports = this.reportService.getAllReports(page);
        if (reports.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        PaginatedReportsDTO response = new PaginatedReportsDTO(
            reports.getContent(),
            reports.isLast(),
            reports.getTotalPages(),
            reports.getTotalElements());
            return ResponseEntity.ok(response);
        }
        
        @GetMapping("/user")
        public ResponseEntity<PaginatedReportsDTO> getUserReports(@RequestParam("userId") Long userId, @RequestParam(value = "page", defaultValue = "0") int page) {
        if(page < 0) return ResponseEntity.badRequest().build();
        Page<ReportResponseDTO> reports = this.reportService.getUserReports(page, userId);
        if(reports.isEmpty())return ResponseEntity.noContent().build();
        PaginatedReportsDTO response = new PaginatedReportsDTO(
            reports.getContent(),
            reports.isLast(),
            reports.getTotalPages(),
            reports.getTotalElements()
        );
        return ResponseEntity.ok(response);
    }
}