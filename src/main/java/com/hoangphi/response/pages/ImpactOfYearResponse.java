package com.hoangphi.response.pages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImpactOfYearResponse {
    private String image;

    private String quantity;

    private String title;

    private String prefix;
}
