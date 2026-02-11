package com.blog.backend.controllers.report;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.data.domain.Page;
import com.blog.backend.models.report.Report;
import com.blog.backend.dtos.report.ReportInputDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController{
private final ReportService reportService;
    // Create a report:
    @PostMapping
    public ResponseEntity<Report> createReport(@RequestBody ReportInputDTO reportInput){
        Report saved = this.reportService.save(report);
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(saved);
    }
}