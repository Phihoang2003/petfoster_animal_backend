package com.hoangphi.controller.admin.review;

import com.hoangphi.request.review.ReviewReplyRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.admin.review.AdminReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {
    private final AdminReviewService reviewService;

    @GetMapping("")
    public ResponseEntity<ApiResponse> filterReviews(
            @RequestParam("productName") Optional<String> productName,
            @RequestParam("minStar") Optional<Integer> minStar,
            @RequestParam("maxStar") Optional<Integer> maxStar,
            @RequestParam("sort") Optional<String> sort,
            @RequestParam("page") Optional<Integer> page) {
        return ResponseEntity.ok(reviewService.filterReviews(productName, minStar, maxStar, sort, page));
    }

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

    @GetMapping("/details")
    public ResponseEntity<ApiResponse> reviewDetailsFilter(@RequestParam("id") String id,
                                                           @RequestParam("notReply") Optional<Boolean> notReply) {
        return ResponseEntity.ok(reviewService.reviewDetailsFilter(id, notReply));
    }


}
