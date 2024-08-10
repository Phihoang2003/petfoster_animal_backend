package com.hoangphi.controller.admin.brand;

import com.hoangphi.request.brands.BrandRequest;
import com.hoangphi.request.brands.CreateBrandRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.admin.brands.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/brands")
public class BrandController {
    private final BrandService brandService;
    @PostMapping("")
    public ResponseEntity<ApiResponse> createBrand(@Valid @RequestBody CreateBrandRequest name ) {
        return ResponseEntity.ok(brandService.createBrand(name));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateBrand(@PathVariable("id") Integer id, @Valid @RequestBody BrandRequest brandRequest ) {
        return ResponseEntity.ok(brandService.updateBrand(id, brandRequest));
    }
    @GetMapping("")
    public ResponseEntity<ApiResponse> getAllBrand() {
        return ResponseEntity.ok(brandService.getAllBrand());
    }
}
