package com.hoangphi.service.impl.feedback;

import com.hoangphi.constant.RespMessage;
import com.hoangphi.entity.Feedback;
import com.hoangphi.repository.FeedbackRepository;
import com.hoangphi.request.feedback.FeedbackRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.common.PaginationResponse;
import com.hoangphi.response.feedback.FeedbackResponse;
import com.hoangphi.response.feedback.ManageFeedbackPagination;
import com.hoangphi.service.feedback.FeedbackService;
import com.hoangphi.utils.MailUtils;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final MailUtils mailUtils;

    @Override
    public ApiResponse getFeedback(Optional<Integer> page) {
        List<Feedback> feedbacks = feedbackRepository.findAllReverse();

        if (feedbacks.isEmpty()) {
            return ApiResponse.builder().message("Page not exist")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .errors(false)
                    .data(PaginationResponse.builder()
                            .data(new ArrayList<>())
                            .pages(0).build())
                    .build();
        }

        Pageable pageable = PageRequest.of(page.orElse(0), 10);
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), feedbacks.size());

        if (startIndex >= endIndex) {
            return ApiResponse.builder()
                    .message(RespMessage.NOT_FOUND.getValue())
                    .data(PaginationResponse.builder()
                            .data(new ArrayList<>())
                            .pages(0).build())
                    .errors(false)
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }

        List<Feedback> visibleFeedbacks = feedbacks.subList(startIndex, endIndex);

        Page<Feedback> pagination = new PageImpl<Feedback>(visibleFeedbacks, pageable,
                feedbacks.size());

        return ApiResponse.builder()
                .message("Successfully!")
                .errors(false)
                .status(HttpStatus.OK.value())
                .data(new ManageFeedbackPagination(pagination.getContent(),
                        pagination.getTotalPages(),feedbacks.size()))
                .build();
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
