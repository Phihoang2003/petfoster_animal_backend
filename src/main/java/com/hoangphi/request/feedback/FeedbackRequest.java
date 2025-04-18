package com.hoangphi.request.feedback;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FeedbackRequest {
    private String fullName;
    private String phone;
    @NotBlank
    private String email;
    @NotBlank
    private String message;
}
