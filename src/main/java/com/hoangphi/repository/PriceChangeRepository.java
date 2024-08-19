package com.hoangphi.repository;

import com.hoangphi.entity.PriceChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceChangeRepository extends JpaRepository<PriceChange,Integer> {
    @Query("select pc from PriceChange pc where pc.productRepo.product.id = :idProduct and pc.productRepo.isActive = true")
    List<PriceChange> findByProductId(@Param("idProduct") String idProduct);
}
