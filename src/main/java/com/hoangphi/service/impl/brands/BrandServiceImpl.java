package com.hoangphi.service.impl.brands;

import com.hoangphi.entity.Brand;
import com.hoangphi.repository.BrandRepository;
import com.hoangphi.request.brands.BrandRequest;
import com.hoangphi.request.brands.CreateBrandRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.admin.brands.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    @Override
    public ApiResponse getAllBrand() {
        return null;
    }
    @Override
    public ApiResponse createBrand(CreateBrandRequest brand) {
        Map<String, String> errorsMap = new HashMap<>();
        if(brand.getName().isEmpty()){
            errorsMap.put("brand", "brand can't be blank!");
            return ApiResponse.builder()
                    .message("brand can't be blank!")
                    .status(HttpStatus.NOT_ACCEPTABLE.value())
                    .errors(errorsMap)
                    .build();
        }
        List<Brand> listBrand=brandRepository.findByName(brand.getName()).orElse(null);
        System.out.println("listBrand: "+listBrand);
        if(!listBrand.isEmpty()){
            errorsMap.put("brand", "brand already exits!");
            return ApiResponse.builder()
                    .message("brand already exits!")
                    .status(HttpStatus.FOUND.value())
                    .errors(errorsMap)
                    .build();
        }
        Brand newBrand=Brand.builder()
                .brand(brand.getName())
                .build();
        brandRepository.save(newBrand);
        return ApiResponse.builder()
                .message("Create brand successfully!")
                .status(HttpStatus.CREATED.value())
                .errors(false)
                .data(newBrand)
                .build();
    }

    @Override
    public ApiResponse updateBrand(Integer id, BrandRequest brandRequest) {
        return null;
    }

    @Override
    public ApiResponse deleteBrand(Integer id) {
        return null;
    }
}
