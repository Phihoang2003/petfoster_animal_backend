package com.hoangphi.service.admin.brands;

import com.hoangphi.request.brands.BrandRequest;
import com.hoangphi.request.brands.CreateBrandRequest;
import com.hoangphi.response.ApiResponse;

public interface BrandService {
    ApiResponse getAllBrand();

    ApiResponse createBrand(CreateBrandRequest brand);

    ApiResponse updateBrand(Integer id, BrandRequest brandRequest);

    ApiResponse deleteBrand(Integer id);
}
