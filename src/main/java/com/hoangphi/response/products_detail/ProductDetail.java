package com.hoangphi.response.products_detail;

import com.hoangphi.response.takeAction.ProductItem;
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
public class ProductDetail {
    public String id;
    public String brand;
    public Integer discount;
    public String image;
    public String name;
    public Double rating;
    public String description;
    public List<String> images;
    public List<SizeAndPrice> sizeAndPrice;
    public List<ProductItem> suggestions;
    private Integer reviews;
    private List<ReviewItem> reviewItems;

}
