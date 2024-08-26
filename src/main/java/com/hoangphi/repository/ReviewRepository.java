package com.hoangphi.repository;

import com.hoangphi.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer>{
    @Query(nativeQuery = true,value = "select * from review where [user_id] = :userId and product_id = :productId")
    public List<Review> findReviewByUserAndProduct(@Param("userId") String userId, @Param("productId") String productId);

    @Query(nativeQuery = true, value = "select * from review where [user_id] = :userId and product_id = :productId and order_id = :orderId")
    public Optional<Review> findReviewByUserAndProduct(@Param("userId") String userId,
                                                       @Param("productId") String productId, @Param("orderId") Integer orderId);

    @Query(nativeQuery = true, value = "select* from review where order_id= :orderId and product_id =:productId ")
    public Optional<Review> findByOrderIdAndProductId(@Param("orderId") Integer orderId,
                                                      @Param("productId") String productId);

}
