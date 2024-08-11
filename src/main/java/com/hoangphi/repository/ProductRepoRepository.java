package com.hoangphi.repository;

import com.hoangphi.entity.Product;
import com.hoangphi.entity.ProductRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepoRepository extends JpaRepository<ProductRepo, Integer>{
    List<ProductRepo> findByProduct(Product product);
}
