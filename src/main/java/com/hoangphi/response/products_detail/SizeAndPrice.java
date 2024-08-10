package com.hoangphi.response.products_detail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SizeAndPrice {
    public Integer size;
    public Double price;
    public Double oldPrice;
    public Integer repo;
}
