package com.hoangphi.service.impl.recentView;

import com.hoangphi.config.SecurityUtils;
import com.hoangphi.entity.*;
import com.hoangphi.repository.ProductRepository;
import com.hoangphi.repository.RecentViewRepository;
import com.hoangphi.repository.ReviewRepository;
import com.hoangphi.repository.UserRepository;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.recent_view.RecentViewResponse;
import com.hoangphi.service.image.ImageServiceUtils;
import com.hoangphi.service.recent_view.RecentViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RecentViewImpl implements RecentViewService {
    private final RecentViewRepository recentViewRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final ImageServiceUtils imageServiceUtils;
    @Override
    public ApiResponse getRecentView(String jwt) {
        User user=userRepository.findByUsername(securityUtils.getCurrentUsername()).orElse(null);
        Map<String, String> errorsMap = new HashMap<>();
        if(user==null){
            errorsMap.put("user", "user not found");
            return ApiResponse.builder()
                    .message("Unauthorized")
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .errors(errorsMap)
                    .data(null)
                    .build();
        }
        List<RecentView> recentViews=recentViewRepository.findAll(user.getId());
        List<RecentViewResponse> listResponse=new ArrayList<>();
        recentViews.forEach(recentView -> {
            Integer[] sizes=recentView.getProduct().getProductsRepo()
                    .stream()
                    .map(ProductRepo::getSize)
                    .toArray(Integer[]::new);
            List<Review> reviews = reviewRepository.findReviewByUserAndProduct(
                    user.getId(), recentView.getProduct().getId());
            Integer rating=reviews.stream()
                    .findFirst()
                    .map(Review::getRate)
                    .orElse(0);
            RecentViewResponse recentViewResponse=RecentViewResponse.builder()
                    .id(recentView.getProduct().getId())
                    .brand(recentView.getProduct().getBrand().getBrand())
                    .size(sizes)
                    .discount(10)
                    .image(imageServiceUtils.getImage(recentView.getProduct().getImgs()
                            .stream()
                            .findFirst()
                            .map(Imgs::getNameImg)
                            .orElse(null)))
                    .name(recentView.getProduct().getName())
                    .oldPrice(recentView.getProduct().getProductsRepo()
                            .stream()
                            .findFirst()
                            .map(ProductRepo::getInPrice)
                            .orElse(0.0))
                    .price(recentView.getProduct().getProductsRepo()
                            .stream()
                            .findFirst()
                            .map(ProductRepo::getInPrice)
                            .orElse(0.0))
                    .rating(rating)
                    .build();

            listResponse.add(recentViewResponse);
        });
        return ApiResponse.builder()
                .data(listResponse)
                .status(HttpStatus.OK.value())
                .message("Successfully get_all_recent_view")
                .build();
    }

    @Override
    public ApiResponse putRecentView(String jwt, String productId) {
        User user=userRepository.findByUsername(securityUtils.getCurrentUsername()).orElse(null);
        Map<String, String> errorsMap = new HashMap<>();
        if(user==null){
            errorsMap.put("user", "user not found");
            return ApiResponse.builder()
                    .message("Unauthorized")
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .errors(errorsMap)
                    .data(null)
                    .build();
        }
        Product product=productRepository.findById(productId).orElse(null);
        if(product==null){
            errorsMap.put("product", "product not found");
            return ApiResponse.builder()
                    .message("Product not found")
                    .status(HttpStatus.NOT_FOUND.value())
                    .errors(errorsMap)
                    .data(null)
                    .build();
        }

        List<RecentView> recentViews=recentViewRepository.findAll(user.getId());
        RecentView existRecentView=recentViewRepository.existByProductId(user.getId(),productId);
        if(recentViews.size()>=10){
            int startIndex=existRecentView!=null?10:9;
            for(int i=startIndex;i<recentViews.size();i++){
                recentViewRepository.delete(recentViews.get(i));
            }
            recentViews=recentViewRepository.findAll(user.getId());
        }
        RecentView recentView=RecentView.builder()
                .id(existRecentView!=null?existRecentView.getId():null)
                .viewAt( LocalDateTime.now())
                .user(user)
                .product(product)
                .build();
        recentViews.add(recentView);
        recentViewRepository.saveAll(recentViews);
        return ApiResponse.builder()
                .data(getRecentView(jwt).getData())
                .status(HttpStatus.OK.value())
                .message("Update recent view successfully!")
                .build();

    }
}
