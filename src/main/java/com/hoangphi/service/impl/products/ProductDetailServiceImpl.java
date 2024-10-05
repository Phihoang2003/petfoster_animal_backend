package com.hoangphi.service.impl.products;

import com.hoangphi.entity.Imgs;
import com.hoangphi.entity.Product;
import com.hoangphi.entity.ProductRepo;
import com.hoangphi.repository.ProductRepoRepository;
import com.hoangphi.repository.ProductRepository;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.products_detail.ProductDetail;
import com.hoangphi.response.products_detail.SizeAndPrice;
import com.hoangphi.response.takeAction.ProductItem;
import com.hoangphi.service.image.ImageServiceUtils;
import com.hoangphi.service.impl.take_action.TakeActionServiceImpl;
import com.hoangphi.service.product.ProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductDetailServiceImpl implements ProductDetailService {
    private final ProductRepository productRepository;
    private final TakeActionServiceImpl takeActionServiceImpl;
    private final ProductRepoRepository productRepoRepository;
    private final ImageServiceUtils imageServiceUtils;
    @Override
    public ApiResponse productDetails(String id) {
        Map<String, String> errorMap = new HashMap<>();

        Product product = productRepository.findById(id).orElse(null);

        if (product == null) {
            errorMap.put("product id", "product id is not exists!!!");
            return ApiResponse.builder()
                    .message("Failure!!!")
                    .errors(errorMap)
                    .build();
        }

        ProductItem productItem = takeActionServiceImpl.createProductTakeAction(product);
        List<SizeAndPrice> sizeAndPrices = getSizeAndPrices(product);
        sizeAndPrices.sort(Comparator.comparing(SizeAndPrice::getSize));

        return ApiResponse.builder()
                .message("Successfully!!!")
                .status(200)
                .errors(false)
                .data(
                        ProductDetail.builder()
                                .id(productItem.getId())
                                .name(productItem.getName())
                                .brand(productItem.getBrand())
                                .discount(productItem.getDiscount())
                                .image(productItem.getImage())
                                .images(getImgNames(product))
                                .rating(productItem.getRating())
                                .description(product.getDesc())
                                .sizeAndPrice(sizeAndPrices)
                                .suggestions(getSuggestionProducts(id))
                                .reviews(productItem.getReviews())
                                .reviewItems(productItem.getReviewItems())
                                .build())
                .build();
    }

    @Override
    public ApiResponse getProductTypesAndBrands() {
        return null;
    }

    public List<SizeAndPrice> getSizeAndPrices(Product product) {

        int discount = 8;
        List<SizeAndPrice> sizeAndprices = new ArrayList<>();

        for (ProductRepo productRepo : productRepoRepository.findByProductSorting(product.getId())) {
            sizeAndprices.add(SizeAndPrice.builder()
                    .size(productRepo.getSize())
                    .price(productRepo.getOutPrice())
                    .oldPrice(productRepo.getOutPrice() + (productRepo.getOutPrice() * (discount / 100.0)))
                    .repo(productRepo.getQuantity())
                    .build());
        }

        return sizeAndprices;
    }
    public List<String> getImgNames(Product product) {

        List<String> imgs = new ArrayList<>();

        for (Imgs img : product.getImgs()) {
            imgs.add(imageServiceUtils.getImage(img.getNameImg()));
        }

        return imgs;
    }
    public List<ProductItem> getSuggestionProducts(String id) {

        List<Product> sameTypeProducts = productRepository.getSameTypeProducts(id);
        List<ProductItem> suggestionProducts = new ArrayList<>();

        for (Product sameTypeProduct : sameTypeProducts) {
            suggestionProducts.add(takeActionServiceImpl.createProductTakeAction(sameTypeProduct));
        }

        return suggestionProducts;
    }
}
