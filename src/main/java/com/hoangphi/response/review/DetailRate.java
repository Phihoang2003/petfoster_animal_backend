package com.hoangphi.response.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailRate {

    private Integer one;

    private Integer two;

    private Integer three;

    private Integer four;

    private Integer five;

}
