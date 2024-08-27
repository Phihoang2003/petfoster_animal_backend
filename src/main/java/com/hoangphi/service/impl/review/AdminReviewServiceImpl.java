package com.hoangphi.service.impl.review;

import com.hoangphi.config.JwtProvider;
import com.hoangphi.entity.Product;
import com.hoangphi.entity.Review;
import com.hoangphi.entity.User;
import com.hoangphi.repository.ProductRepository;
import com.hoangphi.repository.ReviewRepository;
import com.hoangphi.repository.UserRepository;
import com.hoangphi.request.review.ReviewReplyRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.review.DetailRate;
import com.hoangphi.response.review.ReviewDetailsResponse;
import com.hoangphi.response.takeAction.ProductItem;
import com.hoangphi.response.takeAction.ReviewItem;
import com.hoangphi.service.admin.review.AdminReviewService;
import com.hoangphi.service.impl.take_action.TakeActionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminReviewServiceImpl implements AdminReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final ProductRepository productRepository;
    private final TakeActionServiceImpl takeActionServiceImpl;
    @Override
    public ApiResponse filterReviews(Optional<String> name, Optional<Integer> minStar, Optional<Integer> maxStar, Optional<String> sort, Optional<Integer> page) {
        return null;
    }

    @Override
    public ApiResponse reviewDetails(String productId) {
        Product product = productRepository.findById(productId).orElse(null);

        if (product == null) {
            return ApiResponse.builder()
                    .message("Product not found")
                    .status(HttpStatus.NOT_FOUND.value())
                    .errors(true)
                    .build();
        }

        List<Review> reviews = reviewRepository.findByProduct(product);
        ProductItem productItem = takeActionServiceImpl.createProductTakeAction(product);
        DetailRate detailRate = createDetailRate(reviews);
        List<ReviewItem> reviewItems=takeActionServiceImpl.getReviewItems(reviews,product);

        ReviewDetailsResponse reviewResponse = ReviewDetailsResponse.builder()
                .id(productId)
                .name(productItem.getName())
                .image(productItem.getImage())
                //average rating
                .rate(productItem.getRating())
                .detailRate(detailRate)
                .totalRate(reviews.size())
                .reviews(reviewItems)
                .build();

        return ApiResponse.builder()
                .message("Successfully")
                .status(HttpStatus.OK.value())
                .errors(false)
                .data(reviewResponse)
                .build();
    }

    @Override
    public ApiResponse reviewDetailsFilter(String productId, Optional<Boolean> notReply) {
        return null;
    }

    @Override
    public ApiResponse reply(String token, ReviewReplyRequest replayRequest) {
        User user = userRepository.findByUsername(jwtProvider.getUsernameFromToken(token)).orElse(null);

        if (user == null) {
            return ApiResponse.builder()
                    .message("User not found")
                    .status(HttpStatus.NOT_FOUND.value())
                    .errors(true)
                    .data(null)
                    .build();
        }

        Review review = Review.builder()
                .comment(replayRequest.getComment())
                .user(user)
                .repliedId(replayRequest.getIdReply())
                .build();

        reviewRepository.save(review);

        return ApiResponse.builder()
                .message("Successfully")
                .status(HttpStatus.OK.value())
                .errors(false)
                .data(reviewRepository.save(review))
                .build();
    }

    @Override
    public ApiResponse delete(Integer id) {
        return null;
    }
    private DetailRate createDetailRate(List<Review> reviews) {

        return DetailRate.builder()
                .one(getRateNumber(reviews, 1))
                .two(getRateNumber(reviews, 2))
                .three(getRateNumber(reviews, 3))
                .four(getRateNumber(reviews, 4))
                .five(getRateNumber(reviews, 5))
                .build();
    }
    private Integer getRateNumber(List<Review> reviews,int rate){
        return (int) reviews.stream().filter(review->review.getRate()==rate).toList().size();
    }
}
