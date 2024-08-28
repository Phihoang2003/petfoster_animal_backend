package com.hoangphi.controller.feedback;

import com.hoangphi.request.feedback.FeedbackRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.feedback.FeedbackService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;
    @PostMapping("")
    public ResponseEntity<ApiResponse> feedback(@Valid @RequestBody FeedbackRequest feedBackRequest,
                                                HttpServletRequest request) {
        return ResponseEntity.ok(feedbackService.feedback(request, feedBackRequest));
    }
}
