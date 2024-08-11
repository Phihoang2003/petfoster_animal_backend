package com.hoangphi.request.products;

import com.hoangphi.entity.Brand;
import com.hoangphi.entity.ProductRepo;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {
    @NotBlank(message = "Product Name can't be blank!")
    private String name;
    @NotBlank(message = "Product Description can't be blank!")
    private String desc;
    @NotBlank(message = "Product Type can't be blank!")
    private String productType;
    private Brand brand;
    private List<ProductRepo> productsRepo;
}
