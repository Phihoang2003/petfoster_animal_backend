package com.hoangphi.service.impl.recentView;

import com.hoangphi.config.JwtProvider;
import com.hoangphi.entity.*;
import com.hoangphi.repository.RecentViewRepository;
import com.hoangphi.repository.ReviewRepository;
import com.hoangphi.repository.UserRepository;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.recent_view.RecentViewResponse;
import com.hoangphi.service.recent_view.RecentViewService;
import com.hoangphi.utils.PortUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecentViewImpl implements RecentViewService {
    private final RecentViewRepository recentViewRepository;
    private final UserRepository userRepository;
    private final PortUtils   portUtils;
    private final JwtProvider jwtProvider;
    private final ReviewRepository reviewRepository;
    @Override
    public ApiResponse getRecentView(String jwt) {
        User user=userRepository.findByUsername(jwtProvider.getUsernameFromToken(jwt)).orElse(null);
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
                    .image(portUtils.getUrlImage(recentView.getProduct().getImgs()
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
}
