package com.hoangphi.response.recent_view;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecentViewResponse {
    String id;
    String brand;
    Integer[] size;
    Integer discount;
    String image;
    String name;
    Double oldPrice;
    Double price;
    Integer rating;
}
