package com.hoangphi.controller.admin.brand;

import com.hoangphi.request.brands.CreateBrandRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.admin.brands.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/brands/")
public class BrandController {
    private final BrandService brandService;
    @PostMapping("")
    public ResponseEntity<ApiResponse> createBrand(@Valid @RequestBody CreateBrandRequest name ) {
        return ResponseEntity.ok(brandService.createBrand(name));
    }
}
