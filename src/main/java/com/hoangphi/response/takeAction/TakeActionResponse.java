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
public class TakeActionResponse {
    private List<ProductItem> newArrivals;
}
