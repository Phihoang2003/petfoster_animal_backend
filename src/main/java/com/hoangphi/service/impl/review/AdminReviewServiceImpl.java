package com.hoangphi.service.impl.review;

import com.hoangphi.config.JwtProvider;
import com.hoangphi.constant.Constant;
import com.hoangphi.constant.RespMessage;
import com.hoangphi.entity.Product;
import com.hoangphi.entity.Review;
import com.hoangphi.entity.User;
import com.hoangphi.repository.ProductRepository;
import com.hoangphi.repository.ReviewRepository;
import com.hoangphi.repository.UserRepository;
import com.hoangphi.request.review.ReviewReplyRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.common.PaginationResponse;
import com.hoangphi.response.review.DetailRate;
import com.hoangphi.response.review.ReviewDetailsResponse;
import com.hoangphi.response.review.ReviewFilterResponse;
import com.hoangphi.response.takeAction.ProductItem;
import com.hoangphi.response.takeAction.ReviewItem;
import com.hoangphi.service.admin.review.AdminReviewService;
import com.hoangphi.service.impl.take_action.TakeActionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
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
    public ApiResponse filterReviews(Optional<String> productName, Optional<Integer> minStar, Optional<Integer> maxStar, Optional<String> sort, Optional<Integer> page) {
        String name = productName.orElse(null);
        Integer min = minStar.orElse(0);
        Integer max = maxStar.orElse(5);
        String customSort = sort.orElse("");

        List<Product> products = productRepository.findAll();

        if (name != null) {
            products = productRepository.getProductsByNameInReview(name);
        }
        List<ReviewFilterResponse> reviewFilterResponses = new ArrayList<>();
        products.forEach(product->{
            List<Review> noReplyReviews=reviewRepository.getNoReplyReviewByProduct(product.getId());
            ProductItem productItem = takeActionServiceImpl.createProductTakeAction(product);

            reviewFilterResponses.add(
                    ReviewFilterResponse.builder()
                            .productId(productItem.getId())
                            .productName(productItem.getName())
                            .image(productItem.getImage())
                            .rate(productItem.getRating() != null ? productItem.getRating() : 0)
                            .latest(reviewRepository.getLatestReviewByProduct(product.getId()) != null
                                    ? reviewRepository.getLatestReviewByProduct(product.getId()).getCreateAt()
                                    : null)
                            .reviews(productItem.getReviews())
                            .commentNoRep(noReplyReviews.size())
                            .build());
        });
        if (min >= max) {
            return ApiResponse.builder()
                    .message("Max star must larger than Min star")
                    .status(HttpStatus.FAILED_DEPENDENCY.value())
                    .errors("Max star must larger than Min star")
                    .build();
        }
        List<ReviewFilterResponse> filterReviews = new ArrayList<>(reviewFilterResponses.stream()
                .filter(item -> item.getRate() > min && item.getRate() <= max)
                .toList());

        switch (customSort) {
            case "rate-asc":
                filterReviews.sort(Comparator.comparingDouble(ReviewFilterResponse::getRate));
                break;
            case "rate-desc":
                filterReviews.sort(Comparator.comparingDouble(ReviewFilterResponse::getRate).reversed());
                break;
            case "review-asc":
                filterReviews.sort(Comparator.comparingDouble(ReviewFilterResponse::getReviews));
                break;
            case "review-desc":
                filterReviews.sort(Comparator.comparingDouble(ReviewFilterResponse::getReviews).reversed());
                break;
            case "latest-asc":
                filterReviews.sort(Comparator
                        .comparing(review -> review.getLatest() != null ?
                                review.getLatest() : Constant.MIN_DATE));
                break;
            case "latest-desc":
                filterReviews.sort(Comparator
                        .comparing((ReviewFilterResponse review) -> review.getLatest() != null
                                ? review.getLatest()
                                : Constant.MIN_DATE)
                        .reversed());
                break;
            default:
                break;
        }

        Pageable pageable = PageRequest.of(page.orElse(0), 10);
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), filterReviews.size());

        if (startIndex >= endIndex) {
            return ApiResponse.builder()
                    .message(RespMessage.NOT_FOUND.getValue())
                    .data(PaginationResponse.builder().data(new ArrayList<>()).pages(0).build())
                    .errors(true)
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }

        List<ReviewFilterResponse> visibleReviews = filterReviews.subList(startIndex, endIndex);

        Page<ReviewFilterResponse> pagination = new PageImpl<ReviewFilterResponse>(visibleReviews, pageable,
                filterReviews.size());

        return ApiResponse.builder()
                .message("Successfully")
                .status(HttpStatus.OK.value())
                .errors(false)
                .data(PaginationResponse.builder()
                        .data(visibleReviews)
                        .pages(pagination.getTotalPages())
                        .build())
                .build();
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
        Product product = productRepository.findById(productId).orElse(null);
        Boolean notReplyYet = notReply.orElse(false);

        if (product == null) {
            return ApiResponse.builder()
                    .message("Product not found")
                    .status(404)
                    .errors("Product not found")
                    .build();
        }

        List<Review> reviews = new ArrayList<>();
        reviews = notReplyYet ? reviewRepository.getNoReplyReviewByProduct(product.getId())
                : product.getReviews();

        List<ReviewItem> reviewItems = takeActionServiceImpl.getReviewItems(reviews, product);

        return ApiResponse.builder()
                .message("Successfully")
                .status(HttpStatus.OK.value())
                .errors(false)
                .data(reviewItems)
                .build();
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
        Review curReview = reviewRepository.findById(id).orElse(null);

        if (curReview == null) {
            return ApiResponse.builder()
                    .message("Review not found")
                    .status(HttpStatus.NOT_FOUND.value())
                    .errors(true)
                    .data(null)
                    .build();
        }

        List<Review> reviews = reviewRepository.getReplyReviews(curReview.getId());

        if (!reviews.isEmpty()) {
            reviewRepository.deleteAll(reviews);
        }

        reviewRepository.delete(curReview);

        return ApiResponse.builder()
                .message("Successfully")
                .status(HttpStatus.OK.value())
                .errors(false)
                .data(curReview)
                .build();
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
