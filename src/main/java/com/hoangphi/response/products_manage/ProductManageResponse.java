package com.hoangphi.response.products_manage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductManageResponse {
    private String id;
    private String image;
    private String brand;
    private String name;
    private String type;
    private Object repo;
}
