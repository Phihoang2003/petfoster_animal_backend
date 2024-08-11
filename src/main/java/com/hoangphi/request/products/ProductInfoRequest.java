package com.hoangphi.request.products;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductInfoRequest {
    private String id;
    private String name;
    private Integer brand;
    private String type;
    private String description;
}
