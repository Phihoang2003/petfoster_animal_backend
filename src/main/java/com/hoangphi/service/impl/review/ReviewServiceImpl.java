package com.hoangphi.service.impl.review;

import com.hoangphi.config.JwtProvider;
import com.hoangphi.constant.OrderStatus;
import com.hoangphi.entity.*;
import com.hoangphi.repository.OrderRepository;
import com.hoangphi.repository.ProductRepository;
import com.hoangphi.repository.ReviewRepository;
import com.hoangphi.repository.UserRepository;
import com.hoangphi.request.review.ReviewRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.review.ReviewResponse;
import com.hoangphi.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final JwtProvider jwtProvider;
    private final OrderRepository orderRepository;
    @Override
    public ApiResponse createReview(String jwt, ReviewRequest reviewRequest) {
        Map<String,String> errorsMap=new HashMap<>();
        User user=userRepository.findByUsername(jwtProvider.getUsernameFromToken(jwt)).orElse(null);
        if(user==null){
            errorsMap.put("user","user not found!");
            return ApiResponse.builder()
                    .message("user not found!")
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .errors(errorsMap)
                    .build();
        }
        Orders order=orderRepository.findById(reviewRequest.getOrderId()).orElse(null);
        if(order==null){
            errorsMap.put("order","order not found!");
            return ApiResponse.builder()
                    .message("order not found!")
                    .status(HttpStatus.NOT_FOUND.value())
                    .errors(errorsMap)
                    .build();
        }
        if(order.getUser()!=user){
            errorsMap.put("order", "order is not correct with user!");
            return ApiResponse.builder()
                    .message("Order is not correct with user!")
                    .status(HttpStatus.NOT_FOUND.value())
                    .data(null)
                    .errors(errorsMap).build();
        }
        Product product=productRepository.findById(reviewRequest.getProductId()).orElse(null);
        if(product==null){
            errorsMap.put("product","product not found!");
            return ApiResponse.builder()
                    .message("product not found!")
                    .status(HttpStatus.NOT_FOUND.value())
                    .errors(errorsMap)
                    .build();
        }
        List<OrderDetail> orderDetails=order.getOrderDetails();
        if(orderDetails.stream().filter(orderDetail ->
                orderDetail.getProductRepo().getProduct()==product).toList().isEmpty()){
            errorsMap.put("product","product not found in order!");
            return ApiResponse.builder()
                    .message("product not found in order!")
                    .status(HttpStatus.NOT_FOUND.value())
                    .errors(errorsMap)
                    .build();
        }
        if(!order.getStatus().equalsIgnoreCase(OrderStatus.DELIVERED.getValue())){
            errorsMap.put("order","The order hasn't been delivered!");
            return ApiResponse.builder()
                    .message("The order hasn't been delivered!")
                    .status(HttpStatus.NOT_ACCEPTABLE.value())
                    .errors(errorsMap)
                    .build();
        }
        Review review=reviewRepository.findByOrderIdAndProductId(reviewRequest.getOrderId(),reviewRequest.getProductId()).orElse(null);
        if(review!=null){
            errorsMap.put("review","review already exists!");
            return ApiResponse.builder()
                    .message("review already exists!")
                    .status(HttpStatus.FOUND.value())
                    .errors(errorsMap)
                    .build();
        }
        Review newReview = Review.builder()
                .product(product)
                .order(order)
                .user(user)
                .comment(reviewRequest.getComment())
                .rate(reviewRequest.getRate())
                .build();
        reviewRepository.save(newReview);

        ReviewResponse reviewResponse = ReviewResponse.builder()
                .productId(reviewRequest.getProductId())
                .orderId(reviewRequest.getOrderId())
                .comment(reviewRequest.getComment())
                .rate(reviewRequest.getRate())
                .build();

        return ApiResponse.builder()
                .message("Review successfully!!!")
                .status(HttpStatus.OK.value())
                .errors(false)
                .data(reviewResponse)
                .build();
    }
}
