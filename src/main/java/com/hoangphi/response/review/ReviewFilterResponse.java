package com.hoangphi.response.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewFilterResponse {
    private String productId;

    private String productName;

    private String image;

    private Double rate;

    // private String lastest;
    private LocalDate latest;

    private Integer reviews;

    private Integer commentNoRep;
}
