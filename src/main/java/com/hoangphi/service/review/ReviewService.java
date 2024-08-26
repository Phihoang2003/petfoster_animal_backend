package com.hoangphi.service.review;

import com.hoangphi.request.review.ReviewRequest;
import com.hoangphi.response.ApiResponse;

public interface ReviewService {
    public ApiResponse createReview(String jwt, ReviewRequest reviewRequest);
}
