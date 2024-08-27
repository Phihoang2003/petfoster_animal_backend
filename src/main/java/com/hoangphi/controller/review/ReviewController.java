package com.hoangphi.controller.review;

import com.hoangphi.request.review.ReviewRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.review.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/reviews")
    public ResponseEntity<ApiResponse> order(@RequestHeader("Authorization") String jwt,
                                             @Valid @RequestBody ReviewRequest reviewRequest) {
        return ResponseEntity.ok(reviewService.createReview(jwt, reviewRequest));
    }


}
