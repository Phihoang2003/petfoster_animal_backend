package com.hoangphi.service.admin.products;

import com.hoangphi.request.CreateProductRequest;
import com.hoangphi.request.products.ProductInfoRequest;
import com.hoangphi.request.products.ProductRequest;
import com.hoangphi.response.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    ApiResponse createProduct(CreateProductRequest createProductRequest, List<MultipartFile> images);
    ApiResponse updateProduct(String id, ProductRequest updateProductReq);
    ApiResponse getProductInfo(String id);

    ApiResponse getProduct(String id);

    ApiResponse getAllProduct(Optional<Integer> page);
    ApiResponse updateProductWithInfo(String id, ProductInfoRequest productInfoRequest);
    ApiResponse deleteProduct(String id);

}
