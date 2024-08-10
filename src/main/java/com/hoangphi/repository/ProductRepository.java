package com.hoangphi.repository;

import com.hoangphi.entity.Product;
import com.hoangphi.response.ApiResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
    //query on object not on table
    @Query("SELECT p FROM Product p where p.isActive = true")
    public List<Product> findAll();

    @Query("SELECT p FROM Product p")
    public List<Product> findAllNoActive();
}
