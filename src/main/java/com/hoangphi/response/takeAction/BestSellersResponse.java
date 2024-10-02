package com.hoangphi.response.takeAction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BestSellersResponse {
    private List<ProductItem> data;
    private Integer pages;
}
