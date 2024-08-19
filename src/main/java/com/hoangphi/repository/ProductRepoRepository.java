package com.hoangphi.repository;

import com.hoangphi.entity.Product;
import com.hoangphi.entity.ProductRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepoRepository extends JpaRepository<ProductRepo, Integer>{
    List<ProductRepo> findByProduct(Product product);

    @Query("select rp from ProductRepo rp where rp.isActive=true and rp.product.id=:productId order by rp.size")
    List<ProductRepo> findByProductSorting(@Param("productId") String productId);

    @Query(nativeQuery = true, value = "select * from product_repo where product_id = :productId and size = :size")
    public ProductRepo findProductRepoByIdAndSize(@Param("productId") String productId, @Param("size") Integer size);
}
