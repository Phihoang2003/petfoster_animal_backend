package com.hoangphi.service.impl.products;

import com.hoangphi.constant.RespMessage;
import com.hoangphi.entity.*;
import com.hoangphi.repository.*;
import com.hoangphi.request.CreateProductRequest;
import com.hoangphi.request.products.ProductInfoRequest;
import com.hoangphi.request.products.ProductRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.common.PaginationResponse;
import com.hoangphi.response.products_manage.ProductDetailManageResponse;
import com.hoangphi.response.products_manage.ProductInfoResponse;
import com.hoangphi.response.products_manage.ProductManageResponse;
import com.hoangphi.service.admin.products.ProductService;
import com.hoangphi.service.image.ImageServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;
    private final ProductRepoRepository productRepoRepository;
    private final ImagesRepository imagesRepository;
    private final BrandRepository brandRepository;
    private final ImageServiceUtils imageServiceUtils;

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
        List<String> imageFilter=imageServiceUtils.uploadFiles(images);
        imageFilter.forEach(image->{
            try {
//                File file= ImageUtils.createFileImage();
//                image.transferTo(new File(file.getAbsolutePath()));
                Imgs newImg=Imgs.builder()
                        .product(product)
                        .nameImg(image)
                        .build();
                imgsList.add(newImg);
            } catch (Exception e) {
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
//        if (images != null && !images.isEmpty()) {
//            // get old list images related to product
//            List<Imgs> existingImgs = imagesRepository.findByProduct(selectProduct);
//
//
//            if (!existingImgs.isEmpty()) {
//                existingImgs.forEach(img -> {
//                    ImageUtils.deleteImg(img.getNameImg());  // Xóa ảnh từ local storage
//                });
//                imagesRepository.deleteAll(existingImgs);  // Xóa bản ghi ảnh cũ khỏi cơ sở dữ liệu
//            }
//
//            // add new images to product
//            List<Imgs> imgsList = new ArrayList<>();
//            images.forEach(image -> {
//                try {
//                    File file = ImageUtils.createFileImage();
//                    image.transferTo(new File(file.getAbsolutePath()));
//                    Imgs newImg = Imgs.builder()
//                            .product(selectProduct)
//                            .nameImg(file.getName())
//                            .build();
//                    imgsList.add(newImg);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//            imagesRepository.saveAll(imgsList);
//            selectProduct.setImgs(imgsList);
//        }
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

    @Override
    public ApiResponse getProductInfo(String id) {
        if (id.isEmpty()) {
            return ApiResponse.builder()
                    .message("Id invalid")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .errors(true)
                    .data(null)
                    .build();
        }

        Product selectProduct = productRepository.findById(id).orElse(null);

        if (selectProduct == null) {
            return ApiResponse.builder()
                    .message("Can't found product by id")
                    .status(404)
                    .errors(true)
                    .data(null)
                    .build();
        }

        return ApiResponse.builder()
                .message("Successfully !")
                .status(HttpStatus.OK.value())
                .errors(false)
                .data(ProductInfoResponse.builder()
                        .id(selectProduct.getId())
                        .name(selectProduct.getName())
                        .brand(selectProduct.getBrand().getId() + "")
                        .type(selectProduct.getProductType().getId())
                        .description(selectProduct.getDesc())
                        .build())
                .build();
    }

    @Override
    public ApiResponse getProduct(String id) {
        Product selectProduct = productRepository.findById(id).orElse(null);

        if (selectProduct == null) {
            return ApiResponse.builder()
                    .message("Can't found Product ID")
                    .status(HttpStatus.OK.value())
                    .errors(null)
                    .data(null)
                    .build();
        }
        ProductDetailManageResponse data = ProductDetailManageResponse.builder()
                .id(selectProduct.getId())
                .name(selectProduct.getName())
                .description(selectProduct.getDesc())
                .brand(selectProduct.getBrand().getBrand())
                .type(selectProduct.getProductType().getName())
                .repo(selectProduct.getProductsRepo())
                .images(selectProduct.getImgs().stream().map((image) -> {
                    Map<String, String> imageObj = new HashMap<>();
                    imageObj.put("image", imageServiceUtils.getImage(image.getNameImg()));
                    imageObj.put("id", image.getId().toString());
                    return imageObj;
                }))
                .build();

        return ApiResponse.builder()
                .message("Query product Successfully")
                .status(HttpStatus.OK.value())
                .errors(null)
                .data(data)
                .build();
    }

    @Override
    public ApiResponse getAllProduct(Optional<Integer> page) {
        List<ProductManageResponse> productItems = new ArrayList<>();
        List<Product> products = productRepository.findAll();

        Pageable pageable = PageRequest.of(page.orElse(0), 10);
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size());

        if (startIndex >= endIndex) {
            return ApiResponse.builder()
                    .message(RespMessage.NOT_FOUND.getValue())
                    .data(null)
                    .errors(true)
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }

        List<Product> visibleProducts = products.subList(startIndex, endIndex);

        visibleProducts.forEach(product -> {
            productItems.add(ProductManageResponse.builder()
                    .id(product.getId())
                    .image(imageServiceUtils.getImage(product.getImgs().get(0).getNameImg()))
                    .brand(product.getBrand().getBrand())
                    .name(product.getName())
                    .type(product.getProductType().getName())
                    .repo(productRepoRepository.findByProduct(product))
                    .build());
        });

        Page<ProductManageResponse> pagination = new PageImpl<ProductManageResponse>(productItems, pageable,
                products.size());

        return ApiResponse.builder()
                .message("Query product Successfully")
                .status(HttpStatus.OK.value())
                .errors(false)
                .data(PaginationResponse.builder().data(pagination.getContent())
                        .pages(pagination.getTotalPages()).build())
                .build();
    }

    @Override
    public ApiResponse updateProductWithInfo(String id, ProductInfoRequest productInfoRequest) {
        if (id.isEmpty() || !id.equals(productInfoRequest.getId())) {
            return ApiResponse.builder()
                    .message("Id invalid")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .errors(true)
                    .data(null)
                    .build();
        }

        Product selectProduct = productRepository.findById(id).orElse(null);

        if (selectProduct == null) {
            return ApiResponse.builder()
                    .message("Can't found product by id")
                    .status(404)
                    .errors(true)
                    .data(null)
                    .build();
        }

        // find brand
        Brand brand = brandRepository.findById(productInfoRequest.getBrand()).orElse(null);

        if (brand == null) {
            return ApiResponse.builder()
                    .message("Can't found brand by " + productInfoRequest.getBrand())
                    .status(404)
                    .errors(true)
                    .data(null)
                    .build();
        }

        // all good
        selectProduct.setBrand(brand);
        selectProduct.setProductType(getNewTypeForProduct(productInfoRequest.getType(), selectProduct));
        selectProduct.setName(productInfoRequest.getName());
        selectProduct.setDesc(productInfoRequest.getDescription());

        return ApiResponse.builder()
                .message("Update product successfully")
                .status(HttpStatus.OK.value())
                .errors(null)
                .data(productRepository.save(selectProduct))
                .build();
    }

    @Override
    public ApiResponse deleteProduct(String id) {
        if(!productRepository.existsById(id)){
            return ApiResponse.builder()
                    .message("Can't find Product ID")
                    .status(HttpStatus.NOT_FOUND.value())
                    .errors(true)
                    .data(null)
                    .build();
        }
        Product selectProduct=productRepository.findById(id).orElse(null);
        if(selectProduct==null){
            return ApiResponse.builder()
                    .message("Can't find Product ID")
                    .status(HttpStatus.NOT_FOUND.value())
                    .errors(true)
                    .data(null)
                    .build();
        }
        selectProduct.setActive(false);
        return ApiResponse.builder()
                .message("Delete product Successfully")
                .status(HttpStatus.OK.value())
                .errors(null)
                .data(productRepository.save(selectProduct))
                .build();
    }

    public ProductType getNewTypeForProduct(String idType, Product product) {
        //not need query database if type not change
        if (idType.equals(product.getProductType().getId())) {
            return product.getProductType();
        }

        ProductType newType = productTypeRepository.findById(idType).orElse(null);

        if (newType == null) {
            return product.getProductType();
        }

        return newType;
    }

    public ProductType getNewTypeForProduct(String idType) {
        //must query database to get new type
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
