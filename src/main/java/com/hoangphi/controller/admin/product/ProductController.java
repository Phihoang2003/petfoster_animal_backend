package com.hoangphi.controller.admin.product;

import com.hoangphi.request.CreateProductRequest;
import com.hoangphi.request.products.ProductRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.admin.products.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

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

    @PostMapping(value = "/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable("id") String id,
                                                     @RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok(productService.updateProduct(id, productRequest));

    }
    @GetMapping("/info/{id}")
    public ResponseEntity<ApiResponse> getProductInfo(@PathVariable("id") String id) {
        return ResponseEntity.ok(productService.getProductInfo(id));
    }
    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse> getAllProduct(@RequestParam("page") Optional<Integer> page) {
        return ResponseEntity.ok(productService.getAllProduct(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProduct(@PathVariable("id") String id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

}
