package com.hoangphi.service.impl.take_action;

import com.hoangphi.constant.RespMessage;
import com.hoangphi.entity.OrderDetail;
import com.hoangphi.entity.Product;
import com.hoangphi.entity.ProductRepo;
import com.hoangphi.entity.Review;
import com.hoangphi.repository.ProductRepoRepository;
import com.hoangphi.repository.ProductRepository;
import com.hoangphi.repository.ReviewRepository;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.takeAction.BestSellersResponse;
import com.hoangphi.response.takeAction.ProductItem;
import com.hoangphi.response.takeAction.ReviewItem;
import com.hoangphi.response.takeAction.TakeActionResponse;
import com.hoangphi.service.image.ImageServiceUtils;
import com.hoangphi.service.take_action.TakeActionService;
import com.hoangphi.utils.FormatUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TakeActionServiceImpl implements TakeActionService {
    private final ReviewRepository reviewRepository;
    private final ProductRepoRepository productRepoRepository;
    private final FormatUtils formatUtils;
    private final ImageServiceUtils imageServiceUtils;
    private final ProductRepository productRepository;;

    public ProductItem createProductTakeAction(Product product){
        String image="";
        int discount=8;
        List<Review> reviews=reviewRepository.findByProduct(product);
        Double rating=reviews.stream().mapToDouble(Review::getRate).average().orElse(0.0);

        if (!product.getImgs().isEmpty()) {
            image = product.getImgs().get(0).getNameImg();
        }
        List<ReviewItem> reviewItems=getReviewItems(reviews,product);
        List<Integer> sizes=productRepoRepository.findSizeByProduct(product.getId());
        ProductRepo minRepo=productRepoRepository.findByProductMinPrice(product.getId());
        return ProductItem
                .builder()
                .id(product.getId())
                .size(sizes)
                .discount(discount)
                .image(imageServiceUtils.getImage(image))
                .name(product.getName())
                .brand(product.getBrand().getBrand())
                .price(minRepo.getOutPrice().intValue())
                .oldPrice((int) (minRepo.getOutPrice() + (minRepo.getOutPrice() * (discount / 100.0))))
                .rating(rating)
                .reviews(reviews.size())
                .reviewItems(reviewItems)
                .build();
    }

    public List<ReviewItem> getReviewItems(List<Review> reviews,Product product){
        List<ReviewItem> reviewItems=new ArrayList<>();
        reviews.forEach(review->{
            List<Integer> sizes=new ArrayList<>();
            List<OrderDetail> orderDetails=review.getOrder().getOrderDetails();
            orderDetails.forEach(item->{
                if(product.getId().equalsIgnoreCase(item.getProductRepo().getProduct().getId())){
                    sizes.add(item.getProductRepo().getSize());
                }
            });
            List<Review> replyReviews=reviewRepository.getReplyReviews(review.getId());
            List<ReviewItem> replyReviewItems=new ArrayList<>();

            replyReviews.forEach(item->{
                replyReviewItems.add(
                        ReviewItem.builder()
                                .id(item.getId())
                                .name(item.getUser().getUsername())
                                .rating(item.getRate())
                                .sizes(null)
                                .avatar(item.getUser().getAvatar() == null ? null
                                        : imageServiceUtils.getImage(item.getUser().getAvatar()))
                                .comment(item.getComment())
                                .createAt(formatUtils.dateToString(item.getCreateAt(), "MMM d, yyyy"))
                                .replyItems(null)
                                .displayName(item.getUser().getDisplayName())
                                .build());
            });
            reviewItems.add(
                    ReviewItem.builder()
                            .id(review.getId())
                            .name(review.getUser().getUsername())
                            .rating(review.getRate())
                            .displayName(review.getUser().getDisplayName())
                            .avatar(review.getUser().getAvatar()==null?null:
                                    imageServiceUtils.getImage(review.getUser().getAvatar()))
                            .sizes(sizes)
                            .comment(review.getComment())
                            .createAt(formatUtils.dateToString(review.getCreateAt(),"MMM d, yyyy"))
                            .replyItems(replyReviewItems)
                            .build());
        });
        return reviewItems;
    }

    @Override
    public ApiResponse homePageTakeAction() {
        List<ProductItem> newArricals = new ArrayList<>();
        productRepository.selectNewArrivals().stream().forEach((product) -> {
            newArricals.add(this.createProductTakeAction(product));
        });

        if (newArricals.isEmpty()) {
            return ApiResponse.builder()
                    .message(RespMessage.INTERNAL_SERVER_ERROR.getValue())
                    .data(newArricals)
                    .errors(true)
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build();
        }
        ;
        return ApiResponse.builder()
                .message(RespMessage.SUCCESS.getValue())
                .errors(false)
                .status(HttpStatus.OK.value())
                .data(TakeActionResponse.builder()
                        .newArrivals(newArricals)
                        .build())
                .build();
    }

    @Override
    public ApiResponse bestSellers(Optional<Integer> page) {
        List<ProductItem> bestSellers = new ArrayList<>();
        Pageable pageable = PageRequest.of(page.orElse(0), 16);
        List<Product> contents = productRepository.findAllProducts();

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), contents.size());

        if (startIndex >= endIndex) {
            return ApiResponse.builder()
                    .message(RespMessage.NOT_FOUND.getValue())
                    .data(null)
                    .errors(true)
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }

        List<Product> pageContent = contents.subList(startIndex, endIndex);

        Page<Product> pagination = new PageImpl<Product>(pageContent, pageable, contents.size());

        pagination.getContent().forEach((product) -> {
            ProductItem productTakeAction = this.createProductTakeAction(product);
            bestSellers.add(productTakeAction);
        });

        return ApiResponse.builder()
                .message(RespMessage.SUCCESS.getValue())
                .status(HttpStatus.OK.value())
                .errors(false)
                .data(BestSellersResponse.builder()
                        .data(bestSellers)
                        .pages(pagination.getTotalPages())
                        .build())
                .build();
    }
}
