package com.hoangphi.controller.admin.feedback;

import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.feedback.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin/feedbacks")
@RequiredArgsConstructor
public class ManageFeedbackController {
    private final FeedbackService feedbackService;;

    @GetMapping
    public ResponseEntity<ApiResponse> getFeedback(
            @RequestParam(value = "page", defaultValue = "0", required = false) Optional<Integer> page) {
        return ResponseEntity.ok(feedbackService.getFeedback(page));
    }
}
