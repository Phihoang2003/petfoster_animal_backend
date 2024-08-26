package com.hoangphi.response.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse{
    private String productId;
    private Integer orderId;
    private String comment;
    private Integer rate;
}
