package com.hoangphi.controller.admin.feedback;

import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.feedback.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> seen(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(feedbackService.seen(id));
    }
}
