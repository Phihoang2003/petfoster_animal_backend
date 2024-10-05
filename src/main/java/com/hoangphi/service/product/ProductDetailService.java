package com.hoangphi.service.product;

import com.hoangphi.response.ApiResponse;

public interface ProductDetailService {
    public ApiResponse productDetails(String id);

    public ApiResponse getProductTypesAndBrands();
}
