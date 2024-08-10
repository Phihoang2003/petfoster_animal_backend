package com.hoangphi.repository;

import com.hoangphi.entity.ProductRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepoRepository extends JpaRepository<ProductRepo, Integer>{
}
