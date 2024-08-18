package com.hoangphi.repository;

import com.hoangphi.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    @Query("select ci from CartItem ci where ci.productRepo.product.id=:productId and ci.productRepo.size=:size and ci.cart.cartId=:cartId")
    Optional<CartItem> findBySizeAndProductId(@Param("cartId") Integer cartId,@Param("productId") String productId,@Param("size") int size);
}
