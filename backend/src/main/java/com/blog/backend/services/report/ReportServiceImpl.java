package com.blog.backend.services.report;


import com.blog.backend.models.report.Report;
import com.blog.backend.models.user.User;
import com.blog.backend.models.post.Post;
import com.blog.backend.repositories.report.ReportRepository;
import com.blog.backend.repositories.user.UserRepository;
import com.blog.backend.repositories.post.PostRepository;
import com.blog.backend.dtos.report.ReportInputDTO;
import com.blog.backend.dtos.report.ReportResponseDTO;
import com.blog.backend.types.report.ReportStatus;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    // Inject the necessary packages:
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public ReportResponseDTO save(ReportInputDTO report) {
        // fetch the reporter:
        User reporter = userRepository.findById(report.reporterId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch the reported post:
        Post reportedPost = postRepository.findById(report.postId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Report newReport = new Report();
        newReport.setReporter(reporter);
        newReport.setPost(reportedPost);
        newReport.setReason(report.reason());

        Report saved = reportRepository.save(newReport);

        return new ReportResponseDTO(
                saved.getId(),
                saved.getPost().getId(),
                saved.getReporter().getId(),
                saved.getReason(),
                saved.getStatus(),
                saved.getCreatedAt());
    }

    public long getReportsCount() {
        return this.reportRepository.countByStatus(ReportStatus.PENDING);
    }

    @Override
    public Page<ReportResponseDTO> getAllReports(int page) {
        int size = 10;

        Pageable pageable = PageRequest.of(
                page,
                size
        // Sort.by(Sort.Direction.DESC, "createdAt") // newest → oldest
        );

        Page<Report> reports = this.reportRepository.findAllOrderByPriority(pageable);
        return reports.map(report -> {
            return new ReportResponseDTO(
                    report.getId(),
                    report.getPost().getId(),
                    report.getReporter().getId(),
                    report.getReason(),
                    report.getStatus(),
                    report.getCreatedAt());
        });
    }

    @Override
    public Page<ReportResponseDTO> getUserReports(int page, Long userId) {
        int size = 10;

        Pageable pageable = PageRequest.of(
                page,
                size
        // Sort.by(Sort.Direction.DESC, "createdAt") // newest → oldest
        );

        Page<Report> reports = this.reportRepository.findAllByReporterIdOrderByPriority(userId, pageable);
        return reports.map(report -> {
            return new ReportResponseDTO(
                    report.getId(),
                    report.getPost().getId(),
                    report.getReporter().getId(),
                    report.getReason(),
                    report.getStatus(),
                    report.getCreatedAt());
        });
    }
}