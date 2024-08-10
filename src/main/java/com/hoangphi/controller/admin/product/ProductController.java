package com.hoangphi.controller.admin.product;

import com.hoangphi.request.CreateProductRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.admin.products.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("")
    public ResponseEntity<ApiResponse> createProduct(@ModelAttribute CreateProductRequest createProductRequest,
                                                     @RequestPart List<MultipartFile> images) {
        return ResponseEntity.ok(productService.createProduct(createProductRequest, images));
    }
}
