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

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    @Override
    public ApiResponse getAllBrand() {
        List<Brand> listBrand = brandRepository.findAll();
        if (listBrand.isEmpty()) {
            return ApiResponse.builder()
                    .message("No brands data available!")
                    .status(200)
                    .errors(false)
                    .data(new ArrayList<>())
                    .build();
        } else
            return ApiResponse.builder()
                    .message("Successfully!!!")
                    .status(200)
                    .errors(false)
                    .data(listBrand)
                    .build();
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
        Map<String,String>errorsMap=new HashMap<>();
        if(!brandRequest.getId().equals(id)){
            errorsMap.put("id","id not match!");
            return ApiResponse.builder()
                    .message("id not match!")
                    .status(HttpStatus.CONFLICT.value())
                    .errors(errorsMap)
                    .build();
        }
        if(!brandRepository.existsById(brandRequest.getId())){
            errorsMap.put("id","id not found!");
            return ApiResponse.builder()
                    .message("id not found!")
                    .status(HttpStatus.NOT_FOUND.value())
                    .errors(errorsMap)
                    .build();
        }
        AtomicBoolean check= new AtomicBoolean(false);
        List<Brand> listBrand=brandRepository.findByName(brandRequest.getName()).orElse(null);
        assert listBrand != null;
        listBrand.stream().forEach(brand -> {
            if(!Objects.equals(brand.getId(), brandRequest.getId())){
                check.set(true);
            }
        });
        if (check.get()) {
            errorsMap.put("brand", "brand already exits!");
            return ApiResponse.builder()
                    .message("brand already exits!")
                    .status(HttpStatus.FOUND.value())
                    .errors(errorsMap).build();
        }
        Brand updateBrand=brandRepository.findById(id).get();
        updateBrand.setBrand(brandRequest.getName());
        brandRepository.save(updateBrand);
        return ApiResponse.builder()
                .message("Update brand successfully!")
                .status(HttpStatus.OK.value())
                .errors(false)
                .data(updateBrand)
                .build();

    }

    @Override
    public ApiResponse deleteBrand(Integer id) {
        Map<String,String> errorsMap=new HashMap<>();
        if(!brandRepository.existsById(id)){
            errorsMap.put("id","id not found!");
            return ApiResponse.builder()
                    .message("id not found!")
                    .status(HttpStatus.NOT_FOUND.value())
                    .errors(errorsMap)
                    .build();
        }
        try{
            Brand brand=brandRepository.findById(id).orElse(null);
            assert brand != null;
            brand.setDeleted(true);
            brandRepository.save(brand);
            return ApiResponse.builder()
                    .message("Delete brand successfully!")
                    .status(HttpStatus.OK.value())
                    .errors(false)
                    .data(brand)
                    .build();
        }catch(Exception e){
            errorsMap.put("brand", "Can't delete Brand!");

            return ApiResponse.builder()
                    .message("Can't delete Brand!")
                    .status(HttpStatus.NOT_MODIFIED.value())
                    .errors(errorsMap)
                    .data(null)
                    .build();
        }

    }
}
