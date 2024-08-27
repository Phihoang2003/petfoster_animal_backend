package com.hoangphi.controller.admin.review;

import com.hoangphi.request.review.ReviewReplyRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.admin.review.AdminReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {
    private final AdminReviewService reviewService;
    @PostMapping("")
    public ResponseEntity<ApiResponse> replay(@RequestHeader("Authorization") String token,
                                              @RequestBody ReviewReplyRequest replayRequest) {
        return ResponseEntity.ok(reviewService.reply(token, replayRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> reviewDetails(@PathVariable String id) {
        return ResponseEntity.ok(reviewService.reviewDetails(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(
            @PathVariable("id") Integer id) {
        return ResponseEntity.ok(reviewService.delete(id));
    }
}
