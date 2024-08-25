package com.hoangphi.response.orders_history;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderProductItem {
    private String id;
    private String brand;
    private String image;
    private String name;
    private Object size;
    private Integer price;
    private Integer quantity;
    private Integer repo;
    private Boolean isRate;
}
