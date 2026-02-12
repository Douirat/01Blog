package com.blog.backend.services.report;

// import necessary models:
import com.blog.backend.models.report.Report;
import com.blog.backend.models.user.User;
import com.blog.backend.models.post.Post;

// import the necessary repositories:
import com.blog.backend.repositories.report.ReportRepository;
import com.blog.backend.repositories.user.UserRepository;
import com.blog.backend.repositories.post.PostRepository;


import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    // Inject the necessary packages: 
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public Report save(ReportInputDTO report){

        // fetch the reporter:
        User reporter = userRepository.findById(report.reporterId());
        .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch the reported post:
        Post reportedPost = postRepository.findById(report.postId())
        .orElseThrow(() => new RuntimeException("Post not found"));

        Report new_report = new Report();
        new_report.set    


        return reportRepository.save(report);
    }
}