package com.hoangphi.response.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FeedbackResponse {
    private String fullName;
    private String email;
    private String message;
}
