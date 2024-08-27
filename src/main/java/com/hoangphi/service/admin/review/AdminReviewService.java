package com.hoangphi.service.admin.review;

import com.hoangphi.request.review.ReviewReplyRequest;
import com.hoangphi.response.ApiResponse;

import java.util.Optional;

public interface AdminReviewService {
    ApiResponse filterReviews(Optional<String> name, Optional<Integer> minStar, Optional<Integer> maxStar,
                              Optional<String> sort, Optional<Integer> page);

    ApiResponse reviewDetails(String productId);

    ApiResponse reviewDetailsFilter(String productId, Optional<Boolean> notReply);

    ApiResponse reply(String token, ReviewReplyRequest replayRequest);

    ApiResponse delete(Integer id);
}
