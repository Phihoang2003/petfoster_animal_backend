package com.hoangphi.controller.product;

import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.product.ProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductDetailController {
    private final ProductDetailService productDetailsService;
    @GetMapping("/detail/{id}")
    public ResponseEntity<ApiResponse> productDetails(@PathVariable String id) {
        return ResponseEntity.ok(productDetailsService.productDetails(id));
    }
}
