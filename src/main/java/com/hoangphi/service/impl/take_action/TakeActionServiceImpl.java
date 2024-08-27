package com.hoangphi.service.impl.take_action;

import com.hoangphi.entity.OrderDetail;
import com.hoangphi.entity.Product;
import com.hoangphi.entity.Review;
import com.hoangphi.repository.ProductRepoRepository;
import com.hoangphi.repository.ReviewRepository;
import com.hoangphi.response.takeAction.ProductItem;
import com.hoangphi.response.takeAction.ReviewItem;
import com.hoangphi.service.take_action.TakeActionService;
import com.hoangphi.utils.FormatUtils;
import com.hoangphi.utils.PortUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TakeActionServiceImpl implements TakeActionService {
    private final ReviewRepository reviewRepository;
    private final ProductRepoRepository productRepoRepository;
    private final PortUtils portUtils;
    private final FormatUtils formatUtils;
    public ProductItem createProductTakeAction(Product product){
        String image="";
        int discount=0;
        List<Review> reviews=reviewRepository.findByProduct(product);
        Double rating=reviews.stream().mapToDouble(Review::getRate).average().orElse(0.0);

        if (!product.getImgs().isEmpty()) {
            image = product.getImgs().get(0).getNameImg();
        }

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
                                        : portUtils.getUrlImage(item.getUser().getAvatar()))
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
                                    portUtils.getUrlImage(review.getUser().getAvatar()))
                            .sizes(sizes)
                            .comment(review.getComment())
                            .createAt(formatUtils.dateToString(review.getCreateAt(),"MMM d, yyyy"))
                            .replyItems(replyReviewItems)
                            .build());
        });
        return reviewItems;
    }
}
