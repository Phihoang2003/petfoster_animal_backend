package com.hoangphi.response.products_manage;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductInfoResponse {
    private String id;
    private String name;
    private String brand;
    private String type;
    private String description;
}
