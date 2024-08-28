package com.hoangphi.service.impl.feedback;

import com.hoangphi.entity.Feedback;
import com.hoangphi.repository.FeedbackRepository;
import com.hoangphi.request.feedback.FeedbackRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.feedback.FeedbackResponse;
import com.hoangphi.service.feedback.FeedbackService;
import com.hoangphi.utils.MailUtils;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final MailUtils mailUtils;

    @Override
    public ApiResponse getFeedback(Optional<Integer> page) {
        return null;
    }

    @Override
    public ApiResponse feedback(HttpServletRequest request, FeedbackRequest feedBackRequest) {
       try{
           Map<String, String> map = new HashMap<>();
           map.put("fullName", feedBackRequest.getFullName());
           map.put("email", feedBackRequest.getEmail());
           map.put("phone", feedBackRequest.getPhone());
           map.put("message", feedBackRequest.getMessage());
           Feedback feedback=Feedback.builder()
                   .fullName(feedBackRequest.getFullName())
                   .email(feedBackRequest.getEmail())
                   .phone(feedBackRequest.getPhone())
                   .message(feedBackRequest.getMessage())
                   .seen(false)
                   .build();
           feedbackRepository.save(feedback);

           mailUtils.sendTemplateEmail(
                   feedBackRequest.getEmail(), "Thanks your feedback!", "thanks", map);
       }catch(MessagingException e){
           return ApiResponse.builder()
                   .message("Failed")
                   .status(HttpStatus.BAD_REQUEST.value())
                   .errors(Boolean.TRUE)
                   .data(null)
                   .build();
       }
        return ApiResponse.builder()
                .message("Successfully")
                .status(HttpStatus.OK.value()).errors(Boolean.FALSE)
                .data(FeedbackResponse.builder()
                        .fullName(feedBackRequest.getFullName())
                        .email(feedBackRequest.getEmail())
                        .message(feedBackRequest.getMessage())
                        .build())
                .build();
    }

    @Override
    public ApiResponse seen(Integer id) {
        return null;
    }
}
