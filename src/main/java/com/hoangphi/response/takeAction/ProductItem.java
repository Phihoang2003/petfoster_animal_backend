package com.hoangphi.response.takeAction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductItem {
    private String id;
    private String brand;
    private Integer discount;
    private String image;
    private String name;
    private Double rating;
    private Object size;
    private Integer oldPrice;
    private Integer price;
    private Integer reviews;
    private List<ReviewItem> reviewItems;

}
