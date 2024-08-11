package com.hoangphi.service.admin.products_repo;

import com.hoangphi.request.variants.CreateRepoRequest;
import com.hoangphi.response.ApiResponse;

public interface ProductRepoService {
    ApiResponse deleteProductRepo(Integer id);

    ApiResponse getProductRepositories(String idProduct);

    ApiResponse addAProductRepository(String idProduct, CreateRepoRequest createRepoRequest);
}
