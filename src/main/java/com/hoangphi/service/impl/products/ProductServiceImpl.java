package com.hoangphi.service.impl.products;

import com.hoangphi.entity.*;
import com.hoangphi.repository.*;
import com.hoangphi.request.CreateProductRequest;
import com.hoangphi.request.products.ProductRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.admin.products.ProductService;
import com.hoangphi.utils.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;
    private final ProductRepoRepository productRepoRepository;
    private final ImagesRepository imagesRepository;
    private final BrandRepository brandRepository;

    @Override
    public ApiResponse createProduct(CreateProductRequest createProductRequest, List<MultipartFile> images) {
        List<Product> products =productRepository.findAllNoActive();
        Brand brand=brandRepository.findById(createProductRequest.getBrand()).orElse(null);
        if(brand==null){
            return ApiResponse.builder()
                    .message("Can not find brand with id: "+createProductRequest.getBrand())
                    .status(404)
                    .errors(true)
                    .data(null)
                    .build();
        }
        ProductType productType = productTypeRepository.findById(createProductRequest.getType()).orElse(null);
        if (productType == null) {
            return ApiResponse.builder()
                    .message("Type id is not exists")
                    .status(404)
                    .errors("PRODUCT_TYPE_NOT_FOUND")
                    .data(null)
                    .build();
        }
        String newProductId;
        if (products.isEmpty()) {
            newProductId = "PD0001"; // Ví dụ: ID mặc định nếu không có sản phẩm nào
        } else {
            newProductId = getNextId(products.get(products.size() - 1).getId());
        }
        Product product=Product.builder()
                .id(newProductId)
                .name(createProductRequest.getName())
                .brand(brand)
                .isActive(true)
                .desc(createProductRequest.getDescription())
                .productType(getNewTypeForProduct(createProductRequest.getType()))
                .build();
        productRepository.save(product);
        List<ProductRepo> repoList=new ArrayList<>();
        createProductRequest.getRepo().forEach(productRepo -> {
            ProductRepo repo=ProductRepo.builder()
                    .size(productRepo.getSize())
                    .inPrice(productRepo.getInPrice())
                    .outPrice(productRepo.getOutPrice())
                    .inStock(productRepo.getInStock())
                    .quantity(productRepo.getQuantity())
                    .product(product)
                    .isActive(true)
                    .build();
            repoList.add(repo);

        });
        productRepoRepository.saveAll(repoList);
        List<Imgs> imgsList=new ArrayList<>();
        images.forEach(image->{
            try {
                File file= ImageUtils.createFileImage();
                image.transferTo(new File(file.getAbsolutePath()));
                Imgs newImg=Imgs.builder()
                        .product(product)
                        .nameImg(file.getName())
                        .build();
                imgsList.add(newImg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
        imagesRepository.saveAll(imgsList);
        product.setProductType(productType);
        product.setProductsRepo(repoList);
        product.setImgs(imgsList);

        return ApiResponse.builder()
                .message("Successfully")
                .status(200)
                .errors(false)
                .data(product)
                .build();
    }

    @Override
    public ApiResponse updateProduct(String id, ProductRequest updateProductReq) {
        Map<String,String> errorsMap=new HashMap<>();
        if (!productRepository.existsById(id)) {
            errorsMap.put("PRODUCT_NOT_FOUND","Can't find Product ID");
            return ApiResponse.builder()
                    .message("Can't find Product ID")
                    .status(404)
                    .errors(errorsMap)
                    .data(null)
                    .build();
        }
        Product selectedProduct=productRepository.findById(id).orElse(null);
        if(selectedProduct==null){
            errorsMap.put("PRODUCT_NOT_FOUND","Can't find Product ID");
            return ApiResponse.builder()
                    .message("Can't find Product ID")
                    .status(404)
                    .errors(errorsMap)
                    .data(null)
                    .build();
        }
        if(!updateProductReq.getProductsRepo().isEmpty()){
            selectedProduct.setProductsRepo(updateProductReq.getProductsRepo());
            updateProductReq.getProductsRepo().forEach(item->{
                item.setProduct(selectedProduct);
            });
            updateProductReq.getProductsRepo().forEach(item->{
                if(item.getId()==null){
                    productRepoRepository.save(item);
                }else {
                    if (!productRepoRepository.existsById(item.getId())) {
                        productRepoRepository.save(item);
                    }
                }
            });

        }
        ProductType productType=productTypeRepository.findById(updateProductReq.getProductType()).orElse(null);
        if(productType==null){
            errorsMap.put("PRODUCT_TYPE_NOT_FOUND","Can't find Product Type ID");
            return ApiResponse.builder()
                    .message("Can't find Product Type ID")
                    .status(404)
                    .errors(errorsMap)
                    .data(null)
                    .build();
        }
        selectedProduct.setProductType(productType);
        selectedProduct.setName(updateProductReq.getName());
        selectedProduct.setBrand(updateProductReq.getBrand());
        selectedProduct.setDesc(updateProductReq.getDesc());
        return ApiResponse.builder()
                .message("Query product Successfully")
                .status(HttpStatus.OK.value())
                .errors(null)
                .data(productRepository.save(selectedProduct))
                .build();

    }

    public ProductType getNewTypeForProduct(String idType) {
        return productTypeRepository.findById(idType).orElse(null);
    }
    public String getNextId(String lastId){
        String nextId="";
        String first=lastId.substring(0,2);
        String last=lastId.substring(2);
        int number=Integer.parseInt(last);
        double log=Math.log10(number);

        if(log<1){
            nextId=first+"000"+ ++number;
        }else if(log<2){
            nextId=first+"00"+ ++number;
        }else if(log<3){
            nextId=first+"0"+ ++number;
        }else{
            nextId=first+ ++number;
        }
        return nextId;
    }
}
