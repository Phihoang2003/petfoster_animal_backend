package com.hoangphi.repository;

import com.hoangphi.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer>{
    @Query(nativeQuery = true,value = "select * from review where [user_id] = :userId and product_id = :productId")
    public List<Review> findReviewByUserAndProduct(@Param("userId") String userId, @Param("productId") String productId);
}
