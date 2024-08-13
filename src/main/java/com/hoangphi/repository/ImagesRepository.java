package com.hoangphi.repository;

import com.hoangphi.entity.Imgs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagesRepository extends JpaRepository<Imgs, Integer> {
    @Query(nativeQuery = true,value = "select * from imgs where product_id=:productId")
    public List<Imgs> getImagesByProductId(@Param("productId") String productId);
}
