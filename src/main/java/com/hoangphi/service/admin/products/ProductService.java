package com.hoangphi.service.admin.products;

import com.hoangphi.request.CreateProductRequest;
import com.hoangphi.request.products.ProductRequest;
import com.hoangphi.response.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ApiResponse createProduct(CreateProductRequest createProductRequest, List<MultipartFile> images);
    ApiResponse updateProduct(String id, ProductRequest updateProductReq);
}
