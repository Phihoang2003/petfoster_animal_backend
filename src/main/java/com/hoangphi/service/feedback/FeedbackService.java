package com.hoangphi.service.feedback;

import com.hoangphi.request.feedback.FeedbackRequest;
import com.hoangphi.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public interface FeedbackService {
    public ApiResponse getFeedback(Optional<Integer> page);

    public ApiResponse feedback(HttpServletRequest request, FeedbackRequest feedBackRequest);

    public ApiResponse seen(Integer id);
}
