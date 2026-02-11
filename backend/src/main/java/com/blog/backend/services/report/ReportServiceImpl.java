package com.blog.backend.services.report;

import com.blog.backend.models.report.Report;
import com.blog.backend.models.user.User;
import com.blog.backend.models.;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    public ReportServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public Optional<Report> save(Report report) {
        Report new_report = new Report()

        return reportRepository.save(report);
    }
}