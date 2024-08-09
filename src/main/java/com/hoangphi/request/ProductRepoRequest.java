package com.hoangphi.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRepoRequest {
    @NotBlank(message = "Product ID can't be blank!")
    private Integer id;
    private String productId;
    private String size;
    private Double inPrice;
    private Double outPrice;
    private Integer quantity;
    private Boolean inStock;
}
