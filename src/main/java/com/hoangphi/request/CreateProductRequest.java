package com.hoangphi.request;

import com.hoangphi.request.products.ProductRepo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductRequest {
    @NotBlank(message = "Product Name can't be blank!")
    private String name;
    @NotBlank(message = "Product Description can't be blank!")
    private String description;
    @NotBlank(message = "Product Type can't be blank!")
    private String type;
    @NotBlank(message = "Product Type can't be blank!")
    private String brand;
    @Valid
    private List<ProductRepo> productsRepo;
    private List<MultipartFile> images;
}
