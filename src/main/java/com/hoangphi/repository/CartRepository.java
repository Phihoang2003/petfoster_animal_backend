package com.hoangphi.repository;

import com.hoangphi.entity.Carts;
import com.hoangphi.request.carts.CartRequest;
import com.hoangphi.response.ApiResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Carts, Integer> {
    @Query(nativeQuery = true,value="select * from carts where [user_id]=:userId")
    Optional<Carts> findCart(@Param("userId") String userId);


}
