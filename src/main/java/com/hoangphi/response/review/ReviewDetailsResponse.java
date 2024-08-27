package com.hoangphi.response.review;

import com.hoangphi.response.takeAction.ReviewItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDetailsResponse {

    private String id;

    private String name;

    private String image;

    private Double rate;

    private Integer totalRate;

    private DetailRate detailRate;

    //list review of product
    private List<ReviewItem> reviews;

}
