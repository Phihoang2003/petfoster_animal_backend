package com.hoangphi.repository;

import com.hoangphi.entity.Product;
import com.hoangphi.response.ApiResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
    //query on object not on table
    @Query("SELECT p FROM Product p where p.isActive = true")
    public List<Product> findAll();

    @Query("SELECT p FROM Product p")
    public List<Product> findAllNoActive();
    boolean existsById(String id);

    @Query("select p from Product p where p.id = :id")
    public Optional<Product> findById(@Param("id") String id);

    @Query(nativeQuery = true, value = "select * from product " +
            "where product_name like %:name%")
    public List<Product> getProductsByNameInReview(@Param("name") String name);
    @Query(nativeQuery = true, value = "select top 8 * from product p where p.is_active = 1 order by create_at desc")
    public List<Product> selectNewArrivals();

    @Query(nativeQuery = true, value = "select * from product p join product_type t on t.product_type_id = p.[type_id] where p.product_id in ( select top 100 product_id from order_detail od join product_repo rp on rp.product_repo_id = od.product_repo_id group by product_id ) and p.is_active = 1")
    List<Product> findAllProducts();
}
