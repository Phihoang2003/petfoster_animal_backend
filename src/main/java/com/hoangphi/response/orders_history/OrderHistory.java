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
public class OrderHistory {
    private Integer id;
    private String datePlace;
    private String state;
    private String stateMessage;
    private Double total;
    private List<OrderProductItem> products;
    private Boolean isTotalRate;
}
