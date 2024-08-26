package com.hoangphi.response.orders_history;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdersFilterResponse {
    private List<OrderFilterResponse> orderFilters;
    private Integer pages;
}
